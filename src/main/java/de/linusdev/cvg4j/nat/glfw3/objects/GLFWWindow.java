/*
 * Copyright (c) 2023 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.cvg4j.nat.glfw3.objects;

import de.linusdev.cvg4j.api.misc.annos.CallOnlyFromUIThread;
import de.linusdev.cvg4j.nat.glad.custom.DebugMessageListener;
import de.linusdev.cvg4j.nat.glfw3.custom.FrameInfoImpl;
import de.linusdev.cvg4j.nat.glfw3.custom.GLFWWindowHints;
import de.linusdev.cvg4j.nat.glfw3.custom.RenderAPI;
import de.linusdev.cvg4j.nat.glfw3.custom.UpdateListener;
import de.linusdev.cvg4j.nat.glfw3.custom.window.AbstractGLFWWindowListeners;
import de.linusdev.cvg4j.nat.glfw3.custom.window.GLFWWindowListeners;
import de.linusdev.cvg4j.nat.glfw3.exceptions.GLFWException;
import de.linusdev.cvg4j.nat.vulkan.ReturnedVkResult;
import de.linusdev.cvg4j.nat.vulkan.handles.VkInstance;
import de.linusdev.cvg4j.nat.vulkan.handles.VkSurfaceKHR;
import de.linusdev.cvg4j.nat.vulkan.structs.VkAllocationCallbacks;
import de.linusdev.llog.LLog;
import de.linusdev.llog.base.LogInstance;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt2;
import de.linusdev.lutils.nat.pointer.Pointer64;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.linusdev.cvg4j.nat.glfw3.GLFW.*;

@SuppressWarnings("unused")
public class GLFWWindow implements
        AutoCloseable
{

    private static final @NotNull LogInstance log = LLog.getLogInstance();


    protected final long pointer;
    protected boolean closed = false;

    protected final @NotNull FrameInfoImpl frameInfo = new FrameInfoImpl(100);
    protected @Nullable DebugMessageListener debugMessageListener;
    protected final @NotNull GLFWWindowListeners listeners;

    public GLFWWindow(
            @NotNull RenderAPI renderApi,
            @Nullable GLFWWindowHints hints
    ) throws GLFWException {
        glfwInit();

        if(hints == null)
            hints = new GLFWWindowHints();
        if(hints.clientApi == null)
            hints.clientApi = renderApi.getClientApi();

        //Create window
        this.pointer = glfwCreateWindow(true, hints, 500, 500, "Window");
        this.listeners = new GLFWWindowListeners(this);

        //Set user pointer to window id
        glfwSetWindowUserPointer(pointer, this.listeners.getId());

        //Set callbacks
        glfwSetWindowSizeCallback(this.pointer);
        glfwSetFramebufferSizeCallback(this.pointer);
        glfwSetKeyCallback(this.pointer);
        glfwSetCursorPosCallback(this.pointer);
        glfwSetCursorEnterCallback(this.pointer);
        glfwSetMouseButtonCallback(this.pointer);
        glfwSetCharCallback(this.pointer);
        glfwSetScrollCallback(this.pointer);
        glfwSetDropCallback(this.pointer);
    }

    public @NotNull AbstractGLFWWindowListeners listeners() {
        return listeners;
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(pointer, width, height);
    }

    public void setTitle(@NotNull String title) {
        glfwSetWindowTitle(pointer, title);
    }

    public @NotNull BBInt2 getFrameBufferSize(@Nullable BBInt2 size) {
        if(size == null)
            size = BBInt2.newAllocated(null);
        glfwGetFramebufferSize(pointer, size);
        return size;
    }

    @CallOnlyFromUIThread(value = "glfw", creates = true, claims = true)
    @Blocking
    public void show(@NotNull UpdateListener updateListener) {
        glfwShowWindow(pointer);

        long frameStartMillis = System.currentTimeMillis();

        while (!glfwWindowShouldClose(pointer)) {
            updateListener.update0(frameInfo);

            perFrameOperations();

            //submit frame time
            frameInfo.submitFrame(System.currentTimeMillis() - frameStartMillis);
            frameStartMillis = System.currentTimeMillis();
        }

        windowCloseOperations();
    }

    protected void perFrameOperations() {
        // poll for events
        glfwPollEvents();
    }

    protected void windowCloseOperations() {

    }

    @Override
    public void close() {
        closed = true;
        glfwDestroyWindow(pointer);
    }

    public boolean isClosed() {
        return closed;
    }

    public ReturnedVkResult createVkWindowSurface(
            @NotNull VkInstance vkInstance,
            @Nullable VkAllocationCallbacks allocationCallbacks,
            @NotNull VkSurfaceKHR surface
    ) {
        return new ReturnedVkResult(glfwCreateWindowSurface(
                vkInstance.get(),
                pointer,
                Pointer64.of(allocationCallbacks).get(),
                surface.getPointer()
        ));
    }
}

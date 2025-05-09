/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.ljgel.engine.vk.scene;

import de.linusdev.ljgel.engine.scene.Scene;
import de.linusdev.ljgel.engine.scene.State;
import de.linusdev.ljgel.engine.vk.VulkanEngine;
import de.linusdev.ljgel.engine.vk.VulkanGame;
import de.linusdev.ljgel.engine.vk.device.Device;
import de.linusdev.ljgel.engine.vk.pipeline.RasterizationPipeline;
import de.linusdev.ljgel.engine.vk.renderpass.RenderPass;
import de.linusdev.ljgel.engine.vk.swapchain.Extend2D;
import de.linusdev.ljgel.engine.vk.swapchain.SwapChain;
import de.linusdev.ljgel.engine.vk.swapchain.SwapChainRecreationListener;
import de.linusdev.ljgel.engine.vk.window.VulkanWindow;
import de.linusdev.ljgel.nat.vulkan.handles.VkCommandBuffer;
import de.linusdev.ljgel.nat.vulkan.handles.VkFramebuffer;
import de.linusdev.ljgel.nat.vulkan.handles.VkInstance;
import de.linusdev.ljgel.nat.vulkan.structs.VkRect2D;
import de.linusdev.ljgel.nat.vulkan.structs.VkViewport;
import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.thread.var.SyncVar;
import org.jetbrains.annotations.NotNull;

import static de.linusdev.lutils.nat.struct.abstracts.Structure.allocate;

public abstract class VkScene<GAME extends VulkanGame> implements Scene, SwapChainRecreationListener {

    protected final @NotNull VulkanEngine<GAME> engine;
    protected final @NotNull GAME game;

    protected final @NotNull VkInstance vkInstance;
    protected final @NotNull Device device;
    protected final @NotNull SwapChain swapChain;

    protected final @NotNull VulkanWindow window;

    /*
     * Managed by this class
     */
    protected RenderPass renderPass;
    protected RasterizationPipeline pipeLine;
    protected final @NotNull VkViewport viewport;
    protected final @NotNull VkRect2D scissors;

    /*
     * State
     */
    protected final @NotNull SyncVar<@NotNull State> state = SyncVar.createSyncVar(State.CREATED);

    protected VkScene(@NotNull VulkanEngine<GAME> engine) {
        this.engine = engine;
        this.game = engine.getGame();
        this.vkInstance = engine.getVkInstance();
        this.device = engine.getDevice();
        this.swapChain = engine.getSwapChain();
        this.window = engine.getWindow();

        this.viewport = allocate(new VkViewport());
        this.scissors = allocate(new VkRect2D());


        swapChain.addRecreationListener(this);
        calcViewportAndScissors();
    }

    protected void calcViewportAndScissors() {
        viewport.x.set(0f);
        viewport.y.set(0f);
        viewport.width.set(swapChain.getExtend().width());
        viewport.height.set(swapChain.getExtend().height());
        viewport.minDepth.set(0f);
        viewport.maxDepth.set(1f);

        scissors.offset.x.set(0);
        scissors.offset.y.set(0);
        scissors.extent.width.set(swapChain.getExtend().width());
        scissors.extent.height.set(swapChain.getExtend().height());
    }

    @Override
    public void swapChainExtendChanged(@NotNull Stack stack, @NotNull Extend2D newExtend) {
        calcViewportAndScissors();
    }

    protected abstract void render(
            @NotNull Stack stack,
            @NotNull VkInstance vkInstance,
            @NotNull Extend2D extend,
            int frameBufferIndex,
            int currentFrame,
            @NotNull VkCommandBuffer commandBuffer,
            @NotNull VkFramebuffer frameBuffer
    ) ;

    public RenderPass getRenderPass() {
        return renderPass;
    }

    @Override
    public @NotNull SyncVar<@NotNull State> currentState() {
        return state;
    }

    @Override
    public void close() {
        swapChain.removeRecreationListener(this);
        if(pipeLine != null)
            pipeLine.close();
        if(renderPass != null)
            renderPass.close();
    }
}

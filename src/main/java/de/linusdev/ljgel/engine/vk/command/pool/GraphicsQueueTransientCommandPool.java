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

package de.linusdev.ljgel.engine.vk.command.pool;

import de.linusdev.ljgel.engine.vk.VulkanEngine;
import de.linusdev.ljgel.engine.vk.device.Device;
import de.linusdev.ljgel.engine.vk.instance.Instance;
import de.linusdev.ljgel.nat.vulkan.bitmasks.enums.VkCommandBufferUsageFlagBits;
import de.linusdev.ljgel.nat.vulkan.bitmasks.enums.VkCommandPoolCreateFlagBits;
import de.linusdev.ljgel.nat.vulkan.enums.VkCommandBufferLevel;
import de.linusdev.ljgel.nat.vulkan.enums.VkStructureType;
import de.linusdev.ljgel.nat.vulkan.handles.VkCommandBuffer;
import de.linusdev.ljgel.nat.vulkan.handles.VkFence;
import de.linusdev.ljgel.nat.vulkan.handles.VkInstance;
import de.linusdev.ljgel.nat.vulkan.structs.*;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.nat.memory.stack.Stack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static de.linusdev.lutils.nat.pointer.TypedPointer64.ref;
import static de.linusdev.lutils.nat.struct.abstracts.Structure.allocate;

public class GraphicsQueueTransientCommandPool extends CommandPool {

    private final @NotNull VulkanEngine<?> engine;

    public static @NotNull GraphicsQueueTransientCommandPool create(
            @NotNull VulkanEngine<?> engine,
            @NotNull Stack stack,
            @NotNull Instance instance,
            @NotNull Device device
    ) {
        @NotNull VkInstance vkInstance = instance.getVkInstance();
        GraphicsQueueTransientCommandPool commandPool = new GraphicsQueueTransientCommandPool(engine, vkInstance, device);

        VkCommandPoolCreateInfo commandPoolCreateInfo = stack.push(new VkCommandPoolCreateInfo());
        commandPoolCreateInfo.sType.set(VkStructureType.COMMAND_POOL_CREATE_INFO);
        commandPoolCreateInfo.flags.set(VkCommandPoolCreateFlagBits.TRANSIENT);
        commandPoolCreateInfo.queueFamilyIndex.set(device.getGraphicsQueueIndex());

        vkInstance.vkCreateCommandPool(device.getVkDevice(), ref(commandPoolCreateInfo), ref(null), ref(commandPool.vkCommandPool)).check();

        return commandPool;
    }


    public GraphicsQueueTransientCommandPool( @NotNull VulkanEngine<?> engine, @NotNull VkInstance vkInstance, @NotNull Device device) {
        super(vkInstance, device);
        this.engine = engine;
    }

    /**
     * Will record and queue a command buffer using given {@code recordCommandBuffer} consumer. The recording and queuing of the
     * command buffer happens on the {@link VulkanEngine#getRenderThread() renderThread}.
     * @param recordCommandBuffer function to record command buffer. commandBuffer has already begun when this function
     *                            is called and will be automatically ended when this function returns.
     * @return {@link Future} to wait until the work is done.
     */
    public Future<Nothing, VulkanEngine<?>> submitSingleTimeCommand(
            @NotNull Consumer<VkCommandBuffer> recordCommandBuffer
    ) {
        var fut = CompletableFuture.<Nothing, VulkanEngine<?>>create(engine.getAsyncManager(), true);

        engine.getRenderThread().getTaskQueue().queueForExecution(stack -> {
            if(fut.startIfNotCanceled()) return;

            VkCommandBuffer vkCommandBuffer = stack.push(new VkCommandBuffer());
            VkCommandBufferAllocateInfo allocateInfo = stack.push(new VkCommandBufferAllocateInfo());
            VkCommandBufferBeginInfo beginInfo = stack.push(new VkCommandBufferBeginInfo());


            allocateInfo.sType.set(VkStructureType.COMMAND_BUFFER_ALLOCATE_INFO);
            allocateInfo.level.set(VkCommandBufferLevel.PRIMARY);
            allocateInfo.commandPool.set(vkCommandPool);
            allocateInfo.commandBufferCount.set(1);

            vkInstance.vkAllocateCommandBuffers(device.getVkDevice(), ref(allocateInfo), ref(vkCommandBuffer)).check();

            beginInfo.sType.set(VkStructureType.COMMAND_BUFFER_BEGIN_INFO);
            beginInfo.flags.set(VkCommandBufferUsageFlagBits.ONE_TIME_SUBMIT);

            vkInstance.vkBeginCommandBuffer(vkCommandBuffer, ref(beginInfo));

            recordCommandBuffer.accept(vkCommandBuffer);

            vkInstance.vkEndCommandBuffer(vkCommandBuffer);

            VkSubmitInfo submitInfo = stack.push(new VkSubmitInfo());
            submitInfo.sType.set(VkStructureType.SUBMIT_INFO);
            submitInfo.commandBufferCount.set(1);
            submitInfo.pCommandBuffers.set(vkCommandBuffer);

            VkFenceCreateInfo fenceCreateInfo = stack.push(new VkFenceCreateInfo());
            fenceCreateInfo.sType.set(VkStructureType.FENCE_CREATE_INFO);

            VkFence fence = allocate(new VkFence());
            vkInstance.vkCreateFence(device.getVkDevice(), ref(fenceCreateInfo), ref(null), ref(fence)).check();

            vkInstance.vkQueueSubmit(device.getGraphicsQueue(), 1, ref(submitInfo), fence);

            engine.runSupervisedV(() -> {
                vkInstance.vkWaitForFences(device.getVkDevice(), 1, ref(fence), true, Long.MAX_VALUE).check();
                vkInstance.vkDestroyFence(device.getVkDevice(), fence, ref(null));
                fut.complete(Nothing.INSTANCE, engine, null);
            });

            stack.pop(); // fenceCreateInfo
            stack.pop(); // submitInfo
            stack.pop(); // beginInfo
            stack.pop(); // allocateInfo
            stack.pop(); // vkCommandBuffer
        });


        return fut;
    }
}

/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.cvg4j.engine.vk.memory.allocator;

import de.linusdev.cvg4j.engine.exception.EngineException;
import de.linusdev.cvg4j.engine.vk.device.Device;
import de.linusdev.cvg4j.engine.vk.memory.buffer.BufferArrayInput;
import de.linusdev.cvg4j.engine.vk.memory.buffer.BufferOutput;
import de.linusdev.cvg4j.engine.vk.memory.buffer.index.IndexBuffer;
import de.linusdev.cvg4j.engine.vk.memory.buffer.vertex.VertexBuffer;
import de.linusdev.cvg4j.engine.vk.memory.buffer.vertex.VertexElement;
import de.linusdev.cvg4j.nat.vulkan.bitmasks.enums.VkBufferUsageFlagBits;
import de.linusdev.cvg4j.nat.vulkan.bitmasks.enums.VkMemoryPropertyFlagBits;
import de.linusdev.cvg4j.nat.vulkan.enums.VkVertexInputRate;
import de.linusdev.cvg4j.nat.vulkan.handles.VkInstance;
import de.linusdev.llog.LLog;
import de.linusdev.llog.base.LogInstance;
import de.linusdev.lutils.bitfield.IntBitfieldImpl;
import de.linusdev.lutils.nat.memory.Stack;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VulkanMemoryAllocator implements AutoCloseable {

    final static @NotNull LogInstance LOG = LLog.getLogInstance();

    private final @NotNull VkInstance vkInstance;
    private final @NotNull Device device;

    MemoryTypeManager[] typeManagers = new MemoryTypeManager[32];

    public VulkanMemoryAllocator(@NotNull VkInstance vkInstance, @NotNull Device device) {
        this.vkInstance = vkInstance;
        this.device = device;
    }

    public <V extends Structure> VertexBuffer<V> createVertexBuffer(
            @NotNull Stack stack,
            @NotNull String debugName,
            @NotNull Class<?> elementClass,
            @NotNull StructureArray.ElementCreator<V> elementCreator,
            @NotNull List<VertexElement> attributeDescriptors,
            int vertexCount,
            int binding,
            @NotNull VkVertexInputRate vertexInputRate
    ) throws EngineException {

        BufferArrayInput<V> vertexInput = new BufferArrayInput<>(vertexCount, elementClass, elementCreator);

        ArrayInfo info = vertexInput.getBackedArrayInfo();

        VulkanBuffer vulkanBuffer = new VulkanBuffer(vkInstance, device, debugName, info.getRequiredSize());
        vertexInput.setVulkanBuffer(vulkanBuffer);

        VulkanBuffer.create(stack, vkInstance, device, vulkanBuffer,
                new IntBitfieldImpl<>(VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT),
                new IntBitfieldImpl<>(
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT, // staging buffer must be mapped
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT // automatically detect changes
                ));

        add(stack, vulkanBuffer);

        BufferOutput vertexOutput = new BufferOutput(vulkanBuffer, info.getStride());

        return new VertexBuffer<>(vkInstance, vertexInput, vertexOutput, binding, vertexInputRate, attributeDescriptors);
    }

    public <V extends Structure> VertexBuffer<V> createStagedVertexBuffer(
            @NotNull Stack stack,
            @NotNull String debugName,
            @NotNull Class<?> elementClass,
            @NotNull StructureArray.ElementCreator<V> elementCreator,
            @NotNull List<VertexElement> attributeDescriptors,
            int vertexCount,
            int binding,
            @NotNull VkVertexInputRate vertexInputRate
    ) throws EngineException {

        BufferArrayInput<V> vertexInput = new BufferArrayInput<>(vertexCount, elementClass, elementCreator);

        ArrayInfo info = vertexInput.getBackedArrayInfo();

        VulkanBuffer stagingBuffer = new VulkanBuffer(vkInstance, device, debugName, info.getRequiredSize());
        vertexInput.setVulkanBuffer(stagingBuffer);

        VulkanBuffer.create(stack, vkInstance, device, stagingBuffer,
                new IntBitfieldImpl<>(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),
                new IntBitfieldImpl<>(
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT, // staging buffer must be mapped
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT // automatically detect changes
                ));

        add(stack, stagingBuffer);

        VulkanBuffer vertexBuffer = new VulkanBuffer(vkInstance, device, debugName, info.getRequiredSize());
        vertexInput.setVulkanBuffer(stagingBuffer);

        VulkanBuffer.create(stack, vkInstance, device, vertexBuffer,
                new IntBitfieldImpl<>(
                        VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_DST_BIT,
                        VkBufferUsageFlagBits.VK_BUFFER_USAGE_VERTEX_BUFFER_BIT
                ),
                new IntBitfieldImpl<>(
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT // device local
                ));

        add(stack, vertexBuffer);


        BufferOutput vertexOutput = new BufferOutput(vertexBuffer, info.getStride());

        return new VertexBuffer<>(vkInstance, vertexInput, vertexOutput, binding, vertexInputRate, attributeDescriptors);
    }

    public <V extends Structure> IndexBuffer<V> createStagedInstanceBuffer(
            @NotNull Stack stack,
            @NotNull String debugName,
            @NotNull Class<?> elementClass,
            @NotNull StructureArray.ElementCreator<V> elementCreator,
            int instanceCount
    ) throws EngineException {

        BufferArrayInput<V> vertexInput = new BufferArrayInput<>(instanceCount, elementClass, elementCreator);

        ArrayInfo info = vertexInput.getBackedArrayInfo();

        VulkanBuffer stagingBuffer = new VulkanBuffer(vkInstance, device, debugName, info.getRequiredSize());
        vertexInput.setVulkanBuffer(stagingBuffer);

        VulkanBuffer.create(stack, vkInstance, device, stagingBuffer,
                new IntBitfieldImpl<>(VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_SRC_BIT),
                new IntBitfieldImpl<>(
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT, // staging buffer must be mapped
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_HOST_COHERENT_BIT // automatically detect changes
                ));

        add(stack, stagingBuffer);

        VulkanBuffer vertexBuffer = new VulkanBuffer(vkInstance, device, debugName, info.getRequiredSize());
        vertexInput.setVulkanBuffer(stagingBuffer);

        VulkanBuffer.create(stack, vkInstance, device, vertexBuffer,
                new IntBitfieldImpl<>(
                        VkBufferUsageFlagBits.VK_BUFFER_USAGE_TRANSFER_DST_BIT,
                        VkBufferUsageFlagBits.VK_BUFFER_USAGE_INDEX_BUFFER_BIT
                ),
                new IntBitfieldImpl<>(
                        VkMemoryPropertyFlagBits.VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT // device local
                ));

        add(stack, vertexBuffer);


        BufferOutput vertexOutput = new BufferOutput(vertexBuffer, info.getStride());

        return new IndexBuffer<>(vkInstance, vertexInput, vertexOutput);
    }

    public void allocate(@NotNull Stack stack) {
        for (MemoryTypeManager typeManager : typeManagers) {
            if(typeManager != null) typeManager.allocate(stack, vkInstance, device);
        }
    }

    private void add(@NotNull Stack stack, @NotNull VulkanBuffer buffer) {
        if(typeManagers[buffer.memoryTypeIndex] == null)
            typeManagers[buffer.memoryTypeIndex] = new MemoryTypeManager(stack, vkInstance, device, buffer.memoryTypeIndex);

        typeManagers[buffer.memoryTypeIndex].addBuffer(buffer);
    }

    @Override
    public void close() {
        for (MemoryTypeManager typeManager : typeManagers) {
            if(typeManager != null) typeManager.close();
        }
    }
}

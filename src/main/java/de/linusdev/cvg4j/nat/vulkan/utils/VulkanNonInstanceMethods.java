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

package de.linusdev.cvg4j.nat.vulkan.utils;

import de.linusdev.cvg4j.nat.NativeFunctions;
import de.linusdev.cvg4j.nat.NativeUtils;
import de.linusdev.cvg4j.nat.glfw3.GLFW;
import de.linusdev.cvg4j.nat.vulkan.ReturnedVkResult;
import de.linusdev.cvg4j.nat.vulkan.VulkanApiVersion;
import de.linusdev.cvg4j.nat.vulkan.handles.VkInstance;
import de.linusdev.cvg4j.nat.vulkan.structs.VkAllocationCallbacks;
import de.linusdev.cvg4j.nat.vulkan.structs.VkExtensionProperties;
import de.linusdev.cvg4j.nat.vulkan.structs.VkInstanceCreateInfo;
import de.linusdev.cvg4j.nat.vulkan.structs.VkLayerProperties;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.pointer.Pointer64;
import de.linusdev.lutils.nat.pointer.TypedPointer64;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public interface VulkanNonInstanceMethods {

    static ReturnedVkResult vkCreateInstance(
            VkInstanceCreateInfo vkInstanceCreateInfo,
            @Nullable VkAllocationCallbacks pAllocator,
            VkInstance pInstance
    ) {
        long pointer = GLFW.glfwGetInstanceProcAddress(NativeUtils.getNullPointer(), "vkCreateInstance");

        int res = NativeFunctions.callNativeIFunctionPPP(
                pointer,
                vkInstanceCreateInfo.getPointer(),
                pAllocator == null ? NativeUtils.getNullPointer() : pAllocator.getPointer(),
                pInstance.getPointer()
        );

        return new ReturnedVkResult(res);
    }

    static @NotNull StructureArray<VkExtensionProperties> vkEnumerateInstanceExtensionProperties(
            @NotNull TypedPointer64<NullTerminatedUTF8String> pLayerName,
            @NotNull BBUInt1 count,
            @NotNull Function<Integer, StructureArray<VkExtensionProperties>> arraySupplier
    ) {
        long pointer = GLFW.glfwGetInstanceProcAddress(NativeUtils.getNullPointer(), "vkEnumerateInstanceExtensionProperties");
        new ReturnedVkResult(NativeFunctions.callNativeIFunctionPPP(pointer, pLayerName.get(), count.getPointer(), Pointer64.NULL_POINTER)).check();
        var array = arraySupplier.apply(count.get());
        new ReturnedVkResult(NativeFunctions.callNativeIFunctionPPP(pointer, pLayerName.get(), count.getPointer(), array.getPointer())).check();
        return array;
    }

    static @NotNull StructureArray<VkLayerProperties> vkEnumerateInstanceLayerProperties(
            @NotNull BBUInt1 count,
            @NotNull Function<Integer, StructureArray<VkLayerProperties>> arraySupplier
    ) {
        long pointer = GLFW.glfwGetInstanceProcAddress(NativeUtils.getNullPointer(), "vkEnumerateInstanceLayerProperties");
        new ReturnedVkResult(NativeFunctions.callNativeIFunctionPP(pointer, count.getPointer(), Pointer64.NULL_POINTER)).check();
        var array = arraySupplier.apply(count.get());
        new ReturnedVkResult(NativeFunctions.callNativeIFunctionPP(pointer, count.getPointer(), array.getPointer())).check();
        return array;
    }

    static @NotNull BBUInt1 vkEnumerateInstanceVersion(
            @NotNull BBUInt1 version
    ) {
        long pointer = GLFW.glfwGetInstanceProcAddress(NativeUtils.getNullPointer(), "vkEnumerateInstanceVersion");

        if(pointer == Pointer64.NULL_POINTER) {
            version.set(VulkanApiVersion.V_1_0_0.getAsInt());
        }

        new ReturnedVkResult(NativeFunctions.callNativeIFunctionP(pointer, version.getPointer())).check();

        return version;
    }
}

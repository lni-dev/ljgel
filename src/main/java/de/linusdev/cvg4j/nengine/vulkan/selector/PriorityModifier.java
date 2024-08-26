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

package de.linusdev.cvg4j.nengine.vulkan.selector;

import de.linusdev.cvg4j.nat.vulkan.structs.VkExtensionProperties;
import de.linusdev.cvg4j.nat.vulkan.structs.VkPhysicalDeviceProperties;
import de.linusdev.cvg4j.nat.vulkan.structs.VkSurfaceCapabilitiesKHR;
import de.linusdev.cvg4j.nat.vulkan.structs.VkSurfaceFormatKHR;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface PriorityModifier {


    @NotNull PriorityModifierType type();

    int modifier();

    boolean test(@NotNull GpuInfo info);

    default int apply(int current, @NotNull GpuInfo info) {
        if(!test(info))
            return current;

        return type().apply(current, modifier());
    }

    record GpuInfo(
            @NotNull VkPhysicalDeviceProperties props,
            int extensionCount,
            @NotNull StructureArray<VkExtensionProperties> extensions,
            @NotNull VkSurfaceCapabilitiesKHR surfacesCaps,
            int surfaceFormatCount,
            @NotNull StructureArray<VkSurfaceFormatKHR> surfaceFormats
    ) { }

    record Impl(
            @NotNull PriorityModifierType type,
            int modifier,
            @NotNull Predicate<GpuInfo> tester
    ) implements PriorityModifier {

        @Override
        public boolean test(@NotNull GpuInfo info) {
            return tester.test(info);
        }

    }

}

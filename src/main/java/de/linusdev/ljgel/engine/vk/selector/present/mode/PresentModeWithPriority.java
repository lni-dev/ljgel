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

package de.linusdev.ljgel.engine.vk.selector.present.mode;

import de.linusdev.ljgel.engine.vk.selector.priority.Priority;
import de.linusdev.ljgel.nat.vulkan.enums.VkPresentModeKHR;
import de.linusdev.lutils.nat.enums.NativeEnumValue32;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

record PresentModeWithPriority(@Nullable VkPresentModeKHR presentMode, @NotNull Priority priority) {
    public boolean allows(NativeEnumValue32<VkPresentModeKHR> availableMode) {
        return (presentMode == null || presentMode.getValue() == availableMode.get());
    }
}
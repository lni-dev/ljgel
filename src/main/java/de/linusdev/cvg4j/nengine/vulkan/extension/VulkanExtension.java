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

package de.linusdev.cvg4j.nengine.vulkan.extension;

import org.jetbrains.annotations.NotNull;

public interface VulkanExtension {

    static @NotNull VulkanExtension of(@NotNull String name, int version) {
        return new VulkanExtension() {
            @Override
            public @NotNull String extensionName() {
                return name;
            }

            @Override
            public int version() {
                return version;
            }
        };
    }

    static @NotNull VulkanExtension of(@NotNull String name) {
        return of(name, 0);
    }

    static boolean isSufficient(@NotNull VulkanExtension required, @NotNull VulkanExtension available) {
        if(!required.extensionName().equals(available.extensionName()))
            return false;

        return available.version() >= required.version();
    }

    @NotNull String extensionName();

    int version();

}

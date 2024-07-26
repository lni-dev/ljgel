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

package de.linusdev.cvg4j.build.vkregistry.types.abstracts;

import de.linusdev.cvg4j.build.vkregistry.RegistryLoader;
import de.linusdev.cvg4j.build.vkregistry.types.CTypes;
import de.linusdev.lutils.codegen.SourceGenerator;
import de.linusdev.lutils.codegen.java.JavaClass;
import org.jetbrains.annotations.NotNull;

public interface Type {

    @NotNull String getName();

    @NotNull TypeType getType();

    /**
     * @return {@link CTypes} this type represents
     * @throws UnsupportedOperationException if {@link #getType()} is not
     * {@link TypeType#BASIC BASIC} or {@link TypeType#ALIAS_OF_BASIC ALIAS_OF_BASIC}
     */
    default @NotNull CTypes getAsBaseType() {
        throw new UnsupportedOperationException();
    }

    void generate(
            @NotNull RegistryLoader registry,
            @NotNull SourceGenerator generator
    );

    @NotNull JavaClass getJavaClass(
            @NotNull RegistryLoader registry,
            @NotNull SourceGenerator generator
    );

}

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

package de.linusdev.clgl.engine.structs;

import de.linusdev.clgl.api.structs.*;
import de.linusdev.clgl.api.types.bytebuffer.BBInt1;
import org.jetbrains.annotations.NotNull;

public class WorldStruct extends ComplexStructure {

    public static final StructureInfo INFO = new StructureInfo();

    @StructValue public final @NotNull BBInt1 int1 = new BBInt1(false);
    @StructValue public final @NotNull BBInt1 int2 = new BBInt1(false);
    @StructValue public final @NotNull CameraStruct cam = new CameraStruct(false);
    @StructValue @FixedElementSize(value = 5, elementType = PlayerStruct.class)
    public final @NotNull StructureArray<PlayerStruct> players = new StructureArray<>(
            false, true, PlayerStruct.INFO, 5, () -> new PlayerStruct(false));
    @StructValue public final @NotNull PlayerStruct playerA = new PlayerStruct(false);
    @StructValue public final @NotNull PlayerStruct playerB = new PlayerStruct(false);


    public WorldStruct(boolean allocateBuffer) {
        super(true);
        init(allocateBuffer);
    }

    @Override
    protected @NotNull StructureInfo getInfo() {
        return INFO;
    }
}

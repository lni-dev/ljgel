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

package de.linusdev.clgl.api.types.bytebuffer;


import de.linusdev.clgl.api.structs.StructureInfo;
import de.linusdev.clgl.api.types.Float1;
import org.jetbrains.annotations.NotNull;

public class BBFloat1 extends BBFloatN implements Float1 {

    public static StructureInfo INFO = new StructureInfo(4, false, 0, 4, 0);

    public BBFloat1(boolean allocateBuffer) {
        super(1, allocateBuffer);
    }

    @Override
    protected @NotNull StructureInfo getInfo() {
        return INFO;
    }
}

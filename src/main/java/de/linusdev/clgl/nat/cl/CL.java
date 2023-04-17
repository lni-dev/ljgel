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

package de.linusdev.clgl.nat.cl;

import de.linusdev.clgl.api.structs.Structure;
import de.linusdev.clgl.api.types.bytebuffer.BBInt1;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@SuppressWarnings("unused")
public class CL {

    public static int clGetPlatformIDs(int size, @Nullable Structure array, @Nullable BBInt1 platformCount) {

        return _clGetPlatformIDs(size,
                array == null ? null : array.getByteBuf(),
                platformCount == null ? null : platformCount.getByteBuf());
    }
    private static native int _clGetPlatformIDs(int num_entries, ByteBuffer p_platforms, ByteBuffer p_num_platforms);

}

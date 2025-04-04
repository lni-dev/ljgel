/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.ljgel.nat.cl.objects;

import de.linusdev.ljgel.nat.cl.CL;
import org.jetbrains.annotations.NotNull;

public class Device {

    private final long pointer;

    Device(long pointer) {
        this.pointer = pointer;
    }

    public @NotNull String getName() {
        return CL.getDeviceInfoString(pointer, CL.CLDeviceInfo.CL_DEVICE_NAME);
    }

    public long getPointer() {
        return pointer;
    }
}

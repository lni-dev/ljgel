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

package de.linusdev.clgl.nat.cl.structs;

import de.linusdev.clgl.api.types.bytebuffer.BBInt2;
import de.linusdev.clgl.nat.cl.CL;
import org.jetbrains.annotations.NotNull;

public class CLImageFormat extends BBInt2 {
    public CLImageFormat(@NotNull CL.CLChannelOrder order, @NotNull CL.CLChannelType type) {
        super(true);
        x(order.getValue());
        y(type.getValue());
    }

}

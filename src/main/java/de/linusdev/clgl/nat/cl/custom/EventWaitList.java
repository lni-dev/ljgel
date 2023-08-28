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

package de.linusdev.clgl.nat.cl.custom;


import de.linusdev.clgl.nat.cl.objects.Event;
import de.linusdev.lutils.struct.array.StructureArray;

public class EventWaitList extends StructureArray<Event> {

    public EventWaitList(int size) {
        super(true, false, Event.class, size, () -> new Event(false));
    }
}

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

package de.linusdev.cvg4j.engine;

import de.linusdev.cvg4j.engine.structs.WorldStruct;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.generator.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class EngineTest {

    @Test
    public void test() throws InterruptedException, IOException {
        Engine.StaticSetup.setup();

        TestGame yourGame = new TestGame();
        Engine<TestGame> engine = Engine.getInstance(yourGame);

        engine.loadScene(new TestScene(engine));

        System.out.println(Structure.generateStructCode(Language.OPEN_CL, WorldStruct.class));

        engine.getUIThread().getWindowClosedFuture().get();
    }

}
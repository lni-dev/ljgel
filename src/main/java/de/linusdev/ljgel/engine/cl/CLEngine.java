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

package de.linusdev.ljgel.engine.cl;

import de.linusdev.ljgel.api.misc.interfaces.TRunnable;
import de.linusdev.ljgel.engine.Engine;
import de.linusdev.ljgel.engine.cl.window.CLGLWindow;
import de.linusdev.ljgel.engine.window.input.InputManagerImpl;
import de.linusdev.ljgel.engine.window.input.InputManger;
import de.linusdev.ljgel.nat.Load;
import de.linusdev.ljgel.nat.NativeUtils;
import de.linusdev.ljgel.nat.abi.ABISelector;
import de.linusdev.ljgel.nat.cl.objects.Context;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.interfaces.AdvTRunnable;
import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface CLEngine<G extends CLGame> extends Engine<G> {

    class StaticSetup {

        static {
            setup();
        }

        private static boolean staticSetupDone = false;

        private static synchronized void checkSetup() {
            if(!staticSetupDone)
                throw new IllegalStateException("CLEngine.StaticSetup.setup() must be called as first line in main.");
        }

        public static synchronized void setup() {
            if(StaticSetup.staticSetupDone)
                return;
            ABISelector.retrieveAndSetDefaultABI();
            Load.init();
            BufferUtils.setByteBufferFromPointerMethod(NativeUtils::getBufferFromPointer);
            StaticSetup.staticSetupDone = true;
        }
    }

    /**
     * Creates a {@link CLEngine} instance. This will also create the window.
     * Call {@link CLEngine#loadScene(CLScene)} to load your {@link CLScene}.
     * @param game your {@link CLGame}
     * @return {@link CLEngine}
     * @param <T> your {@link CLGame}
     */
    static <T extends CLGame> @NotNull CLEngine<T> getInstance(@NotNull T game) {
        StaticSetup.checkSetup();
        return new CLEngineImpl<>(game);
    }

    @NotNull Future<Nothing, CLScene<G>> loadScene(@NotNull CLScene<G> scene);

    @ApiStatus.Internal
    @NotNull InputManagerImpl createInputManagerForScene(@NotNull CLScene<G> scene);

    @NotNull G getGame();

    @NotNull CLGLWindow getWindow();

    /**
     * {@link InputManger} to be used across all scenes.
     * @return {@link InputManger}
     */
    @NotNull InputManger getGlobalInputManager();

    @NotNull Context getClContext();

    @NotNull AsyncManager getAsyncManager();

    @NotNull
    UIThread<G> getUIThread();

    @Override
    <R> @NotNull Future<R, Nothing> runSupervised(@NotNull AdvTRunnable<R,?> runnable);

    void runSupervised(@NotNull TRunnable runnable);

}

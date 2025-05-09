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

package de.linusdev.ljgel.api.misc.annos;

import java.lang.annotation.*;

/**
 * This Function may only be called from the ui thread.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface CallOnlyFromUIThread {

    /**
     * The name of the ui thread. Similar to an id.
     */
    String value();

    /**
     * Whether this function creates the ui thread.
     */
    boolean creates() default false;

    /**
     * Only {@code true} in combination with {@link #creates()}. If this is {@code true}, the function
     * will claim and block the current thread (making it the ui thread).
     */
    boolean claims() default false;

}

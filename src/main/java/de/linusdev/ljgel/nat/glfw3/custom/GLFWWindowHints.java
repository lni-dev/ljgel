/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.ljgel.nat.glfw3.custom;

import de.linusdev.ljgel.nat.glfw3.GLFWValues;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import static de.linusdev.ljgel.nat.glfw3.GLFW.glfwWindowHint;
import static de.linusdev.ljgel.nat.glfw3.GLFWValues.WindowHints.*;
import static de.linusdev.ljgel.nat.glfw3.GLFWValues.convertBoolean;

/**
 * @see <a href="https://www.glfw.org/docs/latest/window_guide.html#window_hints">glfw window hints</a>
 */
@SuppressWarnings("unused")
public class GLFWWindowHints {

    /**
     * Number of samples (antialiasing)
     */
    public @Nullable Integer samples = null;

    /**
     * OpenGl Context Major Version. This is the first value of the OpenGl Version.
     * Example: <b>4</b>.6
     */
    public @Nullable Integer contextVersionMajor = null;

    /**
     * OpenGl Context Minor Version. This is the second value of the OpenGl Version.
     * Example: 4.<b>6</b>
     */
    public @Nullable Integer contextVersionMinor = null;

    /**
     * Sets {@link #contextVersionMajor} and {@link #contextVersionMinor}.
     */
    @SuppressWarnings("UnusedReturnValue")
    public GLFWWindowHints setContextVersion(int major, int minor) {
        contextVersionMajor = major;
        contextVersionMinor = minor;
        return this;
    }

    /**
     * specifies whether the OpenGL context should be forward-compatible, i.e. one where
     * all functionality deprecated in the requested version of OpenGL is removed.
     * This must only be used if the requested OpenGL version is 3.0 or above. If OpenGL
     * ES is requested, this hint is ignored.
     */
    public @Nullable Boolean openglForwardCompat = null;

    /**
     * Specifies which OpenGL profile to create the context for.
     * Possible values are one of {@link GLFWValues.OpenGlProfiles#GLFW_OPENGL_CORE_PROFILE GLFW_OPENGL_CORE_PROFILE}
     * or {@link GLFWValues.OpenGlProfiles#GLFW_OPENGL_COMPAT_PROFILE GLFW_OPENGL_COMPAT_PROFILE},
     * or {@link GLFWValues.OpenGlProfiles#GLFW_OPENGL_ANY_PROFILE GLFW_OPENGL_ANY_PROFILE}
     * to not request a specific profile. If requesting an OpenGL version below 3.2,
     * {@link GLFWValues.OpenGlProfiles#GLFW_OPENGL_ANY_PROFILE GLFW_OPENGL_ANY_PROFILE} must be used. If OpenGL ES is requested, this hint is ignored.
     */
    public @Nullable GLFWValues.OpenGlProfiles openglProfile = null;

    /**
     * specifies which client API to create the context for.
     * Possible values are GLFW_OPENGL_API, GLFW_OPENGL_ES_API and GLFW_NO_API. This is a hard constraint.
     */
    public @Nullable GLFWValues.ClientApis clientApi = null;

    /**
     * specifies whether the window framebuffer will be transparent. If enabled and supported by the system,
     * the window framebuffer alpha channel will be used to combine the framebuffer with the background. This
     * does not affect window decorations. Possible values are GLFW_TRUE and GLFW_FALSE.
     */
    public @Nullable Boolean transparentFrameBuffer = null;

    /**
     * specifies whether the windowed mode window will be initially visible. Possible values are
     * GLFW_TRUE and GLFW_FALSE. This hint is ignored for full screen windows.
     */
    public @Nullable Boolean visible = null;

    /**
     *  specifies whether the windowed mode window will be resizable by the user. The window will still be resizable
     *  using the glfwSetWindowSize function. Possible values are GLFW_TRUE and GLFW_FALSE. This hint is ignored for
     *  full screen and undecorated windows.
     */
    public @Nullable Boolean resizable = null;

    /**
     * specifies whether the windowed mode window will have window decorations such as a border, a close widget, etc.
     * An undecorated window will not be resizable by the user but will still allow the user to generate close events
     * on some platforms. Possible values are GLFW_TRUE and GLFW_FALSE. This hint is ignored for full screen windows.
     */
    public @Nullable Boolean decorated = null;

    /**
     * specifies whether the windowed mode window will be maximized when created. Possible values are GLFW_TRUE
     * and GLFW_FALSE. This hint is ignored for full screen windows.
     */
    public @Nullable Boolean maximized = null;

    public @ApiStatus.Internal void adjustWindowHints() {
        if(samples != null) glfwWindowHint(GLFW_SAMPLES, samples);
        if(contextVersionMajor != null) glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, contextVersionMajor);
        if(contextVersionMinor != null) glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, contextVersionMinor);
        if(openglForwardCompat != null) glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, convertBoolean(openglForwardCompat));
        if(openglProfile != null) glfwWindowHint(GLFW_OPENGL_PROFILE, openglProfile.value);
        if(clientApi != null) glfwWindowHint(GLFW_CLIENT_API, clientApi.value);
        if(transparentFrameBuffer != null) glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, convertBoolean(transparentFrameBuffer));
        if(visible != null) glfwWindowHint(GLFW_VISIBLE, convertBoolean(visible));
        if(resizable != null) glfwWindowHint(GLFW_RESIZABLE, convertBoolean(resizable));
        if(decorated != null) glfwWindowHint(GLFW_DECORATED, convertBoolean(decorated));
        if(maximized != null) glfwWindowHint(GLFW_MAXIMIZED, convertBoolean(maximized));

    }
}

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

package de.linusdev.ljgel.nat.glad.objects;

import de.linusdev.ljgel.nat.glad.custom.Binding;
import de.linusdev.ljgel.nat.glad.custom.BindingID;
import de.linusdev.ljgel.nat.glad.custom.GLNamedObject;
import de.linusdev.ljgel.nat.glad.custom.GlSizedObject;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import static de.linusdev.ljgel.nat.glad.GLConstants.*;
import static de.linusdev.ljgel.nat.glad.Glad.*;

@SuppressWarnings("unused")
public class GLFrameBuffer extends GLNamedObject<GLFrameBuffer> {

    public static final @NotNull GLFrameBuffer DEFAULT_FRAME_BUFFER = new GLFrameBuffer(0);

    public GLFrameBuffer() {
        this.name = glCreateFramebuffer();
    }

    protected GLFrameBuffer(int name) {
        this.name = name;
    }

    public static @NotNull GLFrameBuffer getDefault() {
        return DEFAULT_FRAME_BUFFER;
    }

    @Override
    public void reCreate() {
        if(!isClosed())
            glDeleteFramebuffer(name);
        this.name = glCreateFramebuffer();
        callReCreationListener();
    }

    public @NotNull Binding<GLFrameBuffer, GLRenderBuffer> addRenderBuffer(
            @NotNull GLRenderBuffer buffer,
            @MagicConstant(intValues = {
                    GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2, GL_COLOR_ATTACHMENT3,
                    GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5, GL_COLOR_ATTACHMENT6, GL_COLOR_ATTACHMENT7,
                    GL_COLOR_ATTACHMENT8, GL_COLOR_ATTACHMENT9, GL_COLOR_ATTACHMENT10,  GL_DEPTH_ATTACHMENT,
                    GL_STENCIL_ATTACHMENT,  GL_DEPTH_STENCIL_ATTACHMENT
            })
            int attachment,
            @MagicConstant(intValues = GL_RENDERBUFFER)
            int target
    ) {
        if(isClosed())
            throw new IllegalStateException("Already closed");

        checkAndDeleteExistingBinding(attachment);

        glNamedFramebufferRenderbuffer(
                name,
                attachment,
                target,
                buffer.getName()
        );

        return new Binding<>(this, buffer, attachment) {
            @Override
            protected void _remove(
                    @NotNull GLFrameBuffer parent,
                    @NotNull GLRenderBuffer component,
                    @NotNull BindingID idToRemove
            ) {
                glNamedFramebufferRenderbuffer(
                        parent.getName(),
                        attachment,
                        target,
                        0
                );

                bindings.remove(idToRemove);
            }

            @Override
            protected void onObjectRecreated(
                    @NotNull GLFrameBuffer parent,
                    @NotNull GLRenderBuffer component
            ) {
                glNamedFramebufferRenderbuffer(
                        parent.getName(),
                        attachment,
                        target,
                        component.getName()
                );
            }
        };
    }

    public void setDrawBuffer(
            @MagicConstant(intValues = {
                    GL_NONE, GL_FRONT, GL_LEFT,  GL_FRONT_LEFT, GL_FRONT_RIGHT, GL_RIGHT,GL_BACK,
                    GL_BACK_LEFT, GL_FRONT_AND_BACK, GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2,
                    GL_COLOR_ATTACHMENT3, GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5, GL_COLOR_ATTACHMENT6,
                    GL_COLOR_ATTACHMENT7, GL_COLOR_ATTACHMENT8, GL_COLOR_ATTACHMENT9, GL_COLOR_ATTACHMENT10
            })
            int buffer
    ) {
        glNamedFramebufferDrawBuffer(name, buffer);
    }

    public void setReadBuffer(
            @MagicConstant(intValues = {
                    GL_FRONT, GL_LEFT,  GL_FRONT_LEFT, GL_FRONT_RIGHT, GL_RIGHT,GL_BACK,
                    GL_BACK_LEFT, GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2,
                    GL_COLOR_ATTACHMENT3, GL_COLOR_ATTACHMENT4, GL_COLOR_ATTACHMENT5, GL_COLOR_ATTACHMENT6,
                    GL_COLOR_ATTACHMENT7, GL_COLOR_ATTACHMENT8, GL_COLOR_ATTACHMENT9, GL_COLOR_ATTACHMENT10
            })
            int buffer
    ) {
        glNamedFramebufferReadBuffer(name, buffer);
    }

    public void blitInto(
            @NotNull GLFrameBuffer destination,
            int srcX0,
            int srcY0,
            int srcX1,
            int srcY1,
            int dstX0,
            int dstY0,
            int dstX1,
            int dstY1,
            @MagicConstant(flags = {GL_COLOR_BUFFER_BIT, GL_DEPTH_BUFFER_BIT, GL_STENCIL_BUFFER_BIT})
            int mask,
            @MagicConstant(intValues = {GL_NEAREST, GL_LINEAR})
            int filter
    ) {
        glBlitNamedFramebuffer(
                name, destination.getName(),
                srcX0, srcY0,
                srcX1, srcY1,
                dstX0, dstY0,
                dstX1, dstY1,
                mask, filter
        );
    }

    public void blitInto(
            @NotNull GLFrameBuffer destination,
            @NotNull GlSizedObject sourceSize,
            @NotNull GlSizedObject destinationSize,
            @MagicConstant(flags = {GL_COLOR_BUFFER_BIT, GL_DEPTH_BUFFER_BIT, GL_STENCIL_BUFFER_BIT})
            int mask,
            @MagicConstant(intValues = {GL_NEAREST, GL_LINEAR})
            int filter
    ) {
        blitInto(destination,
                0, 0, sourceSize.getWidth(), sourceSize.getHeight(),
                0, 0, destinationSize.getWidth(), destinationSize.getHeight(),
                mask, filter
        );
    }

    @Override
    public boolean isReCreatable() {
        return true;
    }

    @Override
    public void close() {
        glDeleteFramebuffer(name);
        name = 0;
        closed = true;
    }
}

/*
 * MIT License
 *
 * Copyright (c) 2018-2020 Yoann CAPLAIN
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.pikei.canon.framework.api.command;

import org.blackdread.camerabinding.jna.EdsdkLibrary.EdsCameraRef;
import io.pikei.canon.framework.api.command.AbstractCanonCommand;
import io.pikei.canon.framework.api.constant.EdsCameraStatusCommand;
import io.pikei.canon.framework.api.helper.factory.CanonFactory;

/**
 * <p>Created on 2018/10/02.<p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.0
 */
public class StatusCommand extends AbstractCanonCommand<Void> {

    private final EdsCameraStatusCommand edsCameraStatusCommand;

    public StatusCommand(final EdsCameraStatusCommand edsCameraStatusCommand) {
        this.edsCameraStatusCommand = edsCameraStatusCommand;
    }

    public StatusCommand(final StatusCommand toCopy) {
        super(toCopy);
        this.edsCameraStatusCommand = toCopy.edsCameraStatusCommand;
    }

    @Override
    protected Void runInternal() {
        CanonFactory.cameraLogic().sendStatusCommand((EdsCameraRef) getTargetRefInternal(), edsCameraStatusCommand);
        return null;
    }
}

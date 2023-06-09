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

import io.pikei.canon.framework.api.CallableCommand;
import io.pikei.canon.framework.api.command.AbstractCanonCommand;

/**
 * Allow to run provide generic commands as not all are defined in the framework, this can help to "create" new commands without having to define a new class.
 * <p>Created on 2018/10/02.<p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.0
 */
public class GenericCommand<R> extends AbstractCanonCommand<R> {

    private final CallableCommand<R> callable;

    public GenericCommand(final CallableCommand<R> callable) {
        this.callable = callable;
    }

    /**
     * Extreme care with variable passed in the callable as it will reuse it
     *
     * @param toCopy command to copy
     */
    public GenericCommand(final GenericCommand<R> toCopy) {
        super(toCopy);
        this.callable = toCopy.callable;
    }

    @Override
    protected R runInternal() throws InterruptedException {
        return callable.call();
    }
}

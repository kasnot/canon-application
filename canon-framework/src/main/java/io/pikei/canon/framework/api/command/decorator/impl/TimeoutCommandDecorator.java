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
package io.pikei.canon.framework.api.command.decorator.impl;

import io.pikei.canon.framework.api.command.CanonCommand;
import io.pikei.canon.framework.api.command.decorator.impl.AbstractDecoratorCommand;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>Created on 2018/10/10.<p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.0
 */
public class TimeoutCommandDecorator<R> extends AbstractDecoratorCommand<R> {

    private Duration timeout;

    public TimeoutCommandDecorator(final CanonCommand<R> delegate, final Duration timeout) {
        super(delegate);
        this.timeout = Objects.requireNonNull(timeout);
    }

    public TimeoutCommandDecorator(final FakeClassArgument fake, final TimeoutCommandDecorator<R> toCopy) {
        super(fake, toCopy);
        this.timeout = toCopy.timeout;
    }

    @Override
    public Optional<Duration> getTimeout() {
        return Optional.of(timeout);
    }

    @Override
    public CanonCommand<R> setTimeout(final Duration timeout) {
        this.timeout = Objects.requireNonNull(timeout);
        return this;
    }
}

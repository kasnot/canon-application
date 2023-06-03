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
package io.pikei.canon.framework.api.helper.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.pikei.canon.framework.api.command.CanonCommand;
import io.pikei.canon.framework.api.command.FetchEventCommand;
import io.pikei.canon.framework.api.helper.logic.CommandDispatcher;
import org.blackdread.camerabinding.jna.EdsdkLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Custom Command Dispatcher.
 */
@ThreadSafe
public final class PikeiCommandDispatcher implements CommandDispatcher {

    protected static final Logger log = LoggerFactory.getLogger(PikeiCommandDispatcher.class);

    // we delay as much as possible the instance
    private volatile static PikeiCommandDispatcher instance;
    private volatile static AtomicInteger loopCounter;


    public static CommandDispatcher getInstance() {
        loopCounter = new AtomicInteger(0);
        if (instance == null) {
            synchronized (PikeiCommandDispatcher.class) {
                if (instance == null) {
                    instance = new PikeiCommandDispatcher();
                }
            }
        }
        return instance;
    }

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("cmd-dispatcher-%d")
        .setDaemon(true)
        .build();

    private final BlockingQueue<CanonCommand> commandQueue = new LinkedBlockingQueue<>();

    private final Semaphore waitForCommandSemaphore = new Semaphore(0);

    private volatile Thread commandDispatcherThread;

    /**
     * Dispatcher current command running or null
     */
    private final AtomicReference<CanonCommand> currentCommand = new AtomicReference<>();

    private void commandDispatcher() {
        try {
            while (true) {
                try {
                    final CanonCommand cmd = commandQueue.take();
                    if( !(cmd instanceof FetchEventCommand) ) log.info("Command Type: {}, Command Queue: {}", cmd.getClass(), commandQueue.size() );
                    try {
                        currentCommand.set(cmd);
                        waitForCommandSemaphore.release();
                        cmd.run();
                    } finally {
                        currentCommand.set(null);
                    }
                } catch (Exception e) {
                    log.warn("Ignored exception in command dispatcher runner", e);
                }
            }
        } finally {
            log.warn("Command dispatcher thread ended");
        }
    }

    @Override
    public void scheduleCommand(final CanonCommand<?> command) {
        commandQueue.add(command);
        startDispatcher();
    }

    @Override
    public void scheduleCommand(final EdsdkLibrary.EdsCameraRef owner, final CanonCommand<?> command) {
        scheduleCommand(command);
    }

    @Override
    public boolean isDispatcherThread() {
        return this.commandDispatcherThread == Thread.currentThread();
    }

    /**
     * Start dispatcher only not already started
     */
    private void startDispatcher() {
        if (commandDispatcherThread != null) {
            return;
        }
        synchronized (threadFactory) {
            if (commandDispatcherThread == null) {
                commandDispatcherThread = threadFactory.newThread(this::commandDispatcher);
                commandDispatcherThread.start();
            }
        }
    }

    private PikeiCommandDispatcher() {
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link BackgroundInitializer}.
 */
class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * A helper class for testing that tracks invocations.
     */
    private static class CloseableCounter {
        final AtomicInteger initializeCalls = new AtomicInteger();
        final AtomicBoolean closed = new AtomicBoolean();

        void close() {
            closed.set(true);
        }

        int getInitializeCalls() {
            return initializeCalls.get();
        }

        CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * A concrete test implementation of BackgroundInitializer. It allows tests to
     * control the execution of the background task via a latch and to simulate
     * different outcomes like exceptions or delays.
     */
    private static class ControllableBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        /** An exception to be thrown by initialize(), null means no exception. */
        Exception ex;

        /** A latch to pause the initialize() method until released. */
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        /** The state object for tracking calls. */
        final CloseableCounter counter = new CloseableCounter();

        ControllableBackgroundInitializer() {
        }

        ControllableBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        /**
         * Pauses the background task until releaseLatch() is called.
         */
        void enableLatch() {
            waitForLatch = true;
        }

        CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * The main background task. Records the invocation, optionally throws an
         * exception, and can be paused with a latch.
         */
        @Override
        protected CloseableCounter initialize() throws Exception {
            if (ex != null) {
                throw ex;
            }
            if (waitForLatch) {
                latch.await();
            }
            return counter.increment();
        }

        /**
         * Allows the background task to complete if it was paused.
         */
        void releaseLatch() {
            latch.countDown();
        }
    }

    /**
     * Verifies that a successful initialization correctly updates the call count.
     *
     * @param init the initializer to test
     */
    private void checkInitialize(final ControllableBackgroundInitializer init) throws ConcurrentException {
        // then
        final CloseableCounter result = init.get();
        assertEquals(1, result.getInitializeCalls(), "Wrong number of invocations");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), "Wrong number of invocations on internal counter");
        assertNotNull(init.getFuture(), "Future should be available after start");
    }

    @Test
    void builder_shouldCorrectlyBuildUnconfiguredInstance() {
        // when
        final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
                .setCloser(null)
                .setExternalExecutor(null)
                .setInitializer(null)
                .get();

        // then
        assertNull(backgroundInitializer.getExternalExecutor());
        assertFalse(backgroundInitializer.isInitialized());
        assertFalse(backgroundInitializer.isStarted());
        assertThrows(IllegalStateException.class, backgroundInitializer::getFuture, "getFuture() should fail before start()");
    }

    @Test
    void builder_shouldCreateInstanceThatThrowsExceptionFromInitializer() {
        // given
        final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
                .setInitializer(() -> {
                    throw new IllegalStateException("test");
                })
                .get();

        // when
        backgroundInitializer.start();

        // then
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, backgroundInitializer::get);
        assertEquals("test", thrown.getMessage());
    }

    @Test
    void getActiveExecutor_shouldReturnNull_beforeStart() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when & then
        assertNull(initializer.getActiveExecutor(), "Active executor should be null before start()");
    }

    @Test
    void getActiveExecutor_shouldReturnExternalExecutor_whenOneIsProvided() throws InterruptedException, ConcurrentException {
        // given
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer(exec);

            // when
            initializer.start();

            // then
            assertSame(exec, initializer.getActiveExecutor(), "Active executor should be the external one");
            checkInitialize(initializer);
        } finally {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void get_shouldThrowIllegalStateException_whenNotStarted() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when & then
        assertThrows(IllegalStateException.class, initializer::get, "get() should throw exception before start()");
    }

    @Test
    void get_shouldThrowConcurrentException_whenInitializerThrowsCheckedException() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();
        final Exception checkedException = new Exception("Test checked exception");
        initializer.ex = checkedException;

        // when
        initializer.start();

        // then
        final ConcurrentException thrown = assertThrows(ConcurrentException.class, initializer::get);
        assertEquals(checkedException, thrown.getCause(), "The cause of ConcurrentException should be the original checked exception");
    }

    @Test
    void get_shouldThrowConcurrentException_whenThreadIsInterrupted() throws InterruptedException {
        // given
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer(exec);
        // Use a latch to ensure the background task is sleeping when we interrupt it.
        initializer.enableLatch();
        initializer.start();

        final AtomicReference<Exception> capturedException = new AtomicReference<>();
        final CountDownLatch testFinishedLatch = new CountDownLatch(1);

        // when
        // Start a new thread to call get() and then interrupt it.
        final Thread getterThread = new Thread(() -> {
            try {
                initializer.get();
            } catch (final ConcurrentException e) {
                capturedException.set(e);
            } finally {
                // The interrupted status should still be true.
                assertTrue(Thread.currentThread().isInterrupted(), "Thread interrupted status should be true");
                testFinishedLatch.countDown();
            }
        });

        getterThread.start();
        // Wait a moment to ensure getterThread is blocked on get()
        Thread.sleep(100);
        getterThread.interrupt();

        // then
        // Wait for the getter thread to finish its assertions.
        assertTrue(testFinishedLatch.await(1, TimeUnit.SECONDS), "Test thread did not complete in time");
        assertNotNull(capturedException.get(), "A ConcurrentException should have been caught");
        assertTrue(capturedException.get().getCause() instanceof InterruptedException, "The cause should be an InterruptedException");

        // finally
        initializer.releaseLatch(); // Allow background task to complete
        exec.shutdownNow();
        exec.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    void get_shouldThrowOriginalRuntimeException_whenInitializerThrowsRuntimeException() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();
        final RuntimeException runtimeException = new RuntimeException("Test runtime exception");
        initializer.ex = runtimeException;

        // when
        initializer.start();

        // then
        final Exception thrown = assertThrows(Exception.class, initializer::get);
        assertEquals(runtimeException, thrown, "The original runtime exception should be re-thrown");
    }

    @Test
    void get_shouldReturnResultOfSuccessfulInitialization() throws ConcurrentException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when
        initializer.start();

        // then
        checkInitialize(initializer);
    }

    @Test
    void isInitialized_shouldBeTrueOnlyAfterCompletion() throws ConcurrentException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();
        initializer.enableLatch(); // Control when initialization finishes

        // when
        initializer.start();

        // then
        assertTrue(initializer.isStarted(), "Should be started");
        assertFalse(initializer.isInitialized(), "Should not be initialized before latch is released");

        // when
        initializer.releaseLatch();
        initializer.get(); // Wait for initialization to complete

        // then
        assertTrue(initializer.isInitialized(), "Should be initialized after latch is released and get() completes");
    }

    @Test
    void isStarted_shouldReturnFalse_beforeStart() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when & then
        assertFalse(initializer.isStarted(), "isStarted() should be false before start()");
    }

    @Test
    void isStarted_shouldReturnTrue_afterStart() {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when
        initializer.start();

        // then
        assertTrue(initializer.isStarted(), "isStarted() should be true after start()");
    }

    @Test
    void isStarted_shouldRemainTrue_afterInitializationCompletes() throws ConcurrentException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when
        initializer.start();
        initializer.get(); // Wait for completion

        // then
        assertTrue(initializer.isStarted(), "isStarted() should remain true after initialization");
    }

    @Test
    void setExternalExecutor_shouldUseProvidedExecutor() throws ConcurrentException {
        // given
        final ExecutorService exec = Executors.newCachedThreadPool();
        try {
            final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

            // when
            initializer.setExternalExecutor(exec);
            initializer.start();

            // then
            assertEquals(exec, initializer.getExternalExecutor(), "External executor should be the one that was set");
            assertSame(exec, initializer.getActiveExecutor(), "Active executor should be the external one");
            assertFalse(exec.isShutdown(), "External executor should not be shut down by the initializer");
            checkInitialize(initializer);
        } finally {
            exec.shutdown();
        }
    }

    @Test
    void setExternalExecutor_shouldThrowIllegalStateException_whenCalledAfterStart() throws InterruptedException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();
        initializer.start();
        final ExecutorService exec = Executors.newSingleThreadExecutor();

        // when & then
        try {
            assertThrows(IllegalStateException.class, () -> initializer.setExternalExecutor(exec));
        } finally {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void start_shouldCreateAndShutdownTemporaryExecutor_whenNoneIsProvided() throws ConcurrentException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when
        assertTrue(initializer.start(), "start() should return true on first call");
        final ExecutorService activeExecutor = initializer.getActiveExecutor();
        checkInitialize(initializer);

        // then
        assertNotNull(activeExecutor, "A temporary executor should have been created");
        assertTrue(activeExecutor.isShutdown(), "Temporary executor should be shut down after initialization");
    }

    @Test
    void start_shouldOnlyRunOnce_whenCalledMultipleTimes() throws ConcurrentException {
        // given
        final ControllableBackgroundInitializer initializer = new ControllableBackgroundInitializer();

        // when
        assertTrue(initializer.start(), "First call to start() should return true");
        for (int i = 0; i < 10; i++) {
            assertFalse(initializer.start(), "Subsequent calls to start() should return false");
        }

        // then
        checkInitialize(initializer); // Verifies that initialize() was called only once.
    }
}
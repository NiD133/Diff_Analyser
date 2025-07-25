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
 * Test class for BackgroundInitializer.
 */
class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * Test implementation of BackgroundInitializer.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /**
         * Exception to be thrown by initialize().
         */
        Exception ex;

        /**
         * Flag whether the background task should sleep a while.
         */
        boolean shouldSleep;

        /**
         * Latch for testing.
         */
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        /**
         * Object containing the state we are testing.
         */
        CloseableCounter counter = new CloseableCounter();

        /**
         * Creates a new instance of TestBackgroundInitializer.
         */
        TestBackgroundInitializer() {
        }

        /**
         * Creates a new instance of TestBackgroundInitializer with an external executor.
         *
         * @param exec the external executor
         */
        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        /**
         * Enables the latch for testing.
         */
        public void enableLatch() {
            waitForLatch = true;
        }

        /**
         * Gets the CloseableCounter object.
         *
         * @return the CloseableCounter object
         */
        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Records this invocation. Optionally throws an exception or sleeps a while.
         *
         * @return the result of the initialization
         * @throws Exception if an error occurs
         */
        @Override
        protected CloseableCounter initialize() throws Exception {
            if (ex != null) {
                throw ex;
            }
            if (shouldSleep) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (waitForLatch) {
                latch.await();
            }
            return counter.increment();
        }

        /**
         * Releases the latch for testing.
         */
        public void releaseLatch() {
            latch.countDown();
        }
    }

    /**
     * Closeable counter for testing.
     */
    protected static class CloseableCounter {
        /**
         * Number of invocations of initialize().
         */
        AtomicInteger initializeCalls = new AtomicInteger();

        /**
         * Flag whether the close consumer successfully reached this object.
         */
        AtomicBoolean closed = new AtomicBoolean();

        /**
         * Closes the counter.
         */
        public void close() {
            closed.set(true);
        }

        /**
         * Gets the number of initialize calls.
         *
         * @return the number of initialize calls
         */
        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        /**
         * Increments the counter.
         *
         * @return the incremented counter
         */
        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        /**
         * Checks if the counter is closed.
         *
         * @return true if the counter is closed, false otherwise
         */
        public boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * Helper method to check if the initialize() method was correctly called.
     *
     * @param init the initializer to test
     * @throws ConcurrentException if an error occurs
     */
    private void checkInitialize(final TestBackgroundInitializer init) throws ConcurrentException {
        final Integer result = init.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Wrong result");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), "Wrong number of invocations");
        assertNotNull(init.getFuture(), "No future");
    }

    /**
     * Test the builder method.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testBuilder() throws ConcurrentException {
        final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(null)
            .get();
        assertNull(backgroundInitializer.getExternalExecutor());
        assertFalse(backgroundInitializer.isInitialized());
        assertFalse(backgroundInitializer.isStarted());
        assertThrows(IllegalStateException.class, backgroundInitializer::getFuture);
    }

    /**
     * Test the builder method with failures.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testBuilderThenGetFailures() throws ConcurrentException {
        final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(() -> {
                throw new IllegalStateException("test");
            })
            .get();
        assertNull(backgroundInitializer.getExternalExecutor());
        assertFalse(backgroundInitializer.isInitialized());
        assertFalse(backgroundInitializer.isStarted());
        assertThrows(IllegalStateException.class, backgroundInitializer::getFuture);
        backgroundInitializer.start();
        assertEquals("test", assertThrows(IllegalStateException.class, backgroundInitializer::get).getMessage());
    }

    /**
     * Test getting the active executor before start.
     */
    @Test
    void testGetActiveExecutorBeforeStart() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        assertNull(init.getActiveExecutor(), "Got an executor");
    }

    /**
     * Test getting the active executor with an external executor.
     *
     * @throws InterruptedException if an error occurs
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testGetActiveExecutorExternal() throws InterruptedException, ConcurrentException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            final TestBackgroundInitializer init = new TestBackgroundInitializer(exec);
            init.start();
            assertSame(exec, init.getActiveExecutor(), "Wrong executor");
            checkInitialize(init);
        } finally {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    /**
     * Test getting the active executor with a temporary executor.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testGetActiveExecutorTemp() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.start();
        assertNotNull(init.getActiveExecutor(), "No active executor");
        checkInitialize(init);
    }

    /**
     * Test getting the result before start.
     */
    @Test
    void testGetBeforeStart() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        assertThrows(IllegalStateException.class, init::get);
    }

    /**
     * Test getting the result with a checked exception.
     */
    @Test
    void testGetCheckedException() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        final Exception ex = new Exception();
        init.ex = ex;
        init.start();
        final ConcurrentException cex = assertThrows(ConcurrentException.class, init::get);
        assertEquals(ex, cex.getCause(), "Exception not thrown");
    }

    /**
     * Test getting the result with an interrupted exception.
     *
     * @throws InterruptedException if an error occurs
     */
    @Test
    void testGetInterruptedException() throws InterruptedException {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final TestBackgroundInitializer init = new TestBackgroundInitializer(exec);
        final CountDownLatch latch1 = new CountDownLatch(1);
        init.shouldSleep = true;
        init.start();
        final AtomicReference<InterruptedException> iex = new AtomicReference<>();
        final Thread getThread = new Thread() {
            @Override
            public void run() {
                try {
                    init.get();
                } catch (final ConcurrentException cex) {
                    if (cex.getCause() instanceof InterruptedException) {
                        iex.set((InterruptedException) cex.getCause());
                    }
                } finally {
                    assertTrue(isInterrupted(), "Thread not interrupted");
                    latch1.countDown();
                }
            }
        };
        getThread.start();
        getThread.interrupt();
        latch1.await();
        exec.shutdownNow();
        exec.awaitTermination(1, TimeUnit.SECONDS);
        assertNotNull(iex.get(), "No interrupted exception");
    }

    /**
     * Test getting the result with a runtime exception.
     */
    @Test
    void testGetRuntimeException() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        final RuntimeException rex = new RuntimeException();
        init.ex = rex;
        init.start();
        final Exception ex = assertThrows(Exception.class, init::get);
        assertEquals(rex, ex, "Runtime exception not thrown");
    }

    /**
     * Test initializing the background initializer.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testInitialize() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.start();
        checkInitialize(init);
    }

    /**
     * Test initializing the background initializer with a temporary executor.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testInitializeTempExecutor() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        assertTrue(init.start(), "Wrong result of start()");
        checkInitialize(init);
        assertTrue(init.getActiveExecutor().isShutdown(), "Executor not shutdown");
    }

    /**
     * Test checking if the background initializer is initialized.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testIsInitialized() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.enableLatch();
        init.start();
        assertTrue(init.isStarted(), "Not started");
        assertFalse(init.isInitialized(), "Initialized before releasing latch");
        init.releaseLatch();
        init.get(); // to ensure the initialize thread has completed
        assertTrue(init.isInitialized(), "Not initialized after releasing latch");
    }

    /**
     * Test checking if the background initializer is started after getting the result.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testIsStartedAfterGet() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.start();
        checkInitialize(init);
        assertTrue(init.isStarted(), "Not started");
    }

    /**
     * Test checking if the background initializer is started before starting.
     */
    @Test
    void testIsStartedFalse() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        assertFalse(init.isStarted(), "Already started");
    }

    /**
     * Test checking if the background initializer is started after starting.
     */
    @Test
    void testIsStartedTrue() {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.start();
        assertTrue(init.isStarted(), "Not started");
    }

    /**
     * Test setting an external executor.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testSetExternalExecutor() throws ConcurrentException {
        final ExecutorService exec = Executors.newCachedThreadPool();
        try {
            final TestBackgroundInitializer init = new TestBackgroundInitializer();
            init.setExternalExecutor(exec);
            assertEquals(exec, init.getExternalExecutor(), "Wrong executor service");
            assertTrue(init.start(), "Wrong result of start()");
            assertSame(exec, init.getActiveExecutor(), "Wrong active executor");
            checkInitialize(init);
            assertFalse(exec.isShutdown(), "Executor was shutdown");
        } finally {
            exec.shutdown();
        }
    }

    /**
     * Test setting an external executor after starting.
     *
     * @throws ConcurrentException if an error occurs
     * @throws InterruptedException if an error occurs
     */
    @Test
    void testSetExternalExecutorAfterStart() throws ConcurrentException, InterruptedException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        init.start();
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalStateException.class, () -> init.setExternalExecutor(exec));
            init.get();
        } finally {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    /**
     * Test starting the background initializer multiple times.
     *
     * @throws ConcurrentException if an error occurs
     */
    @Test
    void testStartMultipleTimes() throws ConcurrentException {
        final TestBackgroundInitializer init = new TestBackgroundInitializer();
        assertTrue(init.start(), "Wrong result for start()");
        for (int i = 0; i < 10; i++) {
            assertFalse(init.start(), "Could start again");
        }
        checkInitialize(init);
    }
}
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BackgroundInitializer}.  These tests cover various scenarios,
 * including successful initialization, exception handling, executor management,
 * and lifecycle states.
 */
class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * A concrete implementation of BackgroundInitializer for testing purposes.
     * It allows controlling the initialization behavior and verifying interactions.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends BackgroundInitializer<CloseableCounter> {

        /** An exception to be thrown by initialize(). */
        Exception initializationException;

        /** A flag indicating whether the background task should sleep. */
        boolean shouldSleep;

        /** A latch for controlling when initialize completes. */
        final CountDownLatch latch = new CountDownLatch(1);

        /** A flag to indicate whether to wait for the latch in initialize. */
        boolean waitForLatch;

        /** An object containing the state we are testing. */
        CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {
        }

        AbstractBackgroundInitializerTestImpl(final ExecutorService executorService) {
            super(executorService);
        }

        /**
         * Enables the latch, causing the initialization to wait until released.
         */
        public void enableLatch() {
            waitForLatch = true;
        }

        /**
         * Returns the closeable counter being managed by this initializer.
         * @return the closeable counter
         */
        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Simulates the initialization process. Optionally throws an exception, sleeps, or waits for a latch.
         *
         * @return The initialized CloseableCounter.
         * @throws Exception If an error occurs during initialization.
         */
        protected CloseableCounter initializeInternal() throws Exception {
            if (initializationException != null) {
                throw initializationException;
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
         * Releases the latch, allowing the initialization to complete.
         */
        public void releaseLatch() {
            latch.countDown();
        }

        /**
         * Sets the exception to be thrown during initialization.
         * @param ex The exception to throw.
         */
        public void setInitializationException(final Exception ex) {
            this.initializationException = ex;
        }

        /**
         * Sets the flag to sleep during initialization.
         * @param shouldSleep Whether the initializer should sleep.
         */
        public void setShouldSleep(final boolean shouldSleep) {
            this.shouldSleep = shouldSleep;
        }
    }

    /**
     * A simple counter that tracks initialization calls and whether it has been closed.
     */
    protected static class CloseableCounter {
        /** The number of invocations of initialize(). */
        AtomicInteger initializeCalls = new AtomicInteger();

        /** Has the close consumer successfully reached this object. */
        AtomicBoolean closed = new AtomicBoolean();

        public void close() {
            closed.set(true);
        }

        public int getInitializeCalls() {
            return initializeCalls.get();
        }

        public CloseableCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * A concrete implementation of BackgroundInitializer that uses the initializeInternal method.
     */
    protected static class MethodBackgroundInitializerTestImpl extends AbstractBackgroundInitializerTestImpl {

        MethodBackgroundInitializerTestImpl() {
        }

        MethodBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * Helper method for checking whether the initialize() method was correctly called.
     * start() must already have been invoked.
     *
     * @param initializer The initializer to test.
     * @throws ConcurrentException If an error occurs during initialization.
     */
    private void checkInitialize(final AbstractBackgroundInitializerTestImpl initializer) throws ConcurrentException {
        final Integer result = initializer.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Initialize should have been called once.");
        assertEquals(1, initializer.getCloseableCounter().getInitializeCalls(), "Counter should have been incremented once.");
        assertNotNull(initializer.getFuture(), "A Future should be available.");
    }

    /**
     * Creates a new instance of AbstractBackgroundInitializerTestImpl.
     *
     * @return A new instance of AbstractBackgroundInitializerTestImpl.
     */
    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl() {
        return new MethodBackgroundInitializerTestImpl();
    }

    /**
     * Creates a new instance of AbstractBackgroundInitializerTestImpl with a given ExecutorService.
     *
     * @param executorService The ExecutorService to use.
     * @return A new instance of AbstractBackgroundInitializerTestImpl.
     */
    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl(final ExecutorService executorService) {
        return new MethodBackgroundInitializerTestImpl(executorService);
    }

    @Nested
    class BuilderTests {

        @Test
        void testBuilder() throws ConcurrentException {
            // @formatter:off
            final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
                .setCloser(null)
                .setExternalExecutor(null)
                .setInitializer(null)
                .get();
            // @formatter:on
            assertNull(backgroundInitializer.getExternalExecutor());
            assertFalse(backgroundInitializer.isInitialized());
            assertFalse(backgroundInitializer.isStarted());
            assertThrows(IllegalStateException.class, backgroundInitializer::getFuture);
        }

        @Test
        void testBuilderThenGetFailures() throws ConcurrentException {
            // @formatter:off
            final BackgroundInitializer<Object> backgroundInitializer = BackgroundInitializer.builder()
                .setCloser(null)
                .setExternalExecutor(null)
                .setInitializer(() -> {
                    throw new IllegalStateException("test");
                })
                .get();
            // @formatter:on
            assertNull(backgroundInitializer.getExternalExecutor());
            assertFalse(backgroundInitializer.isInitialized());
            assertFalse(backgroundInitializer.isStarted());
            assertThrows(IllegalStateException.class, backgroundInitializer::getFuture);
            // start
            backgroundInitializer.start();
            assertEquals("test", assertThrows(IllegalStateException.class, backgroundInitializer::get).getMessage());
        }
    }

    @Nested
    class ExecutorManagementTests {

        /**
         * Tries to obtain the executor before start(). It should not have been
         * initialized yet.
         */
        @Test
        void testGetActiveExecutorBeforeStart() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            assertNull(init.getActiveExecutor(), "Should not have an executor before start().");
        }

        /**
         * Tests whether an external executor is correctly detected.
         */
        @Test
        void testGetActiveExecutorExternal() throws InterruptedException, ConcurrentException {
            final ExecutorService externalExecutor = Executors.newSingleThreadExecutor();
            try {
                final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl(externalExecutor);
                init.start();
                assertSame(externalExecutor, init.getActiveExecutor(), "Should return the external executor.");
                checkInitialize(init);
            } finally {
                externalExecutor.shutdown();
                externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
            }
        }

        /**
         * Tests getActiveExecutor() for a temporary executor.
         */
        @Test
        void testGetActiveExecutorTemp() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.start();
            assertNotNull(init.getActiveExecutor(), "Should have an active executor after start().");
            checkInitialize(init);
        }

        /**
         * Tests whether an external executor can be set using the
         * setExternalExecutor() method.
         */
        @Test
        void testSetExternalExecutor() throws ConcurrentException {
            final ExecutorService externalExecutor = Executors.newCachedThreadPool();
            try {
                final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
                init.setExternalExecutor(externalExecutor);
                assertEquals(externalExecutor, init.getExternalExecutor(), "External executor should be set.");
                assertTrue(init.start(), "Start should return true on first call.");
                assertSame(externalExecutor, init.getActiveExecutor(), "Active executor should be the external executor.");
                checkInitialize(init);
                assertFalse(externalExecutor.isShutdown(), "Executor should not be shutdown.");
            } finally {
                externalExecutor.shutdown();
            }
        }

        /**
         * Tests that setting an executor after start() causes an exception.
         */
        @Test
        void testSetExternalExecutorAfterStart() throws ConcurrentException, InterruptedException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.start();
            final ExecutorService externalExecutor = Executors.newSingleThreadExecutor();
            try {
                assertThrows(IllegalStateException.class, () -> init.setExternalExecutor(externalExecutor),
                    "Setting executor after start() should throw IllegalStateException.");
                init.get();
            } finally {
                externalExecutor.shutdown();
                externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
            }
        }
    }

    @Nested
    class LifecycleTests {

        /**
         * Tests calling get() before start(). This should cause an exception.
         */
        @Test
        void testGetBeforeStart() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            assertThrows(IllegalStateException.class, init::get, "Calling get() before start() should throw IllegalStateException.");
        }

        /**
         * Tests isStarted() before start() was called.
         */
        @Test
        void testIsStartedFalse() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            assertFalse(init.isStarted(), "Should not be started before start() is called.");
        }

        /**
         * Tests isStarted() after start().
         */
        @Test
        void testIsStartedTrue() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.start();
            assertTrue(init.isStarted(), "Should be started after start() is called.");
        }

        /**
         * Tests the execution of the background task if a temporary executor has to
         * be created.
         */
        @Test
        void testInitializeTempExecutor() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            assertTrue(init.start(), "Start should return true on first call.");
            checkInitialize(init);
            assertTrue(init.getActiveExecutor().isShutdown(), "Temporary executor should be shutdown after completion.");
        }

        /**
         * Tests invoking start() multiple times. Only the first invocation should
         * have an effect.
         */
        @Test
        void testStartMultipleTimes() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            assertTrue(init.start(), "Start should return true on first call.");
            for (int i = 0; i < 10; i++) {
                assertFalse(init.start(), "Subsequent calls to start() should return false.");
            }
            checkInitialize(init);
        }
    }

    @Nested
    class InitializationTests {

        /**
         * Tests whether initialize() is invoked.
         */
        @Test
        void testInitialize() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.start();
            checkInitialize(init);
        }

        /**
         * Tests isInitialized() before and after the background task has finished.
         */
        @Test
        void testIsInitialized() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.enableLatch();
            init.start();
            assertTrue(init.isStarted(), "Should be started"); //Started and Initialized should return opposite values
            assertFalse(init.isInitialized(), "Should not be initialized before releasing latch.");
            init.releaseLatch();
            init.get(); //to ensure the initialize thread has completed.
            assertTrue(init.isInitialized(), "Should be initialized after releasing latch.");
        }

        /**
         * Tests isStarted() after the background task has finished.
         */
        @Test
        void testIsStartedAfterGet() throws ConcurrentException {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            init.start();
            checkInitialize(init);
            assertTrue(init.isStarted(), "Should be started after get() is called.");
        }

        /**
         * Tests the get() method if background processing causes a checked
         * exception.
         */
        @Test
        void testGetCheckedException() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            final Exception checkedException = new Exception("Simulated checked exception");
            init.setInitializationException(checkedException);
            init.start();
            final ConcurrentException concurrentException = assertThrows(ConcurrentException.class, init::get,
                "Should throw ConcurrentException when initialize throws a checked exception.");
            assertEquals(checkedException, concurrentException.getCause(), "Cause of ConcurrentException should be the original checked exception.");
        }

        /**
         * Tests the get() method if waiting for the initialization is interrupted.
         */
        @Test
        void testGetInterruptedException() throws InterruptedException {
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl(executorService);
            final CountDownLatch latch1 = new CountDownLatch(1);
            init.setShouldSleep(true);
            init.start();
            final AtomicReference<InterruptedException> interruptedException = new AtomicReference<>();
            final Thread getThread = new Thread(() -> {
                try {
                    init.get();
                } catch (final ConcurrentException cex) {
                    if (cex.getCause() instanceof InterruptedException) {
                        interruptedException.set((InterruptedException) cex.getCause());
                    }
                } finally {
                    assertTrue(Thread.currentThread().isInterrupted(), "Thread should be interrupted.");
                    latch1.countDown();
                }
            });
            getThread.start();
            getThread.interrupt();
            latch1.await();
            executorService.shutdownNow();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
            assertNotNull(interruptedException.get(), "Should have caught an InterruptedException.");
        }

        /**
         * Tests the get() method if background processing causes a runtime
         * exception.
         */
        @Test
        void testGetRuntimeException() {
            final AbstractBackgroundInitializerTestImpl init = getBackgroundInitializerTestImpl();
            final RuntimeException runtimeException = new RuntimeException("Simulated runtime exception");
            init.setInitializationException(runtimeException);
            init.start();
            final Exception thrownException = assertThrows(Exception.class, init::get,
                "Should throw RuntimeException when initialize throws a runtime exception.");
            assertEquals(runtimeException, thrownException, "Should throw the original runtime exception.");
        }
    }
}
package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.*;

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
 * Tests for BackgroundInitializer with a focus on clarity and maintainability.
 *
 * Structure:
 * - Lightweight test doubles for BackgroundInitializer and its result object (CloseableCounter).
 * - Small helper methods to remove duplication and clarify intent.
 * - Given/When/Then-style comments to make test flow explicit.
 */
class BackgroundInitializerTest extends AbstractLangTest {

    // ---------------------------------------------------------------------
    // Helpers and test doubles
    // ---------------------------------------------------------------------

    /**
     * A simple result object to count initialize() invocations and track closing.
     */
    protected static class CloseableCounter {
        private final AtomicInteger initializeCalls = new AtomicInteger();
        private final AtomicBoolean closed = new AtomicBoolean();

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
     * Base test initializer that lets tests control initialize() behavior.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        /** If set, initialize() will throw this exception. */
        Exception exceptionToThrow;

        /** If true, initialize() will sleep for a long time (to simulate slowness). */
        boolean simulateSlowInit;

        /** If true, initialize() will wait on initBlocker. */
        boolean blockOnLatch;

        /** Latch to block initialize() until released by the test. */
        final CountDownLatch initBlocker = new CountDownLatch(1);

        /** The object "created" by initialize(). */
        private final CloseableCounter counter = new CloseableCounter();

        TestBackgroundInitializer() {
            // default: no external executor
        }

        TestBackgroundInitializer(final ExecutorService exec) {
            super(exec);
        }

        public void enableLatch() {
            blockOnLatch = true;
        }

        public void releaseInitBlocker() {
            initBlocker.countDown();
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Core behavior shared by initialize() override below.
         */
        protected CloseableCounter doInitialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            if (simulateSlowInit) {
                // Use a long sleep so the thread remains blocked until interrupted.
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            if (blockOnLatch) {
                initBlocker.await();
            }
            return counter.increment();
        }
    }

    /**
     * Concrete initializer that delegates to the controllable doInitialize().
     */
    protected static class MethodBasedTestInitializer extends TestBackgroundInitializer {
        MethodBasedTestInitializer() {
        }

        MethodBasedTestInitializer(final ExecutorService exec) {
            super(exec);
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            return doInitialize();
        }
    }

    /**
     * Factory for default initializer.
     */
    protected TestBackgroundInitializer newInitializer() {
        return new MethodBasedTestInitializer();
    }

    /**
     * Factory for initializer with an external executor.
     */
    protected TestBackgroundInitializer newInitializer(final ExecutorService exec) {
        return new MethodBasedTestInitializer(exec);
    }

    /**
     * Asserts that:
     * - get() returned a result with exactly one initialize() invocation
     * - the initializer's counter also recorded exactly one initialize() invocation
     * - a future was created
     * Requires that start() has already been invoked.
     */
    private void assertInitializedOnceAndFuturePresent(final TestBackgroundInitializer init) throws ConcurrentException {
        final Integer initializeCallsFromResult = init.get().getInitializeCalls();
        assertEquals(1, initializeCallsFromResult.intValue(), "Expected exactly one initialize() call via returned result");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), "Expected exactly one initialize() call on stored counter");
        assertNotNull(init.getFuture(), "Future should be present after start()");
    }

    /**
     * Gracefully shut down an executor and await termination to avoid leaks.
     */
    private static void shutdownAndAwait(final ExecutorService exec) throws InterruptedException {
        exec.shutdownNow();
        exec.awaitTermination(1, TimeUnit.SECONDS);
    }

    // ---------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------

    @Test
    void testBuilder() {
        // Given a builder with no initializer, closer, or external executor
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(null)
            .get();

        // Then it should be unstarted and uninitialized, without a future
        assertNull(initializer.getExternalExecutor(), "External executor should be null");
        assertFalse(initializer.isInitialized(), "Should not be initialized");
        assertFalse(initializer.isStarted(), "Should not be started");
        assertThrows(IllegalStateException.class, initializer::getFuture, "getFuture() should fail before start()");
    }

    @Test
    void testBuilderThenGetFailures() throws ConcurrentException {
        // Given a builder with an initializer that throws a runtime exception
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(() -> { throw new IllegalStateException("test"); })
            .get();

        // Then before start(), it should be unstarted and uninitialized
        assertNull(initializer.getExternalExecutor(), "External executor should be null");
        assertFalse(initializer.isInitialized(), "Should not be initialized");
        assertFalse(initializer.isStarted(), "Should not be started");
        assertThrows(IllegalStateException.class, initializer::getFuture, "getFuture() should fail before start()");

        // When started and get() is called, the runtime exception should be thrown as-is
        initializer.start();
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, initializer::get);
        assertEquals("test", thrown.getMessage(), "Unexpected exception message");
    }

    @Test
    void testGetActiveExecutorBeforeStart() {
        // Given a new initializer
        final TestBackgroundInitializer init = newInitializer();

        // Then no active executor should be present before start()
        assertNull(init.getActiveExecutor(), "Active executor should be null before start()");
    }

    @Test
    void testGetActiveExecutorExternal() throws Exception {
        // Given an initializer with an external executor
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            final TestBackgroundInitializer init = newInitializer(exec);

            // When started
            init.start();

            // Then that external executor should be the active one and initialization should complete
            assertSame(exec, init.getActiveExecutor(), "Should use the provided external executor");
            assertInitializedOnceAndFuturePresent(init);
        } finally {
            shutdownAndAwait(exec);
        }
    }

    @Test
    void testGetActiveExecutorTemp() throws ConcurrentException {
        // Given a default initializer
        final TestBackgroundInitializer init = newInitializer();

        // When started
        init.start();

        // Then a temporary active executor is created and initialization should complete
        assertNotNull(init.getActiveExecutor(), "A temporary active executor should have been created");
        assertInitializedOnceAndFuturePresent(init);
    }

    @Test
    void testGetBeforeStart() {
        // Given a new initializer
        final TestBackgroundInitializer init = newInitializer();

        // Then get() before start() should fail
        assertThrows(IllegalStateException.class, init::get, "get() should fail before start()");
    }

    @Test
    void testGetCheckedException() {
        // Given an initializer whose initialize() throws a checked exception
        final TestBackgroundInitializer init = newInitializer();
        final Exception cause = new Exception("checked");
        init.exceptionToThrow = cause;

        // When started and get() is called
        init.start();

        // Then the checked exception should be wrapped in a ConcurrentException
        final ConcurrentException thrown = assertThrows(ConcurrentException.class, init::get);
        assertEquals(cause, thrown.getCause(), "Expected checked exception to be wrapped");
    }

    @Test
    void testGetInterruptedWhileWaitingPropagatesCause() throws Exception {
        // Given an initializer that blocks its initialize() on a latch
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        final TestBackgroundInitializer init = newInitializer(exec);
        init.enableLatch(); // Background thread will wait, causing get() to block
        final CountDownLatch finished = new CountDownLatch(1);

        init.start();

        final AtomicReference<InterruptedException> interruptedCause = new AtomicReference<>();

        // When a separate thread calls get() and is then interrupted
        final Thread getter = new Thread(() -> {
            try {
                init.get();
            } catch (final ConcurrentException cex) {
                if (cex.getCause() instanceof InterruptedException) {
                    interruptedCause.set((InterruptedException) cex.getCause());
                }
            } finally {
                // BackgroundInitializer.get() should preserve the interrupt status.
                assertTrue(Thread.currentThread().isInterrupted(), "Caller thread should remain interrupted");
                finished.countDown();
            }
        }, "get-thread");

        getter.start();
        getter.interrupt(); // Interrupt the thread waiting in get()
        finished.await();   // Wait until assertion in finally block runs

        // Cleanup: allow background initialize() to finish and shutdown executor
        init.releaseInitBlocker();
        shutdownAndAwait(exec);

        // Then the wrapped cause should be InterruptedException
        assertNotNull(interruptedCause.get(), "Expected an InterruptedException cause");
    }

    @Test
    void testGetRuntimeException() {
        // Given an initializer whose initialize() throws a runtime exception
        final TestBackgroundInitializer init = newInitializer();
        final RuntimeException expected = new RuntimeException("boom");
        init.exceptionToThrow = expected;

        // When started and get() is called
        init.start();

        // Then the runtime exception should be thrown directly
        final Exception thrown = assertThrows(Exception.class, init::get);
        assertEquals(expected, thrown, "Expected runtime exception to be thrown as-is");
    }

    @Test
    void testInitialize() throws ConcurrentException {
        // Given a default initializer
        final TestBackgroundInitializer init = newInitializer();

        // When started
        init.start();

        // Then initialize() should have been invoked exactly once
        assertInitializedOnceAndFuturePresent(init);
    }

    @Test
    void testInitializeTempExecutor() throws ConcurrentException {
        // Given a default initializer
        final TestBackgroundInitializer init = newInitializer();

        // When started
        assertTrue(init.start(), "First start() should return true");
        assertInitializedOnceAndFuturePresent(init);

        // Then the temporary executor should be shut down after initialization completes
        assertTrue(init.getActiveExecutor().isShutdown(), "Temporary executor should be shut down");
    }

    @Test
    void testIsInitialized() throws ConcurrentException {
        // Given an initializer that blocks completion until latch is released
        final TestBackgroundInitializer init = newInitializer();
        init.enableLatch();

        // When started but not yet completed
        init.start();
        assertTrue(init.isStarted(), "Should report started once start() is called");
        assertFalse(init.isInitialized(), "Should not be initialized while blocked");

        // When the latch is released and get() returns
        init.releaseInitBlocker();
        init.get();

        // Then it should report initialized
        assertTrue(init.isInitialized(), "Should report initialized after completion");
    }

    @Test
    void testIsStartedAfterGet() throws ConcurrentException {
        // Given a started initializer that has completed
        final TestBackgroundInitializer init = newInitializer();
        init.start();
        assertInitializedOnceAndFuturePresent(init);

        // Then it should report started
        assertTrue(init.isStarted(), "Should report started after start()");
    }

    @Test
    void testIsStartedFalse() {
        // Given a fresh initializer
        final TestBackgroundInitializer init = newInitializer();

        // Then it should not be started
        assertFalse(init.isStarted(), "Should not be started before start()");
    }

    @Test
    void testIsStartedTrue() {
        // Given a fresh initializer
        final TestBackgroundInitializer init = newInitializer();

        // When started
        init.start();

        // Then it should report started
        assertTrue(init.isStarted(), "Should report started after start()");
    }

    @Test
    void testSetExternalExecutor() throws ConcurrentException {
        // Given a default initializer and an external executor
        final ExecutorService exec = Executors.newCachedThreadPool();
        try {
            final TestBackgroundInitializer init = newInitializer();

            // When we set the external executor and start
            init.setExternalExecutor(exec);
            assertEquals(exec, init.getExternalExecutor(), "External executor should be set");
            assertTrue(init.start(), "First start() should return true");

            // Then it should use our executor, not shut it down, and initialize once
            assertSame(exec, init.getActiveExecutor(), "Active executor should be the external one");
            assertInitializedOnceAndFuturePresent(init);
            assertFalse(exec.isShutdown(), "External executor must not be shut down by initializer");
        } finally {
            exec.shutdown();
        }
    }

    @Test
    void testSetExternalExecutorAfterStart() throws Exception {
        // Given a started initializer
        final TestBackgroundInitializer init = newInitializer();
        init.start();

        // When attempting to set an executor after start()
        final ExecutorService lateExec = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalStateException.class, () -> init.setExternalExecutor(lateExec),
                    "setExternalExecutor() should fail after start()");
            init.get(); // Ensure background work completes
        } finally {
            shutdownAndAwait(lateExec);
        }
    }

    @Test
    void testStartMultipleTimes() throws ConcurrentException {
        // Given a default initializer
        final TestBackgroundInitializer init = newInitializer();

        // When calling start() multiple times
        assertTrue(init.start(), "First start() should return true");
        for (int i = 0; i < 10; i++) {
            assertFalse(init.start(), "Subsequent start() calls should return false");
        }

        // Then it should have initialized exactly once
        assertInitializedOnceAndFuturePresent(init);
    }
}
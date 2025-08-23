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
 * Tests the state management of BackgroundInitializer, specifically focusing on the 'started' state.
 *
 * Note: The original class name was BackgroundInitializerTestTest14. It has been renamed for clarity.
 */
public class BackgroundInitializerStateTest extends AbstractLangTest {

    // NOTE: The helper methods and inner classes below are part of a test fixture
    // used across multiple tests for BackgroundInitializer. They are left unchanged
    // as they are well-suited for testing complex concurrent behaviors.

    /**
     * Helper method for checking whether the initialize() method was correctly
     * called. start() must already have been invoked.
     *
     * @param init the initializer to test
     */
    private void checkInitialize(final AbstractBackgroundInitializerTestImpl init) throws ConcurrentException {
        final Integer result = init.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Wrong result");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), "Wrong number of invocations");
        assertNotNull(init.getFuture(), "No future");
    }

    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl() {
        return new MethodBackgroundInitializerTestImpl();
    }

    protected AbstractBackgroundInitializerTestImpl getBackgroundInitializerTestImpl(final ExecutorService exec) {
        return new MethodBackgroundInitializerTestImpl(exec);
    }

    /**
     * A concrete implementation of BackgroundInitializer. It also overloads
     * some methods that simplify testing.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends BackgroundInitializer<CloseableCounter> {

        /**
         * An exception to be thrown by initialize().
         */
        Exception ex;

        /**
         * A flag whether the background task should sleep a while.
         */
        boolean shouldSleep;

        /**
         * A latch tests can use to control when initialize completes.
         */
        final CountDownLatch latch = new CountDownLatch(1);

        boolean waitForLatch;

        /**
         * An object containing the state we are testing
         */
        CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {
        }

        AbstractBackgroundInitializerTestImpl(final ExecutorService exec) {
            super(exec);
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Records this invocation. Optionally throws an exception or sleeps a
         * while.
         *
         * @throws Exception in case of an error
         */
        protected CloseableCounter initializeInternal() throws Exception {
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

        public void releaseLatch() {
            latch.countDown();
        }
    }

    protected static class CloseableCounter {

        /**
         * The number of invocations of initialize().
         */
        AtomicInteger initializeCalls = new AtomicInteger();

        /**
         * Has the close consumer successfully reached this object.
         */
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

    // --- IMPROVED TEST CASE ---

    /**
     * Tests that isStarted() returns false for a newly created initializer
     * before the start() method has been invoked.
     */
    @Test
    void isStartedShouldReturnFalseBeforeStartIsCalled() {
        // Arrange: Create a new BackgroundInitializer instance.
        final BackgroundInitializer<?> initializer = getBackgroundInitializerTestImpl();

        // Act: The state is checked directly via isStarted().

        // Assert: The initializer should not be in a 'started' state.
        assertFalse(initializer.isStarted(), "isStarted() should return false before start() is called.");
    }
}
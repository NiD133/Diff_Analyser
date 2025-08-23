package org.apache.commons.lang3.concurrent;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiBackgroundInitializerTestTest18 extends AbstractLangTest {

    /**
     * Constant for the names of the child initializers.
     */
    private static final String CHILD_INIT = "childInitializer";

    /**
     * A short time to wait for background threads to run.
     */
    protected static final long PERIOD_MILLIS = 50;

    /**
     * The initializer to be tested.
     */
    protected MultiBackgroundInitializer initializer;

    /**
     * Tests whether a child initializer has been executed. Optionally the
     * expected executor service can be checked, too.
     *
     * @param child the child initializer
     * @param expExec the expected executor service (null if the executor should
     * not be checked)
     * @throws ConcurrentException if an error occurs
     */
    private void checkChild(final BackgroundInitializer<?> child, final ExecutorService expExec) throws ConcurrentException {
        final AbstractChildBackgroundInitializer cinit = (AbstractChildBackgroundInitializer) child;
        final Integer result = cinit.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Wrong result");
        assertEquals(1, cinit.initializeCalls, "Wrong number of executions");
        if (expExec != null) {
            assertEquals(expExec, cinit.currentExecutor, "Wrong executor service");
        }
    }

    /**
     * Helper method for testing the initialize() method. This method can
     * operate with both an external and a temporary executor service.
     *
     * @return the result object produced by the initializer
     * @throws org.apache.commons.lang3.concurrent.ConcurrentException so we don't have to catch it
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults checkInitialize() throws ConcurrentException {
        final int count = 5;
        for (int i = 0; i < count; i++) {
            initializer.addInitializer(CHILD_INIT + i, createChildBackgroundInitializer());
        }
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();
        assertEquals(count, res.initializerNames().size(), "Wrong number of child initializers");
        for (int i = 0; i < count; i++) {
            final String key = CHILD_INIT + i;
            assertTrue(res.initializerNames().contains(key), "Name not found: " + key);
            assertEquals(CloseableCounter.wrapInteger(1), res.getResultObject(key), "Wrong result object");
            assertFalse(res.isException(key), "Exception flag");
            assertNull(res.getException(key), "Got an exception");
            checkChild(res.getInitializer(key), initializer.getActiveExecutor());
        }
        return res;
    }

    /**
     * An overrideable method to create concrete implementations of
     * {@code BackgroundInitializer} used for defining background tasks
     * for {@code MultiBackgroundInitializer}.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * A mostly complete implementation of {@code BackgroundInitializer} used for
     * defining background tasks for {@code MultiBackgroundInitializer}.
     *
     * Subclasses will contain the initializer, either as an method implementation
     * or by using a supplier.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {

        /**
         * Stores the current executor service.
         */
        volatile ExecutorService currentExecutor;

        /**
         * An object containing the state we are testing
         */
        CloseableCounter counter = new CloseableCounter();

        /**
         * A counter for the invocations of initialize().
         */
        volatile int initializeCalls;

        /**
         * An exception to be thrown by initialize().
         */
        Exception ex;

        /**
         * A latch tests can use to control when initialize completes.
         */
        final CountDownLatch latch = new CountDownLatch(1);

        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        /**
         * Records this invocation. Optionally throws an exception.
         */
        protected CloseableCounter initializeInternal() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();
            if (waitForLatch) {
                latch.await();
            }
            if (ex != null) {
                throw ex;
            }
            return counter.increment();
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    protected static class CloseableCounter {

        // A convenience for testing that a CloseableCounter typed as Object has a specific initializeCalls value
        public static CloseableCounter wrapInteger(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }

        /**
         * The number of invocations of initialize().
         */
        volatile int initializeCalls;

        /**
         * Has the close consumer successfully reached this object.
         */
        volatile boolean closed;

        public void close() {
            closed = true;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }

        public int getInitializeCalls() {
            return initializeCalls;
        }

        @Override
        public int hashCode() {
            return initializeCalls;
        }

        public CloseableCounter increment() {
            initializeCalls++;
            return this;
        }

        public boolean isClosed() {
            return closed;
        }

        public CloseableCounter setInitializeCalls(final int i) {
            initializeCalls = i;
            return this;
        }
    }

    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {

        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * Tries to query the exception flag of an unknown child initializer from
     * the results object. This should cause an exception.
     *
     * @throws org.apache.commons.lang3.concurrent.ConcurrentException so we don't have to catch it
     */
    @Test
    void testResultIsExceptionUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = checkInitialize();
        assertThrows(NoSuchElementException.class, () -> res.isException("unknown"));
    }
}

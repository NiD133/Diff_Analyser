package org.apache.commons.lang3.concurrent;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link MultiBackgroundInitializer}.
 */
class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * A mock implementation of {@code BackgroundInitializer} for testing purposes.
     * It simulates background tasks for {@code MultiBackgroundInitializer}.
     */
    protected static class MockChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        CloseableCounter counter = new CloseableCounter();
        volatile int initializeCalls;
        Exception exceptionToThrow;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();

            if (waitForLatch) {
                latch.await();
            }

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return counter.increment();
        }

        public void releaseLatch() {
            latch.countDown();
        }
    }

    protected static class CloseableCounter {
        volatile int initializeCalls;
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

        public CloseableCounter setInitializeCalls(final int calls) {
            initializeCalls = calls;
            return this;
        }
    }

    private static final String CHILD_INIT = "childInitializer";
    protected static final long PERIOD_MILLIS = 50;
    protected MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    private void verifyChildInitializer(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final MockChildBackgroundInitializer mockInitializer = (MockChildBackgroundInitializer) child;
        assertEquals(1, mockInitializer.get().getInitializeCalls(), "Unexpected result");
        assertEquals(1, mockInitializer.initializeCalls, "Unexpected number of executions");
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, mockInitializer.currentExecutor, "Unexpected executor service");
        }
    }

    private MultiBackgroundInitializer.MultiBackgroundInitializerResults executeInitialization() throws ConcurrentException {
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT + i, createMockChildInitializer());
        }
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertEquals(childCount, results.initializerNames().size(), "Unexpected number of child initializers");
        for (int i = 0; i < childCount; i++) {
            final String key = CHILD_INIT + i;
            assertTrue(results.initializerNames().contains(key), "Name not found: " + key);
            assertEquals(CloseableCounter.wrapInteger(1), results.getResultObject(key), "Unexpected result object");
            assertFalse(results.isException(key), "Unexpected exception flag");
            assertNull(results.getException(key), "Unexpected exception");
            verifyChildInitializer(results.getInitializer(key), initializer.getActiveExecutor());
        }
        return results;
    }

    protected MockChildBackgroundInitializer createMockChildInitializer() {
        return new MockChildBackgroundInitializer();
    }

    @Test
    void testAddInitializerAfterStart() throws ConcurrentException {
        initializer.start();
        assertThrows(IllegalStateException.class, () -> initializer.addInitializer(CHILD_INIT, createMockChildInitializer()), "Initializer added after start!");
        initializer.get();
    }

    @Test
    void testAddInitializerNullInit() {
        assertNullPointerException(() -> initializer.addInitializer(CHILD_INIT, null));
    }

    @Test
    void testAddInitializerNullName() {
        assertNullPointerException(() -> initializer.addInitializer(null, createMockChildInitializer()));
    }

    @Test
    void testInitializeChildWithExecutor() throws ConcurrentException, InterruptedException {
        final String childWithExecutor = "childInitializerWithExecutor";
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final MockChildBackgroundInitializer child1 = createMockChildInitializer();
            final MockChildBackgroundInitializer child2 = createMockChildInitializer();
            child2.setExternalExecutor(executor);
            initializer.addInitializer(CHILD_INIT, child1);
            initializer.addInitializer(childWithExecutor, child2);
            initializer.start();
            initializer.get();
            verifyChildInitializer(child1, initializer.getActiveExecutor());
            verifyChildInitializer(child2, executor);
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitializeWithException() throws ConcurrentException {
        final MockChildBackgroundInitializer child = createMockChildInitializer();
        child.exceptionToThrow = new Exception();
        initializer.addInitializer(CHILD_INIT, child);
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertTrue(results.isException(CHILD_INIT), "No exception flag");
        assertNull(results.getResultObject(CHILD_INIT), "Unexpected results object");
        final ConcurrentException concurrentException = results.getException(CHILD_INIT);
        assertEquals(child.exceptionToThrow, concurrentException.getCause(), "Unexpected cause");
    }

    @Test
    void testInitializeWithExternalExecutor() throws ConcurrentException, InterruptedException {
        final ExecutorService executor = Executors.newCachedThreadPool();
        try {
            initializer = new MultiBackgroundInitializer(executor);
            executeInitialization();
            assertEquals(executor, initializer.getActiveExecutor(), "Unexpected executor");
            assertFalse(executor.isShutdown(), "Executor was shutdown");
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitializeNested() throws ConcurrentException {
        final String nestedInitializerName = "nestedInitializer";
        initializer.addInitializer(CHILD_INIT, createMockChildInitializer());
        final MultiBackgroundInitializer nestedInitializer = new MultiBackgroundInitializer();
        final int nestedCount = 3;
        for (int i = 0; i < nestedCount; i++) {
            nestedInitializer.addInitializer(CHILD_INIT + i, createMockChildInitializer());
        }
        initializer.addInitializer(nestedInitializerName, nestedInitializer);
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        final ExecutorService executor = initializer.getActiveExecutor();
        verifyChildInitializer(results.getInitializer(CHILD_INIT), executor);
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults = (MultiBackgroundInitializer.MultiBackgroundInitializerResults) results.getResultObject(nestedInitializerName);
        assertEquals(nestedCount, nestedResults.initializerNames().size(), "Unexpected number of initializers");
        for (int i = 0; i < nestedCount; i++) {
            verifyChildInitializer(nestedResults.getInitializer(CHILD_INIT + i), executor);
        }
        assertTrue(executor.isShutdown(), "Executor not shutdown");
    }

    @Test
    void testInitializeNoChildren() throws ConcurrentException {
        assertTrue(initializer.start(), "Unexpected result of start()");
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertTrue(results.initializerNames().isEmpty(), "Unexpected child initializers");
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor not shutdown");
    }

    @Test
    void testInitializeResultsIsSuccessfulFalse() throws ConcurrentException {
        final MockChildBackgroundInitializer child = createMockChildInitializer();
        child.exceptionToThrow = new Exception();
        initializer.addInitializer(CHILD_INIT, child);
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertFalse(results.isSuccessful(), "Unexpected success flag");
    }

    @Test
    void testInitializeResultsIsSuccessfulTrue() throws ConcurrentException {
        final MockChildBackgroundInitializer child = createMockChildInitializer();
        initializer.addInitializer(CHILD_INIT, child);
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        assertTrue(results.isSuccessful(), "Unexpected success flag");
    }

    @Test
    void testInitializeRuntimeException() {
        final MockChildBackgroundInitializer child = createMockChildInitializer();
        child.exceptionToThrow = new RuntimeException();
        initializer.addInitializer(CHILD_INIT, child);
        initializer.start();
        final Exception exception = assertThrows(Exception.class, initializer::get);
        assertEquals(child.exceptionToThrow, exception, "Unexpected exception");
    }

    @Test
    void testInitializeWithTemporaryExecutor() throws ConcurrentException {
        executeInitialization();
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor not shutdown");
    }

    @Test
    void testIsInitialized() throws ConcurrentException, InterruptedException {
        final MockChildBackgroundInitializer childOne = createMockChildInitializer();
        final MockChildBackgroundInitializer childTwo = createMockChildInitializer();

        childOne.enableLatch();
        childTwo.enableLatch();

        assertFalse(initializer.isInitialized(), "Unexpectedly initialized");

        initializer.addInitializer("childOne", childOne);
        initializer.addInitializer("childTwo", childTwo);
        initializer.start();

        final long startTime = System.currentTimeMillis();
        final long waitTime = 3000;
        final long endTime = startTime + waitTime;

        while (!childOne.isStarted() || !childTwo.isStarted()) {
            if (System.currentTimeMillis() > endTime) {
                fail("Children never started");
                Thread.sleep(PERIOD_MILLIS);
            }
        }

        assertFalse(initializer.isInitialized(), "Unexpectedly initialized with running children");

        childOne.releaseLatch();
        childOne.get();
        assertFalse(initializer.isInitialized(), "Unexpectedly initialized with one running child");

        childTwo.releaseLatch();
        childTwo.get();
        assertTrue(initializer.isInitialized(), "Not initialized with no running children");
    }

    @Test
    void testResultGetExceptionUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeInitialization();
        assertThrows(NoSuchElementException.class, () -> results.getException("unknown"));
    }

    @Test
    void testResultGetInitializerUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeInitialization();
        assertThrows(NoSuchElementException.class, () -> results.getInitializer("unknown"));
    }

    @Test
    void testResultGetResultObjectUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeInitialization();
        assertThrows(NoSuchElementException.class, () -> results.getResultObject("unknown"));
    }

    @Test
    void testResultInitializerNamesModify() throws ConcurrentException {
        executeInitialization();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();
        final Iterator<String> iterator = results.initializerNames().iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void testResultIsExceptionUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeInitialization();
        assertThrows(NoSuchElementException.class, () -> results.isException("unknown"));
    }
}
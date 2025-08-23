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

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiBackgroundInitializer}.
 *
 * Notes on test helpers:
 * - CloseableCounter: Tracks initialize() invocations and whether close() reached the object.
 * - ChildInitializerBase: A BackgroundInitializer<CloseableCounter> that delegates to initializeInternal(),
 *   optionally blocks on a latch, and can throw a configured exception.
 */
class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * Base class for child initializers used in tests. Implements the mechanics needed by
     * the tests (tracking executor used, counting initialize() calls, optional blocking, etc.).
     */
    protected static class ChildInitializerBase extends BackgroundInitializer<CloseableCounter> {
        volatile ExecutorService currentExecutor;
        final CloseableCounter counter = new CloseableCounter();
        volatile int initializeCalls;
        Exception ex;

        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;

        void enableLatch() {
            waitForLatch = true;
        }

        void releaseLatch() {
            latch.countDown();
        }

        CloseableCounter getCloseableCounter() {
            return counter;
        }

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
    }

    /**
     * Simple test data holder to verify close propagation and call counts.
     */
    protected static class CloseableCounter {
        static CloseableCounter wrapInteger(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }

        volatile int initializeCalls;
        volatile boolean closed;

        public void close() {
            closed = true;
        }

        public int getInitializeCalls() {
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

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CloseableCounter) {
                return initializeCalls == ((CloseableCounter) other).getInitializeCalls();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return initializeCalls;
        }
    }

    /**
     * Concrete implementation that delegates to initializeInternal().
     */
    protected static class MethodChildInitializer extends ChildInitializerBase {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    // Common test constants
    private static final String CHILD_INIT = "childInitializer";
    private static final String CHILD_ONE = "child one";
    private static final String CHILD_TWO = "child two";
    private static final int DEFAULT_CHILD_COUNT = 5;

    /** A short time to wait for background threads to run. */
    protected static final long PERIOD_MILLIS = 50;

    /** Time limit for waiting loops in tests. */
    private static final long WAIT_TIMEOUT_MILLIS = 3_000;

    /** System under test. */
    protected MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Factory for child initializers used across tests.
     */
    protected ChildInitializerBase createChildBackgroundInitializer() {
        return new MethodChildInitializer();
    }

    /**
     * Convenience: add a number of simple child initializers with unique names.
     */
    private void addChildren(final int count) {
        for (int i = 0; i < count; i++) {
            initializer.addInitializer(CHILD_INIT + i, createChildBackgroundInitializer());
        }
    }

    /**
     * Verifies that a given child initializer was executed once and, if provided, used the expected executor.
     */
    private void assertChildRanOnceWithExecutor(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor)
            throws ConcurrentException {
        final ChildInitializerBase cinit = (ChildInitializerBase) child;

        final Integer result = cinit.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Expected initialize() to return a counter incremented once");
        assertEquals(1, cinit.initializeCalls, "Child initialize() should be invoked exactly once");

        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, cinit.currentExecutor, "Child should use the expected ExecutorService");
        }
    }

    /**
     * Starts the initializer with DEFAULT_CHILD_COUNT children and validates the results.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults startAndAssertDefaultChildren()
            throws ConcurrentException {
        addChildren(DEFAULT_CHILD_COUNT);

        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        assertEquals(DEFAULT_CHILD_COUNT, res.initializerNames().size(), "Unexpected number of child initializers");

        for (int i = 0; i < DEFAULT_CHILD_COUNT; i++) {
            final String key = CHILD_INIT + i;

            assertTrue(res.initializerNames().contains(key), "Missing child initializer: " + key);
            assertEquals(CloseableCounter.wrapInteger(1), res.getResultObject(key), "Unexpected result object for " + key);
            assertFalse(res.isException(key), "Exception flag should be false for " + key);
            assertNull(res.getException(key), "No exception expected for " + key);

            assertChildRanOnceWithExecutor(res.getInitializer(key), initializer.getActiveExecutor());
        }
        return res;
    }

    /**
     * Waits until all provided child initializers have started or the timeout elapses.
     */
    private void awaitChildrenStarted(final long timeoutMillis, final ChildInitializerBase... children) throws InterruptedException {
        final long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            boolean allStarted = true;
            for (final ChildInitializerBase child : children) {
                if (!child.isStarted()) {
                    allStarted = false;
                    break;
                }
            }
            if (allStarted) {
                return;
            }
            Thread.sleep(PERIOD_MILLIS);
        }
        fail("Children never started within timeout");
    }

    @Test
    void testAddInitializerAfterStart() throws ConcurrentException {
        // Arrange
        initializer.start();

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> initializer.addInitializer(CHILD_INIT, createChildBackgroundInitializer()),
                "Should not allow adding initializers after start()");

        // Ensure background processing completes cleanly
        initializer.get();
    }

    @Test
    void testAddInitializerNullInit() {
        assertNullPointerException(() -> initializer.addInitializer(CHILD_INIT, null));
    }

    @Test
    void testAddInitializerNullName() {
        assertNullPointerException(() -> initializer.addInitializer(null, createChildBackgroundInitializer()));
    }

    @Test
    void testInitializeChildWithExecutor() throws ConcurrentException, InterruptedException {
        final String namedChildWithOwnExec = "childInitializerWithExecutor";
        final ExecutorService externalExec = Executors.newSingleThreadExecutor();
        try {
            // Arrange
            final ChildInitializerBase childUsingParentExec = createChildBackgroundInitializer();
            final ChildInitializerBase childUsingOwnExec = createChildBackgroundInitializer();
            childUsingOwnExec.setExternalExecutor(externalExec);

            initializer.addInitializer(CHILD_INIT, childUsingParentExec);
            initializer.addInitializer(namedChildWithOwnExec, childUsingOwnExec);

            // Act
            initializer.start();
            initializer.get();

            // Assert
            assertChildRanOnceWithExecutor(childUsingParentExec, initializer.getActiveExecutor());
            assertChildRanOnceWithExecutor(childUsingOwnExec, externalExec);
        } finally {
            externalExec.shutdown();
            externalExec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitializeEx() throws ConcurrentException {
        // Arrange
        final ChildInitializerBase child = createChildBackgroundInitializer();
        child.ex = new Exception();
        initializer.addInitializer(CHILD_INIT, child);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        // Assert
        assertTrue(res.isException(CHILD_INIT), "Exception flag should be true for child with checked exception");
        assertNull(res.getResultObject(CHILD_INIT), "Result object should be null when exception is thrown");
        final ConcurrentException cex = res.getException(CHILD_INIT);
        assertEquals(child.ex, cex.getCause(), "Cause of ConcurrentException should be the thrown checked exception");
    }

    @Test
    void testInitializeExternalExec() throws ConcurrentException, InterruptedException {
        final ExecutorService externalExec = Executors.newCachedThreadPool();
        try {
            // Arrange
            initializer = new MultiBackgroundInitializer(externalExec);

            // Act
            startAndAssertDefaultChildren();

            // Assert
            assertEquals(externalExec, initializer.getActiveExecutor(), "Should use provided external ExecutorService");
            assertFalse(externalExec.isShutdown(), "External ExecutorService should not be shutdown by initializer");
        } finally {
            externalExec.shutdown();
            externalExec.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitializeNested() throws ConcurrentException {
        // Arrange
        final String nestedName = "multiChildInitializer";
        initializer.addInitializer(CHILD_INIT, createChildBackgroundInitializer());

        final MultiBackgroundInitializer nested = new MultiBackgroundInitializer();
        final int nestedCount = 3;
        for (int i = 0; i < nestedCount; i++) {
            nested.addInitializer(CHILD_INIT + i, createChildBackgroundInitializer());
        }
        initializer.addInitializer(nestedName, nested);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults topLevelResults = initializer.get();

        // Assert
        final ExecutorService exec = initializer.getActiveExecutor();
        assertChildRanOnceWithExecutor(topLevelResults.getInitializer(CHILD_INIT), exec);

        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults =
                (MultiBackgroundInitializer.MultiBackgroundInitializerResults) topLevelResults.getResultObject(nestedName);

        assertEquals(nestedCount, nestedResults.initializerNames().size(), "Unexpected number of nested initializers");
        for (int i = 0; i < nestedCount; i++) {
            assertChildRanOnceWithExecutor(nestedResults.getInitializer(CHILD_INIT + i), exec);
        }
        assertTrue(exec.isShutdown(), "Parent-created ExecutorService should be shutdown");
    }

    @Test
    void testInitializeNoChildren() throws ConcurrentException {
        // Act
        assertTrue(initializer.start(), "start() should return true when there are no children");
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        // Assert
        assertTrue(res.initializerNames().isEmpty(), "There should be no child initializers");
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor should be shutdown when created by initializer");
    }

    @Test
    void testInitializeResultsIsSuccessfulFalse() throws ConcurrentException {
        // Arrange
        final ChildInitializerBase child = createChildBackgroundInitializer();
        child.ex = new Exception();
        initializer.addInitializer(CHILD_INIT, child);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        // Assert
        assertFalse(res.isSuccessful(), "isSuccessful() should be false when any child throws");
    }

    @Test
    void testInitializeResultsIsSuccessfulTrue() throws ConcurrentException {
        // Arrange
        final ChildInitializerBase child = createChildBackgroundInitializer();
        initializer.addInitializer(CHILD_INIT, child);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        // Assert
        assertTrue(res.isSuccessful(), "isSuccessful() should be true when no child throws");
    }

    @Test
    void testInitializeRuntimeEx() {
        // Arrange
        final ChildInitializerBase child = createChildBackgroundInitializer();
        child.ex = new RuntimeException();
        initializer.addInitializer(CHILD_INIT, child);
        initializer.start();

        // Act + Assert
        final Exception ex = assertThrows(Exception.class, initializer::get);
        assertEquals(child.ex, ex, "Runtime exceptions should be propagated as-is");
    }

    @Test
    void testInitializeTempExec() throws ConcurrentException {
        // Act
        startAndAssertDefaultChildren();

        // Assert
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor should be shutdown when created by initializer");
    }

    @Test
    void testIsInitialized() throws ConcurrentException, InterruptedException {
        // Arrange
        final ChildInitializerBase childOne = createChildBackgroundInitializer();
        final ChildInitializerBase childTwo = createChildBackgroundInitializer();
        childOne.enableLatch();
        childTwo.enableLatch();

        assertFalse(initializer.isInitialized(), "Should not be initialized before any start() call");

        initializer.addInitializer(CHILD_ONE, childOne);
        initializer.addInitializer(CHILD_TWO, childTwo);
        initializer.start();

        // Wait for both children to start running initialize()
        awaitChildrenStarted(WAIT_TIMEOUT_MILLIS, childOne, childTwo);
        assertFalse(initializer.isInitialized(), "Should not be initialized while children are still running");

        // Release first child
        childOne.releaseLatch();
        childOne.get(); // ensure completion
        assertFalse(initializer.isInitialized(), "Should not be initialized while one child is still running");

        // Release second child
        childTwo.releaseLatch();
        childTwo.get(); // ensure completion
        assertTrue(initializer.isInitialized(), "Should be initialized once all children complete");
    }

    @Test
    void testResultGetExceptionUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = startAndAssertDefaultChildren();
        assertThrows(NoSuchElementException.class, () -> res.getException("unknown"));
    }

    @Test
    void testResultGetInitializerUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = startAndAssertDefaultChildren();
        assertThrows(NoSuchElementException.class, () -> res.getInitializer("unknown"));
    }

    @Test
    void testResultGetResultObjectUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = startAndAssertDefaultChildren();
        assertThrows(NoSuchElementException.class, () -> res.getResultObject("unknown"));
    }

    @Test
    void testResultInitializerNamesModify() throws ConcurrentException {
        // Arrange
        startAndAssertDefaultChildren();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = initializer.get();

        // Act
        final Set<String> names = res.initializerNames();
        final Iterator<String> it = names.iterator();
        it.next();

        // Assert
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void testResultIsExceptionUnknown() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults res = startAndAssertDefaultChildren();
        assertThrows(NoSuchElementException.class, () -> res.isException("unknown"));
    }
}
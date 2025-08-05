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
 *
 * <p>This test class uses a set of nested helper classes to create controllable
 * background tasks for testing the MultiBackgroundInitializer.</p>
 */
class MultiBackgroundInitializerTest extends AbstractLangTest {

    /**
     * A test-specific implementation of BackgroundInitializer.
     * It provides hooks to control its execution, such as latches and the ability
     * to throw exceptions on demand, allowing for precise testing of concurrent behavior.
     */
    protected static class AbstractChildBackgroundInitializer extends BackgroundInitializer<CloseableCounter> {
        /** The result object, which also tracks state for testing. */
        private final CloseableCounter counter = new CloseableCounter();
        /** An exception to be thrown by initialize() for failure scenario tests. */
        private Exception ex;

        /** A latch to signal that the initialize() method has been entered. */
        private final CountDownLatch startLatch = new CountDownLatch(1);
        /** A latch to pause the initialize() method until released by the test. */
        private final CountDownLatch endLatch = new CountDownLatch(1);
        private boolean waitForEndLatch;

        /** Stores the executor service used to run this initializer. */
        private volatile ExecutorService currentExecutor;
        /** Counts the number of times initialize() is called. */
        private volatile int initializeCalls;

        public void enableEndLatch() {
            waitForEndLatch = true;
        }

        public void releaseEndLatch() {
            endLatch.countDown();
        }

        public boolean awaitStart(final long timeout, final TimeUnit unit) throws InterruptedException {
            return startLatch.await(timeout, unit);
        }

        protected CloseableCounter initializeInternal() throws Exception {
            startLatch.countDown();
            initializeCalls++;
            currentExecutor = getActiveExecutor();

            if (waitForEndLatch) {
                endLatch.await();
            }

            if (ex != null) {
                throw ex;
            }

            return counter.increment();
        }
    }

    /** A simple counter that can be "closed", used as the result of a background task. */
    protected static class CloseableCounter {
        /** A factory method for convenience in assertions. */
        public static CloseableCounter withInitializeCalls(final int i) {
            return new CloseableCounter().setInitializeCalls(i);
        }

        private volatile int initializeCalls;
        private volatile boolean closed;

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

        public CloseableCounter setInitializeCalls(final int i) {
            initializeCalls = i;
            return this;
        }

        // Custom equals for test assertions, comparing only the call count.
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

    /** A concrete implementation of the test initializer. */
    protected static class MethodChildBackgroundInitializer extends AbstractChildBackgroundInitializer {
        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    private static final String CHILD_INITIALIZER_PREFIX = "childInitializer_";

    /** The main initializer instance under test. */
    private MultiBackgroundInitializer initializer;

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    /**
     * Creates a new instance of our test-specific background initializer.
     */
    protected AbstractChildBackgroundInitializer createChildBackgroundInitializer() {
        return new MethodChildBackgroundInitializer();
    }

    /**
     * Verifies that a child initializer has been executed correctly.
     *
     * @param child the child initializer to check.
     * @param expectedExecutor the executor service that should have run the task.
     */
    private void checkChildWasExecuted(final BackgroundInitializer<?> child, final ExecutorService expectedExecutor) throws ConcurrentException {
        final AbstractChildBackgroundInitializer cinit = (AbstractChildBackgroundInitializer) child;
        final Integer result = cinit.get().getInitializeCalls();
        assertEquals(1, result.intValue(), "Child initializer should have been called once.");
        assertEquals(1, cinit.initializeCalls, "initialize() should have been invoked once.");
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, cinit.currentExecutor, "Child should have run on the correct executor service.");
        }
    }

    /**
     * Helper to get the results from a standard, successful initialization.
     *
     * @return the results object after successful initialization.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults getResultsForSuccessfulInit() throws ConcurrentException {
        initializer.addInitializer(CHILD_INITIALIZER_PREFIX + "1", createChildBackgroundInitializer());
        initializer.addInitializer(CHILD_INITIALIZER_PREFIX + "2", createChildBackgroundInitializer());
        initializer.start();
        return initializer.get();
    }

    @Test
    void addInitializer_shouldThrowIllegalStateException_whenCalledAfterStart() throws ConcurrentException {
        // Arrange
        initializer.start();
        initializer.get(); // Complete initialization

        // Act & Assert
        final IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> initializer.addInitializer("anotherChild", createChildBackgroundInitializer()));
        assertEquals("addInitializer() must be called before start()!", ex.getMessage());
    }

    @Test
    void addInitializer_shouldThrowNullPointerException_whenInitializerIsNull() {
        // Act & Assert
        assertNullPointerException(() -> initializer.addInitializer("someName", null));
    }

    @Test
    void addInitializer_shouldThrowNullPointerException_whenNameIsNull() {
        // Act & Assert
        assertNullPointerException(() -> initializer.addInitializer(null, createChildBackgroundInitializer()));
    }

    @Test
    void initialize_shouldUseAndShutdownTemporaryExecutor_whenNoneIsProvided() throws ConcurrentException {
        // Arrange
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INITIALIZER_PREFIX + i, createChildBackgroundInitializer());
        }

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertTrue(results.isSuccessful(), "Initialization should be successful.");
        assertEquals(childCount, results.initializerNames().size(), "Should have results for all children.");
        for (int i = 0; i < childCount; i++) {
            final String name = CHILD_INITIALIZER_PREFIX + i;
            assertTrue(results.initializerNames().contains(name), "Name not found: " + name);
            assertEquals(CloseableCounter.withInitializeCalls(1), results.getResultObject(name), "Wrong result object for " + name);
            assertFalse(results.isException(name), "Exception flag should be false for " + name);
            assertNull(results.getException(name), "Exception object should be null for " + name);
            checkChildWasExecuted(results.getInitializer(name), initializer.getActiveExecutor());
        }
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Temporary executor should be shut down.");
    }

    @Test
    void initialize_shouldUseProvidedExternalExecutorAndNotShutItDown() throws ConcurrentException, InterruptedException {
        // Arrange
        final ExecutorService externalExecutor = Executors.newCachedThreadPool();
        try {
            initializer = new MultiBackgroundInitializer(externalExecutor);
            final int childCount = 2;
            for (int i = 0; i < childCount; i++) {
                initializer.addInitializer(CHILD_INITIALIZER_PREFIX + i, createChildBackgroundInitializer());
            }

            // Act
            initializer.start();
            final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

            // Assert
            assertTrue(results.isSuccessful());
            assertEquals(childCount, results.initializerNames().size());
            for (String name : results.initializerNames()) {
                checkChildWasExecuted(results.getInitializer(name), externalExecutor);
            }

            assertEquals(externalExecutor, initializer.getActiveExecutor(), "Should use the provided executor.");
            assertFalse(externalExecutor.isShutdown(), "External executor should not be shut down.");
        } finally {
            externalExecutor.shutdown();
            assertTrue(externalExecutor.awaitTermination(1, TimeUnit.SECONDS), "Executor service did not shut down in time.");
        }
    }

    @Test
    void initialize_shouldUseChildsExecutor_whenChildHasOne() throws ConcurrentException, InterruptedException {
        // Arrange
        final ExecutorService childExecutor = Executors.newSingleThreadExecutor();
        try {
            final AbstractChildBackgroundInitializer childWithOwnExecutor = createChildBackgroundInitializer();
            childWithOwnExecutor.setExternalExecutor(childExecutor);

            final AbstractChildBackgroundInitializer childWithSharedExecutor = createChildBackgroundInitializer();

            initializer.addInitializer("childWithOwnExecutor", childWithOwnExecutor);
            initializer.addInitializer("childWithSharedExecutor", childWithSharedExecutor);

            // Act
            initializer.start();
            initializer.get();

            // Assert
            checkChildWasExecuted(childWithSharedExecutor, initializer.getActiveExecutor());
            checkChildWasExecuted(childWithOwnExecutor, childExecutor);
        } finally {
            childExecutor.shutdown();
            assertTrue(childExecutor.awaitTermination(1, TimeUnit.SECONDS), "Child executor did not shut down in time.");
        }
    }

    @Test
    void get_shouldStoreCheckedExceptionInResults_whenChildInitializerThrows() throws ConcurrentException {
        // Arrange
        final AbstractChildBackgroundInitializer failingChild = createChildBackgroundInitializer();
        final Exception cause = new Exception("Test exception");
        failingChild.ex = cause;
        initializer.addInitializer("failingChild", failingChild);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertTrue(results.isException("failingChild"), "Exception flag should be set for the failing child.");
        assertNull(results.getResultObject("failingChild"), "Result object should be null on failure.");
        final ConcurrentException concurrentEx = results.getException("failingChild");
        assertEquals(cause, concurrentEx.getCause(), "The stored exception should have the correct cause.");
    }

    @Test
    void get_shouldPropagateRuntimeException_whenChildInitializerThrows() {
        // Arrange
        final AbstractChildBackgroundInitializer failingChild = createChildBackgroundInitializer();
        final RuntimeException cause = new RuntimeException("Test runtime exception");
        failingChild.ex = cause;
        initializer.addInitializer("failingChild", failingChild);
        initializer.start();

        // Act & Assert
        final Exception thrown = assertThrows(Exception.class, initializer::get,
                "A RuntimeException from a child should be propagated by get().");
        assertEquals(cause, thrown, "The propagated exception should be the one thrown by the child.");
    }

    @Test
    void initialize_shouldHandleNestedMultiBackgroundInitializers() throws ConcurrentException {
        // Arrange
        // 1. Create a nested initializer with 3 children
        final String nestedInitializerName = "nestedMultiBackgroundInitializer";
        final MultiBackgroundInitializer nestedInitializer = new MultiBackgroundInitializer();
        final int nestedChildCount = 3;
        for (int i = 0; i < nestedChildCount; i++) {
            nestedInitializer.addInitializer(CHILD_INITIALIZER_PREFIX + i, createChildBackgroundInitializer());
        }

        // 2. Create the main initializer and add one direct child and the nested one
        final String directChildName = "directChild";
        initializer.addInitializer(directChildName, createChildBackgroundInitializer());
        initializer.addInitializer(nestedInitializerName, nestedInitializer);

        // Act
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        final ExecutorService executor = initializer.getActiveExecutor();

        // Check the direct child
        checkChildWasExecuted(results.getInitializer(directChildName), executor);

        // Check the nested initializer's results
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults =
                (MultiBackgroundInitializer.MultiBackgroundInitializerResults) results.getResultObject(nestedInitializerName);
        assertEquals(nestedChildCount, nestedResults.initializerNames().size(), "Wrong number of nested initializers.");
        for (int i = 0; i < nestedChildCount; i++) {
            checkChildWasExecuted(nestedResults.getInitializer(CHILD_INITIALIZER_PREFIX + i), executor);
        }

        assertTrue(executor.isShutdown(), "Main temporary executor should be shut down.");
    }

    @Test
    void initialize_shouldSucceed_whenThereAreNoChildInitializers() throws ConcurrentException {
        // Act
        assertTrue(initializer.start(), "start() should return true.");
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertTrue(results.initializerNames().isEmpty(), "There should be no child initializers in the results.");
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Executor should be shut down even with no children.");
    }

    @Test
    void resultsIsSuccessful_shouldReturnFalse_whenAChildInitializerFails() throws ConcurrentException {
        // Arrange
        final AbstractChildBackgroundInitializer failingChild = createChildBackgroundInitializer();
        failingChild.ex = new Exception("Failure");
        initializer.addInitializer("failingChild", failingChild);
        initializer.addInitializer("successfulChild", createChildBackgroundInitializer());
        initializer.start();

        // Act
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertFalse(results.isSuccessful(), "isSuccessful() should be false if any child fails.");
    }

    @Test
    void resultsIsSuccessful_shouldReturnTrue_whenAllChildInitializersSucceed() throws ConcurrentException {
        // Arrange
        initializer.addInitializer("child1", createChildBackgroundInitializer());
        initializer.addInitializer("child2", createChildBackgroundInitializer());
        initializer.start();

        // Act
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert
        assertTrue(results.isSuccessful(), "isSuccessful() should be true when all children succeed.");
    }

    @Test
    void isInitialized_shouldTrackTheLifecycleOfChildInitializers() throws Exception {
        // Arrange
        final AbstractChildBackgroundInitializer childOne = createChildBackgroundInitializer();
        final AbstractChildBackgroundInitializer childTwo = createChildBackgroundInitializer();
        childOne.enableEndLatch();
        childTwo.enableEndLatch();

        assertFalse(initializer.isInitialized(), "Should not be initialized before start.");

        initializer.addInitializer("childOne", childOne);
        initializer.addInitializer("childTwo", childTwo);

        // Act
        initializer.start();

        // Assert that children have started but are paused by the latch
        assertTrue(childOne.awaitStart(3, TimeUnit.SECONDS), "Child one did not start in time.");
        assertTrue(childTwo.awaitStart(3, TimeUnit.SECONDS), "Child two did not start in time.");
        assertFalse(initializer.isInitialized(), "Should not be initialized while children are running.");

        // Allow one child to finish
        childOne.releaseEndLatch();
        childOne.get(); // Block until this child is fully initialized
        assertFalse(initializer.isInitialized(), "Should not be initialized while one child is still running.");

        // Allow the second child to finish
        childTwo.releaseEndLatch();
        childTwo.get(); // Block until this child is fully initialized
        assertTrue(initializer.isInitialized(), "Should be initialized after all children have finished.");
    }

    @Test
    void resultsGetException_shouldThrowNoSuchElementException_forUnknownInitializer() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = getResultsForSuccessfulInit();
        final String unknownName = "unknownInitializer";

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> results.getException(unknownName));
    }

    @Test
    void resultsGetInitializer_shouldThrowNoSuchElementException_forUnknownInitializer() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = getResultsForSuccessfulInit();
        final String unknownName = "unknownInitializer";

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> results.getInitializer(unknownName));
    }

    @Test
    void resultsGetResultObject_shouldThrowNoSuchElementException_forUnknownInitializer() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = getResultsForSuccessfulInit();
        final String unknownName = "unknownInitializer";

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> results.getResultObject(unknownName));
    }

    @Test
    void resultsIsException_shouldThrowNoSuchElementException_forUnknownInitializer() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = getResultsForSuccessfulInit();
        final String unknownName = "unknownInitializer";

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> results.isException(unknownName));
    }

    @Test
    void resultsInitializerNames_shouldReturnUnmodifiableSet() throws ConcurrentException {
        // Arrange
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = getResultsForSuccessfulInit();
        final Iterator<String> it = results.initializerNames().iterator();
        it.next();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, it::remove,
                "The set of initializer names should be unmodifiable.");
    }
}
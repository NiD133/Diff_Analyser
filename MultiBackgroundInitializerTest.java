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
 * This test suite verifies that MultiBackgroundInitializer can:
 * - Manage multiple child initializers concurrently
 * - Handle exceptions from child initializers properly
 * - Work with both external and temporary executor services
 * - Provide correct results and status information
 */
class MultiBackgroundInitializerTest extends AbstractLangTest {

    // Test constants
    private static final String CHILD_INIT_NAME = "childInitializer";
    private static final long BACKGROUND_THREAD_WAIT_MILLIS = 50;
    private static final int MULTIPLE_CHILDREN_COUNT = 5;
    private static final int NESTED_CHILDREN_COUNT = 3;
    private static final long LATCH_TIMEOUT_MILLIS = 3000;

    private MultiBackgroundInitializer multiInitializer;

    @BeforeEach
    public void setUp() {
        multiInitializer = new MultiBackgroundInitializer();
    }

    // ========== Test Helper Classes ==========

    /**
     * A test counter that can be closed and tracks initialization calls.
     * Used as the result object for test initializers.
     */
    protected static class TestCounter {
        private volatile int initializationCount;
        private volatile boolean isClosed;

        public static TestCounter withCount(final int count) {
            return new TestCounter().setCount(count);
        }

        public void close() {
            isClosed = true;
        }

        public TestCounter increment() {
            initializationCount++;
            return this;
        }

        public int getCount() {
            return initializationCount;
        }

        public boolean isClosed() {
            return isClosed;
        }

        public TestCounter setCount(final int count) {
            initializationCount = count;
            return this;
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestCounter && 
                   initializationCount == ((TestCounter) other).getCount();
        }

        @Override
        public int hashCode() {
            return initializationCount;
        }
    }

    /**
     * Base class for test background initializers.
     * Provides common functionality like tracking executor service and initialization calls.
     */
    protected static class TestBackgroundInitializer extends BackgroundInitializer<TestCounter> {
        volatile ExecutorService executorUsed;
        volatile int initializeCallCount;
        
        final TestCounter counter = new TestCounter();
        Exception exceptionToThrow;
        
        // Latch mechanism for controlling when initialization completes
        final CountDownLatch completionLatch = new CountDownLatch(1);
        boolean shouldWaitForLatch;

        public void enableLatchWait() {
            shouldWaitForLatch = true;
        }

        public void releaseLatch() {
            completionLatch.countDown();
        }

        public TestCounter getCounter() {
            return counter;
        }

        @Override
        protected TestCounter initialize() throws Exception {
            initializeCallCount++;
            executorUsed = getActiveExecutor();

            if (shouldWaitForLatch) {
                completionLatch.await();
            }

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return counter.increment();
        }
    }

    // ========== Test Helper Methods ==========

    private TestBackgroundInitializer createTestInitializer() {
        return new TestBackgroundInitializer();
    }

    private void verifyChildInitializerExecution(final BackgroundInitializer<?> child,
                                                final ExecutorService expectedExecutor) throws ConcurrentException {
        final TestBackgroundInitializer testChild = (TestBackgroundInitializer) child;
        final TestCounter result = testChild.get();
        
        assertEquals(1, result.getCount(), "Child initializer should have been called exactly once");
        assertEquals(1, testChild.initializeCallCount, "Initialize method should have been called exactly once");
        
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, testChild.executorUsed, 
                        "Child should have used the expected executor service");
        }
    }

    private MultiBackgroundInitializer.MultiBackgroundInitializerResults executeMultipleChildrenTest() 
            throws ConcurrentException {
        // Add multiple child initializers
        for (int i = 0; i < MULTIPLE_CHILDREN_COUNT; i++) {
            multiInitializer.addInitializer(CHILD_INIT_NAME + i, createTestInitializer());
        }
        
        multiInitializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        // Verify all children were processed correctly
        assertEquals(MULTIPLE_CHILDREN_COUNT, results.initializerNames().size(), 
                    "Should have processed all child initializers");
        
        for (int i = 0; i < MULTIPLE_CHILDREN_COUNT; i++) {
            final String childName = CHILD_INIT_NAME + i;
            
            assertTrue(results.initializerNames().contains(childName), 
                      "Results should contain child: " + childName);
            assertEquals(TestCounter.withCount(1), results.getResultObject(childName), 
                        "Child should have correct result object");
            assertFalse(results.isException(childName), 
                       "Child should not have thrown exception");
            assertNull(results.getException(childName), 
                      "Child should have no exception");
            
            verifyChildInitializerExecution(results.getInitializer(childName), 
                                          multiInitializer.getActiveExecutor());
        }
        
        return results;
    }

    // ========== Validation Tests ==========

    @Test
    void testAddInitializer_WithNullName_ThrowsException() {
        assertNullPointerException(() -> 
            multiInitializer.addInitializer(null, createTestInitializer()),
            "Should reject null initializer name");
    }

    @Test
    void testAddInitializer_WithNullInitializer_ThrowsException() {
        assertNullPointerException(() -> 
            multiInitializer.addInitializer(CHILD_INIT_NAME, null),
            "Should reject null initializer");
    }

    @Test
    void testAddInitializer_AfterStart_ThrowsException() throws ConcurrentException {
        multiInitializer.start();
        
        assertThrows(IllegalStateException.class, () -> 
            multiInitializer.addInitializer(CHILD_INIT_NAME, createTestInitializer()),
            "Should not allow adding initializers after start() is called");
        
        multiInitializer.get(); // Complete the initialization
    }

    // ========== Basic Functionality Tests ==========

    @Test
    void testInitialize_WithNoChildren_CompletesSuccessfully() throws ConcurrentException {
        assertTrue(multiInitializer.start(), "Start should return true");
        
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        assertTrue(results.initializerNames().isEmpty(), "Should have no child initializers");
        assertTrue(multiInitializer.getActiveExecutor().isShutdown(), 
                  "Temporary executor should be shut down");
    }

    @Test
    void testInitialize_WithTemporaryExecutor_ExecutesAllChildren() throws ConcurrentException {
        executeMultipleChildrenTest();
        assertTrue(multiInitializer.getActiveExecutor().isShutdown(), 
                  "Temporary executor should be shut down after completion");
    }

    @Test
    void testInitialize_WithExternalExecutor_KeepsExecutorRunning() 
            throws ConcurrentException, InterruptedException {
        final ExecutorService externalExecutor = Executors.newCachedThreadPool();
        
        try {
            multiInitializer = new MultiBackgroundInitializer(externalExecutor);
            executeMultipleChildrenTest();
            
            assertEquals(externalExecutor, multiInitializer.getActiveExecutor(), 
                        "Should use the provided external executor");
            assertFalse(externalExecutor.isShutdown(), 
                       "External executor should not be shut down");
        } finally {
            externalExecutor.shutdown();
            externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    // ========== Exception Handling Tests ==========

    @Test
    void testInitialize_WithChildThrowingCheckedException_CapturesException() throws ConcurrentException {
        final TestBackgroundInitializer childWithException = createTestInitializer();
        childWithException.exceptionToThrow = new Exception("Test checked exception");
        
        multiInitializer.addInitializer(CHILD_INIT_NAME, childWithException);
        multiInitializer.start();
        
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        assertTrue(results.isException(CHILD_INIT_NAME), "Should indicate child threw exception");
        assertNull(results.getResultObject(CHILD_INIT_NAME), "Should have no result object");
        
        final ConcurrentException capturedException = results.getException(CHILD_INIT_NAME);
        assertEquals(childWithException.exceptionToThrow, capturedException.getCause(), 
                    "Should wrap the original exception");
    }

    @Test
    void testInitialize_WithChildThrowingRuntimeException_PropagatesException() {
        final TestBackgroundInitializer childWithException = createTestInitializer();
        childWithException.exceptionToThrow = new RuntimeException("Test runtime exception");
        
        multiInitializer.addInitializer(CHILD_INIT_NAME, childWithException);
        multiInitializer.start();
        
        final Exception thrownException = assertThrows(Exception.class, multiInitializer::get,
                                                      "Runtime exceptions should be propagated");
        assertEquals(childWithException.exceptionToThrow, thrownException, 
                    "Should propagate the exact runtime exception");
    }

    // ========== Advanced Functionality Tests ==========

    @Test
    void testInitialize_WithChildHavingOwnExecutor_RespectsChildExecutor() 
            throws ConcurrentException, InterruptedException {
        final String childWithOwnExecutorName = "childWithOwnExecutor";
        final ExecutorService childExecutor = Executors.newSingleThreadExecutor();
        
        try {
            final TestBackgroundInitializer childWithDefaultExecutor = createTestInitializer();
            final TestBackgroundInitializer childWithOwnExecutor = createTestInitializer();
            childWithOwnExecutor.setExternalExecutor(childExecutor);
            
            multiInitializer.addInitializer(CHILD_INIT_NAME, childWithDefaultExecutor);
            multiInitializer.addInitializer(childWithOwnExecutorName, childWithOwnExecutor);
            multiInitializer.start();
            multiInitializer.get();
            
            verifyChildInitializerExecution(childWithDefaultExecutor, multiInitializer.getActiveExecutor());
            verifyChildInitializerExecution(childWithOwnExecutor, childExecutor);
        } finally {
            childExecutor.shutdown();
            childExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitialize_WithNestedMultiInitializers_ExecutesAllLevels() throws ConcurrentException {
        final String nestedInitializerName = "nestedMultiInitializer";
        
        // Add a regular child to the main initializer
        multiInitializer.addInitializer(CHILD_INIT_NAME, createTestInitializer());
        
        // Create a nested MultiBackgroundInitializer with multiple children
        final MultiBackgroundInitializer nestedInitializer = new MultiBackgroundInitializer();
        for (int i = 0; i < NESTED_CHILDREN_COUNT; i++) {
            nestedInitializer.addInitializer(CHILD_INIT_NAME + i, createTestInitializer());
        }
        multiInitializer.addInitializer(nestedInitializerName, nestedInitializer);
        
        multiInitializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        final ExecutorService usedExecutor = multiInitializer.getActiveExecutor();
        
        // Verify the regular child
        verifyChildInitializerExecution(results.getInitializer(CHILD_INIT_NAME), usedExecutor);
        
        // Verify the nested initializer and its children
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults = 
            (MultiBackgroundInitializer.MultiBackgroundInitializerResults) results.getResultObject(nestedInitializerName);
        
        assertEquals(NESTED_CHILDREN_COUNT, nestedResults.initializerNames().size(), 
                    "Nested initializer should have all its children");
        
        for (int i = 0; i < NESTED_CHILDREN_COUNT; i++) {
            verifyChildInitializerExecution(nestedResults.getInitializer(CHILD_INIT_NAME + i), usedExecutor);
        }
        
        assertTrue(usedExecutor.isShutdown(), "Main executor should be shut down");
    }

    @Test
    void testIsInitialized_TracksInitializationProgress() throws ConcurrentException, InterruptedException {
        final TestBackgroundInitializer firstChild = createTestInitializer();
        final TestBackgroundInitializer secondChild = createTestInitializer();
        
        // Enable latch waiting so we can control when initialization completes
        firstChild.enableLatchWait();
        secondChild.enableLatchWait();
        
        assertFalse(multiInitializer.isInitialized(), 
                   "Should not be initialized before adding any children");
        
        multiInitializer.addInitializer("first", firstChild);
        multiInitializer.addInitializer("second", secondChild);
        multiInitializer.start();
        
        // Wait for children to start
        waitForChildrenToStart(firstChild, secondChild);
        
        assertFalse(multiInitializer.isInitialized(), 
                   "Should not be initialized while children are running");
        
        // Complete first child
        firstChild.releaseLatch();
        firstChild.get();
        assertFalse(multiInitializer.isInitialized(), 
                   "Should not be initialized while one child is still running");
        
        // Complete second child
        secondChild.releaseLatch();
        secondChild.get();
        assertTrue(multiInitializer.isInitialized(), 
                  "Should be initialized when all children complete");
    }

    private void waitForChildrenToStart(TestBackgroundInitializer... children) throws InterruptedException {
        final long endTime = System.currentTimeMillis() + LATCH_TIMEOUT_MILLIS;
        
        while (System.currentTimeMillis() < endTime) {
            boolean allStarted = true;
            for (TestBackgroundInitializer child : children) {
                if (!child.isStarted()) {
                    allStarted = false;
                    break;
                }
            }
            if (allStarted) {
                return;
            }
            Thread.sleep(BACKGROUND_THREAD_WAIT_MILLIS);
        }
        fail("Children never started within timeout period");
    }

    // ========== Results Object Tests ==========

    @Test
    void testResults_IsSuccessful_TrueWhenNoExceptions() throws ConcurrentException {
        final TestBackgroundInitializer child = createTestInitializer();
        multiInitializer.addInitializer(CHILD_INIT_NAME, child);
        multiInitializer.start();
        
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        assertTrue(results.isSuccessful(), "Should be successful when no exceptions occur");
    }

    @Test
    void testResults_IsSuccessful_FalseWhenExceptionOccurs() throws ConcurrentException {
        final TestBackgroundInitializer childWithException = createTestInitializer();
        childWithException.exceptionToThrow = new Exception("Test exception");
        
        multiInitializer.addInitializer(CHILD_INIT_NAME, childWithException);
        multiInitializer.start();
        
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        assertFalse(results.isSuccessful(), "Should not be successful when exception occurs");
    }

    @Test
    void testResults_InitializerNames_IsUnmodifiable() throws ConcurrentException {
        executeMultipleChildrenTest();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.get();
        
        final Iterator<String> nameIterator = results.initializerNames().iterator();
        nameIterator.next();
        
        assertThrows(UnsupportedOperationException.class, nameIterator::remove,
                    "Initializer names set should be unmodifiable");
    }

    @Test
    void testResults_GetResultObject_WithUnknownName_ThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeMultipleChildrenTest();
        
        assertThrows(NoSuchElementException.class, () -> results.getResultObject("unknownChild"),
                    "Should throw exception for unknown child name");
    }

    @Test
    void testResults_GetInitializer_WithUnknownName_ThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeMultipleChildrenTest();
        
        assertThrows(NoSuchElementException.class, () -> results.getInitializer("unknownChild"),
                    "Should throw exception for unknown child name");
    }

    @Test
    void testResults_GetException_WithUnknownName_ThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeMultipleChildrenTest();
        
        assertThrows(NoSuchElementException.class, () -> results.getException("unknownChild"),
                    "Should throw exception for unknown child name");
    }

    @Test
    void testResults_IsException_WithUnknownName_ThrowsException() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = executeMultipleChildrenTest();
        
        assertThrows(NoSuchElementException.class, () -> results.isException("unknownChild"),
                    "Should throw exception for unknown child name");
    }
}
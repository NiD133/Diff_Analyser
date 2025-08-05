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
 */
class MultiBackgroundInitializerTest extends AbstractLangTest {
    /**
     * Helper class to simulate child initializers with controllable behavior.
     */
    protected static class ChildInitializer extends BackgroundInitializer<InitializationState> {
        volatile ExecutorService currentExecutor;
        final InitializationState state = new InitializationState();
        volatile int initializeCalls;
        Exception exceptionToThrow;
        final CountDownLatch completionLatch = new CountDownLatch(1);
        boolean waitForLatch;

        public void enableLatch() {
            waitForLatch = true;
        }

        public InitializationState getState() {
            return state;
        }

        @Override
        protected InitializationState initialize() throws Exception {
            initializeCalls++;
            currentExecutor = getActiveExecutor();

            if (waitForLatch) {
                completionLatch.await();
            }

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return state.increment();
        }

        public void releaseLatch() {
            completionLatch.countDown();
        }
    }

    /**
     * Tracks initialization state for test verification.
     */
    protected static class InitializationState {
        volatile int initializationCount;
        volatile boolean closed;

        public static InitializationState withCount(int count) {
            return new InitializationState().setCount(count);
        }

        public void close() {
            closed = true;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof InitializationState && 
                   initializationCount == ((InitializationState) other).getCount();
        }

        public int getCount() {
            return initializationCount;
        }

        @Override
        public int hashCode() {
            return initializationCount;
        }

        public InitializationState increment() {
            initializationCount++;
            return this;
        }

        public boolean isClosed() {
            return closed;
        }

        public InitializationState setCount(int count) {
            initializationCount = count;
            return this;
        }
    }

    private static final String CHILD_INIT_PREFIX = "childInitializer";
    protected static final long SHORT_TIMEOUT = 50;
    protected MultiBackgroundInitializer initializer;

    /**
     * Verifies child initializer executed correctly.
     */
    private void verifyChildExecution(BackgroundInitializer<?> child, 
                                      ExecutorService expectedExecutor) 
        throws ConcurrentException {
        final ChildInitializer childInit = (ChildInitializer) child;
        final int resultCount = childInit.get().getCount();
        assertEquals(1, resultCount, "Incorrect initialization count");
        assertEquals(1, childInit.initializeCalls, "Incorrect execution count");
        if (expectedExecutor != null) {
            assertEquals(expectedExecutor, childInit.currentExecutor, 
                         "Incorrect executor service used");
        }
    }

    /**
     * Tests initialization process with multiple children.
     */
    private MultiBackgroundInitializer.MultiBackgroundInitializerResults testInitializeWithChildren() 
        throws ConcurrentException {
        final int childCount = 5;
        for (int i = 0; i < childCount; i++) {
            initializer.addInitializer(CHILD_INIT_PREFIX + i, createChildInitializer());
        }
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
        
        assertEquals(childCount, results.initializerNames().size(), 
                     "Incorrect number of child initializers");
        
        for (int i = 0; i < childCount; i++) {
            final String name = CHILD_INIT_PREFIX + i;
            assertTrue(results.initializerNames().contains(name), 
                       "Missing child initializer: " + name);
            assertEquals(InitializationState.withCount(1), results.getResultObject(name), 
                         "Incorrect result object for " + name);
            assertFalse(results.isException(name), 
                        "Unexpected exception flag for " + name);
            assertNull(results.getException(name), 
                       "Unexpected exception for " + name);
            verifyChildExecution(results.getInitializer(name), 
                                 initializer.getActiveExecutor());
        }
        return results;
    }

    /**
     * Creates a new child initializer instance.
     */
    protected ChildInitializer createChildInitializer() {
        return new ChildInitializer();
    }

    @BeforeEach
    public void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    void testAddInitializerAfterStart_ThrowsIllegalState() throws ConcurrentException {
        // Setup: Start initializer
        initializer.start();
        
        // Verify adding after start is prohibited
        assertThrows(IllegalStateException.class, 
            () -> initializer.addInitializer(CHILD_INIT_PREFIX, createChildInitializer()),
            "Should prevent adding initializers after start()");
        
        initializer.get();
    }

    @Test
    void testAddNullInitializer_ThrowsNullPointer() {
        assertNullPointerException(
            () -> initializer.addInitializer(CHILD_INIT_PREFIX, null),
            "Should reject null initializer");
    }

    @Test
    void testAddInitializerWithNullName_ThrowsNullPointer() {
        assertNullPointerException(
            () -> initializer.addInitializer(null, createChildInitializer()),
            "Should reject null name");
    }

    @Test
    void testInitializeWithChildExecutor_RespectsCustomExecutor() 
        throws ConcurrentException, InterruptedException {
        final String customExecutorChild = "childWithCustomExecutor";
        final ExecutorService customExecutor = Executors.newSingleThreadExecutor();
        
        try {
            // Setup children with different executors
            final ChildInitializer defaultChild = createChildInitializer();
            final ChildInitializer customChild = createChildInitializer();
            customChild.setExternalExecutor(customExecutor);
            
            initializer.addInitializer(CHILD_INIT_PREFIX, defaultChild);
            initializer.addInitializer(customExecutorChild, customChild);
            initializer.start();
            initializer.get();
            
            // Verify correct executors used
            verifyChildExecution(defaultChild, initializer.getActiveExecutor());
            verifyChildExecution(customChild, customExecutor);
        } finally {
            customExecutor.shutdown();
            customExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testInitializeWithChildException_RecordsFailure() throws ConcurrentException {
        // Setup child that throws exception
        final ChildInitializer failingChild = createChildInitializer();
        failingChild.exceptionToThrow = new Exception("Test exception");
        initializer.addInitializer(CHILD_INIT_PREFIX, failingChild);
        
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
        
        // Verify exception handling
        assertTrue(results.isException(CHILD_INIT_PREFIX), 
                   "Should flag exception");
        assertNull(results.getResultObject(CHILD_INIT_PREFIX), 
                   "Should not have result object");
        assertEquals(failingChild.exceptionToThrow, 
                     results.getException(CHILD_INIT_PREFIX).getCause(),
                     "Should record exception cause");
    }

    @Test
    void testInitializeWithExternalExecutor_ProperlyManagesExecutor() 
        throws ConcurrentException, InterruptedException {
        final ExecutorService externalExecutor = Executors.newCachedThreadPool();
        
        try {
            // Setup with external executor
            initializer = new MultiBackgroundInitializer(externalExecutor);
            testInitializeWithChildren();
            
            // Verify executor handling
            assertEquals(externalExecutor, initializer.getActiveExecutor(),
                         "Should use external executor");
            assertFalse(externalExecutor.isShutdown(), 
                        "Should not shutdown external executor");
        } finally {
            externalExecutor.shutdown();
            externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testNestedInitializers_HandlesHierarchy() throws ConcurrentException {
        // Setup nested initializers
        initializer.addInitializer(CHILD_INIT_PREFIX, createChildInitializer());
        
        final MultiBackgroundInitializer nestedInitializer = new MultiBackgroundInitializer();
        final int nestedCount = 3;
        for (int i = 0; i < nestedCount; i++) {
            nestedInitializer.addInitializer(CHILD_INIT_PREFIX + i, createChildInitializer());
        }
        initializer.addInitializer("nested", nestedInitializer);
        
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
        final ExecutorService rootExecutor = initializer.getActiveExecutor();
        
        // Verify parent
        verifyChildExecution(results.getInitializer(CHILD_INIT_PREFIX), rootExecutor);
        
        // Verify nested
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults nestedResults = 
            (MultiBackgroundInitializer.MultiBackgroundInitializerResults) 
            results.getResultObject("nested");
            
        assertEquals(nestedCount, nestedResults.initializerNames().size(),
                     "Incorrect nested initializer count");
        
        for (int i = 0; i < nestedCount; i++) {
            verifyChildExecution(nestedResults.getInitializer(CHILD_INIT_PREFIX + i), 
                                rootExecutor);
        }
        
        assertTrue(rootExecutor.isShutdown(), 
                   "Should shutdown temporary executor");
    }

    @Test
    void testInitializeWithNoChildren_CompletesSuccessfully() throws ConcurrentException {
        // Test empty initializer
        assertTrue(initializer.start(), "start() should return true");
        
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
            
        assertTrue(results.initializerNames().isEmpty(), 
                   "Should have no child initializers");
        assertTrue(initializer.getActiveExecutor().isShutdown(),
                   "Should shutdown executor");
    }

    @Test
    void testResultsIsSuccessful_FalseWhenChildFails() throws ConcurrentException {
        // Setup failing child
        final ChildInitializer failingChild = createChildInitializer();
        failingChild.exceptionToThrow = new Exception();
        initializer.addInitializer(CHILD_INIT_PREFIX, failingChild);
        
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
            
        assertFalse(results.isSuccessful(), 
                    "Should indicate failure when child throws exception");
    }

    @Test
    void testResultsIsSuccessful_TrueWhenAllSucceed() throws ConcurrentException {
        // Setup successful child
        final ChildInitializer successfulChild = createChildInitializer();
        initializer.addInitializer(CHILD_INIT_PREFIX, successfulChild);
        
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
            
        assertTrue(results.isSuccessful(), 
                   "Should indicate success when all children succeed");
    }

    @Test
    void testInitializeWithRuntimeException_PropagatesException() {
        // Setup child that throws runtime exception
        final ChildInitializer failingChild = createChildInitializer();
        failingChild.exceptionToThrow = new RuntimeException("Test runtime exception");
        initializer.addInitializer(CHILD_INIT_PREFIX, failingChild);
        
        initializer.start();
        final Exception exception = assertThrows(Exception.class, 
            () -> initializer.get(),
            "Should propagate runtime exception");
            
        assertEquals(failingChild.exceptionToThrow, exception,
                     "Should throw child's exception");
    }

    @Test
    void testInitializeWithTemporaryExecutor_ShutsDownAfterCompletion() 
        throws ConcurrentException {
        testInitializeWithChildren();
        assertTrue(initializer.getActiveExecutor().isShutdown(),
                   "Should shutdown temporary executor");
    }

    @Test
    void testIsInitialized_ReportsCorrectStatus() 
        throws ConcurrentException, InterruptedException {
        // Setup controlled children
        final ChildInitializer child1 = createChildInitializer();
        final ChildInitializer child2 = createChildInitializer();
        child1.enableLatch();
        child2.enableLatch();

        // Pre-start verification
        assertFalse(initializer.isInitialized(), 
                    "Should not be initialized before adding children");

        // Add children and start
        initializer.addInitializer("child1", child1);
        initializer.addInitializer("child2", child2);
        initializer.start();

        // Wait for children to start
        final long timeout = System.currentTimeMillis() + 3000;
        while (!child1.isStarted() || !child2.isStarted()) {
            if (System.currentTimeMillis() > timeout) {
                fail("Children failed to start within timeout");
            }
            Thread.sleep(SHORT_TIMEOUT);
        }

        // Mid-execution verification
        assertFalse(initializer.isInitialized(), 
                    "Should not be initialized while children are running");

        // Complete child1
        child1.releaseLatch();
        child1.get();  // Wait for completion
        assertFalse(initializer.isInitialized(), 
                    "Should not be initialized while one child is running");

        // Complete child2
        child2.releaseLatch();
        child2.get();  // Wait for completion
        assertTrue(initializer.isInitialized(), 
                   "Should be initialized after all children complete");
    }

    @Test
    void testResultsGetException_UnknownChildThrows() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            testInitializeWithChildren();
        
        assertThrows(NoSuchElementException.class, 
            () -> results.getException("unknown"),
            "Should throw for unknown child");
    }

    @Test
    void testResultsGetInitializer_UnknownChildThrows() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            testInitializeWithChildren();
        
        assertThrows(NoSuchElementException.class, 
            () -> results.getInitializer("unknown"),
            "Should throw for unknown child");
    }

    @Test
    void testResultsGetResultObject_UnknownChildThrows() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            testInitializeWithChildren();
        
        assertThrows(NoSuchElementException.class, 
            () -> results.getResultObject("unknown"),
            "Should throw for unknown child");
    }

    @Test
    void testResultsInitializerNames_Immutable() throws ConcurrentException {
        testInitializeWithChildren();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            initializer.get();
            
        final Iterator<String> iterator = results.initializerNames().iterator();
        iterator.next();
        
        assertThrows(UnsupportedOperationException.class, 
            iterator::remove,
            "Should prevent modification of initializer names");
    }

    @Test
    void testResultsIsException_UnknownChildThrows() throws ConcurrentException {
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = 
            testInitializeWithChildren();
        
        assertThrows(NoSuchElementException.class, 
            () -> results.isException("unknown"),
            "Should throw for unknown child");
    }
}
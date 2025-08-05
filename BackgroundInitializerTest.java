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

class BackgroundInitializerTest extends AbstractLangTest {

    /**
     * A simple counter that tracks initialization calls and can be closed.
     * Used as the result object for background initialization tests.
     */
    protected static class InitializationCounter {
        private final AtomicInteger initializeCalls = new AtomicInteger(0);
        private final AtomicBoolean closed = new AtomicBoolean(false);

        public InitializationCounter increment() {
            initializeCalls.incrementAndGet();
            return this;
        }

        public int getInitializationCount() {
            return initializeCalls.get();
        }

        public void close() {
            closed.set(true);
        }

        public boolean isClosed() {
            return closed.get();
        }
    }

    /**
     * Test implementation of BackgroundInitializer that provides control over:
     * - Exception throwing during initialization
     * - Sleep duration during initialization  
     * - Synchronization with test thread via latch
     * - Access to initialization result counter
     */
    protected static class TestableBackgroundInitializer extends BackgroundInitializer<InitializationCounter> {
        
        private final InitializationCounter counter = new InitializationCounter();
        private final CountDownLatch initializationLatch = new CountDownLatch(1);
        
        // Configuration options for test scenarios
        private Exception exceptionToThrow;
        private boolean shouldSleepDuringInitialization;
        private boolean shouldWaitForLatch;

        public TestableBackgroundInitializer() {
            super();
        }

        public TestableBackgroundInitializer(ExecutorService executorService) {
            super(executorService);
        }

        @Override
        protected InitializationCounter initialize() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            
            if (shouldSleepDuringInitialization) {
                ThreadUtils.sleep(Duration.ofMinutes(1));
            }
            
            if (shouldWaitForLatch) {
                initializationLatch.await();
            }
            
            return counter.increment();
        }

        // Test configuration methods
        public void throwExceptionDuringInitialization(Exception exception) {
            this.exceptionToThrow = exception;
        }

        public void sleepDuringInitialization() {
            this.shouldSleepDuringInitialization = true;
        }

        public void waitForLatchDuringInitialization() {
            this.shouldWaitForLatch = true;
        }

        public void releaseInitializationLatch() {
            initializationLatch.countDown();
        }

        public InitializationCounter getCounter() {
            return counter;
        }
    }

    // Factory methods for creating test instances
    protected TestableBackgroundInitializer createInitializer() {
        return new TestableBackgroundInitializer();
    }

    protected TestableBackgroundInitializer createInitializerWithExecutor(ExecutorService executorService) {
        return new TestableBackgroundInitializer(executorService);
    }

    /**
     * Verifies that initialization was called exactly once and completed successfully.
     */
    private void verifySuccessfulInitialization(TestableBackgroundInitializer initializer) throws ConcurrentException {
        InitializationCounter result = initializer.get();
        assertEquals(1, result.getInitializationCount(), "Initialize should be called exactly once");
        assertEquals(1, initializer.getCounter().getInitializationCount(), "Counter should show one initialization");
        assertNotNull(initializer.getFuture(), "Future should be available after start");
    }

    @Test
    void testBuilder_CreatesInitializerWithNullValues() throws ConcurrentException {
        BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(null)
            .get();

        assertNull(initializer.getExternalExecutor());
        assertFalse(initializer.isInitialized());
        assertFalse(initializer.isStarted());
        assertThrows(IllegalStateException.class, initializer::getFuture);
    }

    @Test
    void testBuilder_InitializerThrowsException() throws ConcurrentException {
        BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(() -> {
                throw new IllegalStateException("test");
            })
            .get();

        assertNull(initializer.getExternalExecutor());
        assertFalse(initializer.isInitialized());
        assertFalse(initializer.isStarted());
        assertThrows(IllegalStateException.class, initializer::getFuture);
        
        // After starting, the exception should be propagated
        initializer.start();
        IllegalStateException thrownException = assertThrows(IllegalStateException.class, initializer::get);
        assertEquals("test", thrownException.getMessage());
    }

    @Test
    void testGetActiveExecutor_BeforeStart_ReturnsNull() {
        TestableBackgroundInitializer initializer = createInitializer();
        assertNull(initializer.getActiveExecutor(), "Should not have an active executor before start");
    }

    @Test
    void testGetActiveExecutor_WithExternalExecutor_ReturnsSameExecutor() throws InterruptedException, ConcurrentException {
        ExecutorService externalExecutor = Executors.newSingleThreadExecutor();
        try {
            TestableBackgroundInitializer initializer = createInitializerWithExecutor(externalExecutor);
            initializer.start();
            
            assertSame(externalExecutor, initializer.getActiveExecutor(), "Should return the external executor");
            verifySuccessfulInitialization(initializer);
        } finally {
            externalExecutor.shutdown();
            externalExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testGetActiveExecutor_WithTemporaryExecutor_ReturnsCreatedExecutor() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.start();
        
        assertNotNull(initializer.getActiveExecutor(), "Should have an active executor after start");
        verifySuccessfulInitialization(initializer);
    }

    @Test
    void testGet_BeforeStart_ThrowsIllegalStateException() {
        TestableBackgroundInitializer initializer = createInitializer();
        assertThrows(IllegalStateException.class, initializer::get, 
                    "Should throw IllegalStateException when get() called before start()");
    }

    @Test
    void testGet_WhenInitializationThrowsCheckedException_WrapsInConcurrentException() {
        TestableBackgroundInitializer initializer = createInitializer();
        Exception checkedException = new Exception("Test checked exception");
        initializer.throwExceptionDuringInitialization(checkedException);
        initializer.start();
        
        ConcurrentException thrownException = assertThrows(ConcurrentException.class, initializer::get);
        assertEquals(checkedException, thrownException.getCause(), "Should wrap the original exception");
    }

    @Test
    void testGet_WhenInitializationThrowsRuntimeException_PropagatesException() {
        TestableBackgroundInitializer initializer = createInitializer();
        RuntimeException runtimeException = new RuntimeException("Test runtime exception");
        initializer.throwExceptionDuringInitialization(runtimeException);
        initializer.start();
        
        Exception thrownException = assertThrows(Exception.class, initializer::get);
        assertEquals(runtimeException, thrownException, "Should propagate runtime exception directly");
    }

    @Test
    void testGet_WhenThreadInterrupted_WrapsInterruptedException() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        TestableBackgroundInitializer initializer = createInitializerWithExecutor(executor);
        CountDownLatch testThreadLatch = new CountDownLatch(1);
        
        initializer.sleepDuringInitialization();
        initializer.start();
        
        AtomicReference<InterruptedException> caughtInterruptedException = new AtomicReference<>();
        Thread getterThread = new Thread(() -> {
            try {
                initializer.get();
            } catch (ConcurrentException ex) {
                if (ex.getCause() instanceof InterruptedException) {
                    caughtInterruptedException.set((InterruptedException) ex.getCause());
                }
            } finally {
                assertTrue(Thread.currentThread().isInterrupted(), "Thread should remain interrupted");
                testThreadLatch.countDown();
            }
        });
        
        getterThread.start();
        getterThread.interrupt();
        testThreadLatch.await();
        
        executor.shutdownNow();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        assertNotNull(caughtInterruptedException.get(), "Should catch InterruptedException");
    }

    @Test
    void testInitialize_CallsInitializeMethodOnce() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifySuccessfulInitialization(initializer);
    }

    @Test
    void testInitializeWithTemporaryExecutor_ShutsDownExecutorAfterCompletion() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        assertTrue(initializer.start(), "Start should return true on first call");
        verifySuccessfulInitialization(initializer);
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Temporary executor should be shut down");
    }

    @Test
    void testIsInitialized_BeforeAndAfterCompletion() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.waitForLatchDuringInitialization();
        initializer.start();
        
        assertTrue(initializer.isStarted(), "Should be started after start() call");
        assertFalse(initializer.isInitialized(), "Should not be initialized while waiting for latch");
        
        initializer.releaseInitializationLatch();
        initializer.get(); // Wait for completion
        
        assertTrue(initializer.isInitialized(), "Should be initialized after completion");
    }

    @Test
    void testIsStarted_AfterGet_RemainsTrue() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.start();
        verifySuccessfulInitialization(initializer);
        assertTrue(initializer.isStarted(), "Should remain started after get() completes");
    }

    @Test
    void testIsStarted_BeforeStart_ReturnsFalse() {
        TestableBackgroundInitializer initializer = createInitializer();
        assertFalse(initializer.isStarted(), "Should not be started before start() is called");
    }

    @Test
    void testIsStarted_AfterStart_ReturnsTrue() {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.start();
        assertTrue(initializer.isStarted(), "Should be started after start() is called");
    }

    @Test
    void testSetExternalExecutor_SetsAndUsesProvidedExecutor() throws ConcurrentException {
        ExecutorService externalExecutor = Executors.newCachedThreadPool();
        try {
            TestableBackgroundInitializer initializer = createInitializer();
            initializer.setExternalExecutor(externalExecutor);
            
            assertEquals(externalExecutor, initializer.getExternalExecutor(), 
                        "Should return the set external executor");
            assertTrue(initializer.start(), "Start should return true on first call");
            assertSame(externalExecutor, initializer.getActiveExecutor(), 
                      "Should use the external executor for background task");
            verifySuccessfulInitialization(initializer);
            assertFalse(externalExecutor.isShutdown(), 
                       "External executor should not be shut down by initializer");
        } finally {
            externalExecutor.shutdown();
        }
    }

    @Test
    void testSetExternalExecutor_AfterStart_ThrowsIllegalStateException() throws ConcurrentException, InterruptedException {
        TestableBackgroundInitializer initializer = createInitializer();
        initializer.start();
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalStateException.class, 
                        () -> initializer.setExternalExecutor(executor),
                        "Should throw IllegalStateException when setting executor after start");
            initializer.get(); // Ensure initialization completes
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    void testStart_MultipleCalls_OnlyFirstCallReturnsTrue() throws ConcurrentException {
        TestableBackgroundInitializer initializer = createInitializer();
        
        assertTrue(initializer.start(), "First start() call should return true");
        
        for (int i = 0; i < 10; i++) {
            assertFalse(initializer.start(), "Subsequent start() calls should return false");
        }
        
        verifySuccessfulInitialization(initializer);
    }
}
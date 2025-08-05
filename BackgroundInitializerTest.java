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
     * Test implementation of BackgroundInitializer with hooks for controlling test behavior.
     */
    protected static class AbstractBackgroundInitializerTestImpl extends
            BackgroundInitializer<CloseableCounter> {
        Exception exceptionToThrow;
        boolean shouldSleep;
        final CountDownLatch latch = new CountDownLatch(1);
        boolean waitForLatch;
        CloseableCounter counter = new CloseableCounter();

        AbstractBackgroundInitializerTestImpl() {}
        AbstractBackgroundInitializerTestImpl(ExecutorService exec) {
            super(exec);
        }

        public void enableLatch() {
            waitForLatch = true;
        }

        public CloseableCounter getCloseableCounter() {
            return counter;
        }

        protected CloseableCounter initializeInternal() throws Exception {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
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

    /**
     * Tracks initialization calls and closure state.
     */
    protected static class CloseableCounter {
        AtomicInteger initializeCalls = new AtomicInteger();
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
     * Concrete implementation that delegates to initializeInternal.
     */
    protected static class MethodBackgroundInitializerTestImpl extends AbstractBackgroundInitializerTestImpl {
        MethodBackgroundInitializerTestImpl() {}
        MethodBackgroundInitializerTestImpl(ExecutorService exec) {
            super(exec);
        }

        @Override
        protected CloseableCounter initialize() throws Exception {
            return initializeInternal();
        }
    }

    /**
     * Verifies initialize() was called exactly once.
     */
    private void verifyInitializationCalledOnce(AbstractBackgroundInitializerTestImpl init) 
            throws ConcurrentException {
        final int result = init.get().getInitializeCalls();
        assertEquals(1, result, "Initialize should be called once");
        assertEquals(1, init.getCloseableCounter().getInitializeCalls(), 
            "Counter should show one initialization call");
        assertNotNull(init.getFuture(), "Future should be available");
    }

    protected AbstractBackgroundInitializerTestImpl createBackgroundInitializer() {
        return new MethodBackgroundInitializerTestImpl();
    }

    protected AbstractBackgroundInitializerTestImpl createBackgroundInitializer(ExecutorService exec) {
        return new MethodBackgroundInitializerTestImpl(exec);
    }

    @Test
    void testBuilderWithNullParameters() {
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setCloser(null)
            .setExternalExecutor(null)
            .setInitializer(null)
            .get();
        
        assertNull(initializer.getExternalExecutor(), "External executor should be null");
        assertFalse(initializer.isInitialized(), "Should not be initialized");
        assertFalse(initializer.isStarted(), "Should not be started");
        assertThrows(IllegalStateException.class, initializer::getFuture);
    }

    @Test
    void testBuilderWithFailingInitializer() {
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setInitializer(() -> {
                throw new IllegalStateException("test");
            })
            .get();
        
        initializer.start();
        final Throwable exception = assertThrows(IllegalStateException.class, initializer::get);
        assertEquals("test", exception.getMessage(), "Correct exception message expected");
    }

    @Test
    void testGetActiveExecutorBeforeStart() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        assertNull(initializer.getActiveExecutor(), "Should not have executor before start");
    }

    @Test
    void testGetActiveExecutorWithExternalExecutor() throws InterruptedException, ConcurrentException {
        final ExecutorService externalExecutor = Executors.newSingleThreadExecutor();
        try {
            final AbstractBackgroundInitializerTestImpl initializer = 
                createBackgroundInitializer(externalExecutor);
            
            initializer.start();
            assertSame(externalExecutor, initializer.getActiveExecutor(), 
                "Should use external executor");
            verifyInitializationCalledOnce(initializer);
        } finally {
            externalExecutor.shutdown();
            assertTrue(externalExecutor.awaitTermination(1, TimeUnit.SECONDS), 
                "Executor should terminate");
        }
    }

    @Test
    void testGetActiveExecutorWithTemporaryExecutor() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        assertNotNull(initializer.getActiveExecutor(), "Temporary executor should be created");
        verifyInitializationCalledOnce(initializer);
    }

    @Test
    void testGetBeforeStartThrowsException() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        assertThrows(IllegalStateException.class, initializer::get, 
            "Should throw when get() called before start()");
    }

    @Test
    void testGetWhenInitializationThrowsCheckedException() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        final Exception testException = new Exception("Test checked exception");
        initializer.exceptionToThrow = testException;
        
        initializer.start();
        final ConcurrentException exception = assertThrows(ConcurrentException.class, initializer::get);
        assertEquals(testException, exception.getCause(), "Should wrap original exception");
    }

    @Test
    void testGetWhenInterrupted() throws InterruptedException {
        final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
        try {
            final AbstractBackgroundInitializerTestImpl initializer = 
                createBackgroundInitializer(backgroundExecutor);
            initializer.shouldSleep = true; // Force background task to sleep
            initializer.start();

            final CountDownLatch getCompleted = new CountDownLatch(1);
            final AtomicReference<InterruptedException> interruption = new AtomicReference<>();
            
            final Thread getThread = new Thread(() -> {
                try {
                    initializer.get();
                } catch (ConcurrentException e) {
                    if (e.getCause() instanceof InterruptedException) {
                        interruption.set((InterruptedException) e.getCause());
                    }
                } finally {
                    assertTrue(Thread.currentThread().isInterrupted(), 
                        "Thread should be interrupted");
                    getCompleted.countDown();
                }
            });
            
            getThread.start();
            getThread.interrupt(); // Interrupt the waiting thread
            assertTrue(getCompleted.await(5, TimeUnit.SECONDS), "Get should complete");
            
            assertNotNull(interruption.get(), "Should have InterruptedException");
        } finally {
            backgroundExecutor.shutdownNow();
            assertTrue(backgroundExecutor.awaitTermination(1, TimeUnit.SECONDS), 
                "Background executor should terminate");
        }
    }

    @Test
    void testGetWhenInitializationThrowsRuntimeException() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        final RuntimeException testException = new RuntimeException("Test runtime exception");
        initializer.exceptionToThrow = testException;
        
        initializer.start();
        final Exception exception = assertThrows(Exception.class, initializer::get);
        assertEquals(testException, exception, "Should throw original runtime exception");
    }

    @Test
    void testInitializeInBackground() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        verifyInitializationCalledOnce(initializer);
    }

    @Test
    void testTemporaryExecutorShutdownAfterInitialization() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        verifyInitializationCalledOnce(initializer);
        assertTrue(initializer.getActiveExecutor().isShutdown(), "Temporary executor should be shutdown");
    }

    @Test
    void testIsInitializedAfterBackgroundTaskCompletes() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.enableLatch(); // Control when initialization completes
        initializer.start();
        
        assertTrue(initializer.isStarted(), "Should be started");
        assertFalse(initializer.isInitialized(), "Should not be initialized before latch release");
        
        initializer.releaseLatch();
        initializer.get(); // Wait for completion
        
        assertTrue(initializer.isInitialized(), "Should be initialized after completion");
    }

    @Test
    void testIsStartedAfterInitialization() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        verifyInitializationCalledOnce(initializer);
        assertTrue(initializer.isStarted(), "Should remain started after initialization");
    }

    @Test
    void testIsStartedWhenNotStarted() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        assertFalse(initializer.isStarted(), "Should not be started before start() call");
    }

    @Test
    void testIsStartedWhenStarted() {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        assertTrue(initializer.isStarted(), "Should be started after start() call");
    }

    @Test
    void testSetExternalExecutorBeforeStart() throws InterruptedException, ConcurrentException {
        final ExecutorService externalExecutor = Executors.newCachedThreadPool();
        try {
            final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
            initializer.setExternalExecutor(externalExecutor);
            
            assertEquals(externalExecutor, initializer.getExternalExecutor(), 
                "Should store external executor");
            
            assertTrue(initializer.start(), "Start should succeed");
            assertSame(externalExecutor, initializer.getActiveExecutor(), 
                "Should use external executor");
            
            verifyInitializationCalledOnce(initializer);
            assertFalse(externalExecutor.isShutdown(), "External executor should remain active");
        } finally {
            externalExecutor.shutdown();
        }
    }

    @Test
    void testSetExternalExecutorAfterStartThrowsException() 
            throws ConcurrentException, InterruptedException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        initializer.start();
        
        final ExecutorService externalExecutor = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalStateException.class, 
                () -> initializer.setExternalExecutor(externalExecutor),
                "Should not allow setting executor after start()");
            
            initializer.get(); // Complete initialization
        } finally {
            externalExecutor.shutdown();
            assertTrue(externalExecutor.awaitTermination(1, TimeUnit.SECONDS), 
                "Executor should terminate");
        }
    }

    @Test
    void testStartCanOnlyBeCalledOnce() throws ConcurrentException {
        final AbstractBackgroundInitializerTestImpl initializer = createBackgroundInitializer();
        assertTrue(initializer.start(), "First start() should succeed");
        
        for (int i = 0; i < 10; i++) {
            assertFalse(initializer.start(), "Subsequent start() calls should fail");
        }
        
        verifyInitializationCalledOnce(initializer);
    }
}
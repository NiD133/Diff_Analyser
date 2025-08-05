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

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A test suite for {@link BackgroundInitializer}, refactored for clarity and maintainability.
 */
public class BackgroundInitializerTest {

    /**
     * A concrete implementation of BackgroundInitializer for testing purposes.
     * It allows simulating a long-running initialization and throwing exceptions.
     */
    private static class TestableBackgroundInitializer extends BackgroundInitializer<String> {
        private final CountDownLatch startSignal;
        private final Exception exceptionToThrow;
        private final String result;

        TestableBackgroundInitializer(String result) {
            this(null, result, null);
        }

        TestableBackgroundInitializer(Exception ex) {
            this(null, null, ex);
        }

        TestableBackgroundInitializer(CountDownLatch startSignal, String result, Exception ex) {
            this.startSignal = startSignal;
            this.result = result;
            this.exceptionToThrow = ex;
        }

        @Override
        protected String initialize() throws Exception {
            if (startSignal != null) {
                // Wait for a signal to proceed, simulating a long-running task
                startSignal.await(5, TimeUnit.SECONDS);
            }
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return result;
        }
    }

    /**
     * Tests the default state of the initializer before the start() method is called.
     */
    @Test
    public void testInitialStateBeforeStart() {
        // Arrange
        final BackgroundInitializer<String> initializer = new BackgroundInitializer<>();

        // Assert
        assertFalse("isStarted() should be false before start()", initializer.isStarted());
        assertFalse("isInitialized() should be false before start()", initializer.isInitialized());
        assertNull("Active executor should be null before start()", initializer.getActiveExecutor());
    }

    /**
     * Tests the basic lifecycle when using an internally created executor.
     */
    @Test
    public void testSuccessfulInitializationWithInternalExecutor() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer("Success");

        // Act
        final boolean started = initializer.start();
        final String result = initializer.get();

        // Assert
        assertTrue("start() should return true on first call", started);
        assertTrue("isStarted() should be true after start()", initializer.isStarted());
        assertTrue("isInitialized() should be true after get() completes", initializer.isInitialized());
        assertEquals("get() should return the initialized value", "Success", result);
        assertNotNull("Active executor should be created internally", initializer.getActiveExecutor());
    }

    /**
     * Tests initialization using a provided external executor service.
     */
    @Test
    public void testSuccessfulInitializationWithExternalExecutor() throws ConcurrentException {
        // Arrange
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer("Success");
        initializer.setExternalExecutor(executor);

        // Act
        final boolean started = initializer.start();
        final String result = initializer.get();

        // Assert
        assertTrue("start() should return true", started);
        assertEquals("get() should return the initialized value", "Success", result);
        assertSame("Active executor should be the one provided", executor, initializer.getActiveExecutor());
        assertFalse("External executor should not be shut down by initializer", executor.isShutdown());

        // Cleanup
        executor.shutdown();
    }

    /**
     * Verifies that subsequent calls to start() return false and do not affect the outcome.
     */
    @Test
    public void testSubsequentStartCallsReturnFalse() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer("Done");

        // Act
        final boolean firstStart = initializer.start();
        final boolean secondStart = initializer.start();
        final String result = initializer.get();

        // Assert
        assertTrue("First call to start() should return true", firstStart);
        assertFalse("Second call to start() should return false", secondStart);
        assertEquals("get() should still return the correct result", "Done", result);
    }

    /**
     * Tests that calling get() before start() throws an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetShouldThrowIllegalStateExceptionBeforeStart() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act
        initializer.get(); // Should throw
    }

    /**
     * Tests that get() blocks until the background task is complete.
     */
    @Test(timeout = 5000)
    public void testGetShouldBlockUntilInitializationCompletes() throws Exception {
        // Arrange
        final CountDownLatch completionSignal = new CountDownLatch(1);
        final TestableBackgroundInitializer initializer = new TestableBackgroundInitializer(completionSignal, "DelayedResult", null);

        initializer.start();
        assertFalse("Should not be initialized immediately after start", initializer.isInitialized());

        // Act: Signal the initializer to complete its work and then get the result
        completionSignal.countDown();
        final String result = initializer.get();

        // Assert
        assertEquals("DelayedResult", result);
        assertTrue("Should be initialized after get() returns", initializer.isInitialized());
    }

    /**
     * Tests that a checked exception during initialization is wrapped in a ConcurrentException.
     */
    @Test
    public void testGetShouldWrapCheckedExceptionInConcurrentException() {
        // Arrange
        final Exception checkedException = new Exception("Test Checked Exception");
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer(checkedException);
        initializer.start();

        // Act & Assert
        try {
            initializer.get();
            fail("Expected ConcurrentException to be thrown");
        } catch (final ConcurrentException e) {
            assertEquals("The cause of ConcurrentException should be the original exception",
                    checkedException, e.getCause());
        }
    }

    /**
     * Tests that a runtime exception during initialization is thrown directly, not wrapped.
     */
    @Test
    public void testGetShouldThrowRuntimeExceptionAsIs() {
        // Arrange
        final RuntimeException runtimeException = new IllegalStateException("Test Runtime Exception");
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer(runtimeException);
        initializer.start();

        // Act & Assert
        try {
            initializer.get();
            fail("Expected IllegalStateException to be thrown");
        } catch (final IllegalStateException e) {
            // Expected behavior
            assertEquals("The thrown exception should be the original runtime exception",
                    runtimeException, e);
        } catch (final ConcurrentException e) {
            fail("RuntimeExceptions should not be wrapped in ConcurrentException");
        }
    }

    /**
     * Tests that setExternalExecutor() throws an IllegalStateException if called after start().
     */
    @Test(expected = IllegalStateException.class)
    public void testSetExternalExecutorShouldThrowIllegalStateExceptionAfterStart() {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();
        initializer.start();

        // Act
        initializer.setExternalExecutor(Executors.newSingleThreadExecutor()); // Should throw
    }

    /**
     * Tests that getFuture() throws an IllegalStateException if called before start().
     */
    @Test(expected = IllegalStateException.class)
    public void testGetFutureShouldThrowIllegalStateExceptionBeforeStart() {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act
        initializer.getFuture(); // Should throw
    }

    /**
     * Tests that getFuture() returns a valid Future after start() has been called.
     */
    @Test
    public void testGetFutureReturnsValidFutureAfterStart() throws Exception {
        // Arrange
        final BackgroundInitializer<String> initializer = new TestableBackgroundInitializer("FutureResult");

        // Act
        initializer.start();
        final java.util.concurrent.Future<String> future = initializer.getFuture();

        // Assert
        assertNotNull("Future should not be null after start()", future);
        assertEquals("Future should complete with the correct result", "FutureResult", future.get());
    }

    /**
     * Tests that start() throws a RejectedExecutionException if the executor rejects the task.
     */
    @Test
    public void testStartShouldThrowWhenExecutorRejectsTask() {
        // Arrange
        final ExecutorService rejectingExecutor = Executors.newSingleThreadExecutor();
        rejectingExecutor.shutdown(); // Shutdown the executor so it rejects new tasks
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>(rejectingExecutor);

        // Act & Assert
        try {
            initializer.start();
            fail("Expected RejectedExecutionException");
        } catch (final RejectedExecutionException e) {
            // Expected behavior
            assertNotNull("Exception should not be null", e);
        }
    }

    /**
     * Verifies that the default initialize() method returns null.
     */
    @Test
    public void testDefaultInitializeReturnsNull() throws ConcurrentException {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act
        initializer.start();
        final Object result = initializer.get();

        // Assert
        assertNull("Default initialize() method should return null", result);
    }

    /**
     * Tests that getTaskCount() returns 1 by default.
     */
    @Test
    public void testGetTaskCountShouldReturnOneByDefault() {
        // Arrange
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Assert
        assertEquals("getTaskCount() should return 1 by default", 1, initializer.getTaskCount());
    }
}
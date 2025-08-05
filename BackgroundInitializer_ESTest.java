package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockException;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

/**
 * Test suite for BackgroundInitializer class.
 * Tests various scenarios including normal operation, error conditions,
 * and state management of background initialization tasks.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true,
                     resetStaticState = true, separateClassLoader = true)
public class BackgroundInitializer_ESTest extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that BackgroundInitializer throws RejectedExecutionException
     * when the provided ThreadPoolExecutor cannot create threads.
     *
     * This test verifies proper error handling when the underlying executor
     * service is misconfigured (mock ThreadFactory returns null threads).
     */
    @Test(timeout = 4000)
    public void testStartWithInvalidThreadFactory_ThrowsRejectedExecutionException() throws Throwable {
        // Arrange: Create a ThreadPoolExecutor with a mock ThreadFactory that returns null threads
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        SynchronousQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();
        ThreadFactory mockThreadFactory = mock(ThreadFactory.class, new ViolatedAssumptionAnswer());
        doReturn((Thread) null, (Thread) null).when(mockThreadFactory).newThread(any(Runnable.class));

        ThreadPoolExecutor faultyExecutor = new ThreadPoolExecutor(1, 1, 0L, timeUnit, workQueue, mockThreadFactory);
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>(faultyExecutor);

        // Act & Assert: Starting should throw RejectedExecutionException
        try {
            initializer.start();
            fail("Expected RejectedExecutionException when ThreadFactory returns null threads");
        } catch(RejectedExecutionException e) {
            // Verify the exception comes from the ThreadPoolExecutor's AbortPolicy
            verifyException("java.util.concurrent.ThreadPoolExecutor$AbortPolicy", e);
        }
    }

    /**
     * Tests the complete lifecycle of a BackgroundInitializer:
     * creation -> start -> get result -> check initialization status.
     *
     * Verifies that a successfully started initializer returns null result
     * (default behavior) and correctly reports its initialization status.
     */
    @Test(timeout = 4000)
    public void testSuccessfulInitializationLifecycle() throws Throwable {
        // Arrange: Create a new BackgroundInitializer
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();
        assertFalse("Initializer should not be started initially", initializer.isStarted());

        // Act: Start the initializer
        boolean startResult = initializer.start();
        assertTrue("Start should return true on first call", startResult);

        // Act: Get the result (should be null for default implementation)
        Delayed result = initializer.get();
        assertNull("Default initialize() method should return null", result);

        // Assert: Check final state
        boolean isInitialized = initializer.isInitialized();
        assertTrue("Initialization status should match start result", isInitialized == startResult);
        assertTrue("Initializer should be marked as initialized", isInitialized);
    }

    /**
     * Tests the timing of initialization status checks.
     *
     * Verifies that isInitialized() returns false immediately after start()
     * but before the background task completes.
     */
    @Test(timeout = 4000)
    public void testInitializationStatusBeforeCompletion() throws Throwable {
        // Arrange & Act
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();
        boolean startResult = initializer.start();
        boolean isInitializedImmediately = initializer.isInitialized();

        // Assert: Start should succeed but initialization may not be complete yet
        assertFalse("Start result and immediate initialization status should differ",
                   isInitializedImmediately == startResult);
        assertFalse("Initialization should not be complete immediately after start",
                   isInitializedImmediately);
    }

    /**
     * Tests multiple operations on a BackgroundInitializer with ForkJoinPool.
     *
     * This test exercises various methods to ensure they work correctly
     * with an external executor service.
     */
    @Test(timeout = 4000)
    public void testMultipleOperationsWithForkJoinPool() throws Throwable {
        // Arrange: Create initializers with different configurations
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Delayed> initializerWithPool = new BackgroundInitializer<Delayed>(commonPool);
        BackgroundInitializer<Exception> defaultInitializer = new BackgroundInitializer<Exception>();
        BackgroundInitializer<BackgroundInitializer<Exception>> nestedInitializer =
            new BackgroundInitializer<BackgroundInitializer<Exception>>();

        // Act: Perform various operations
        commonPool.getFactory(); // Verify pool is accessible
        initializerWithPool.start();
        initializerWithPool.getFuture(); // Should not throw after start()

        BackgroundInitializer.builder(); // Test static builder method
        defaultInitializer.getActiveExecutor(); // Should return null before start
        defaultInitializer.isInitialized(); // Should return false before start

        // Test exception handling
        ConcurrentException testException = new ConcurrentException();
        testException.getSuppressed();
        nestedInitializer.getTypedException(testException);
        nestedInitializer.setExternalExecutor(null); // Should be allowed before start

        // Assert: Verify expected states
        ExecutorService externalExecutor = defaultInitializer.getExternalExecutor();
        assertNull("External executor should be null when not set", externalExecutor);
    }

    /**
     * Tests setting an external executor and starting the initializer.
     *
     * Verifies that an external ForkJoinPool can be set and used successfully.
     */
    @Test(timeout = 4000)
    public void testSetExternalExecutorAndStart() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();
        ForkJoinPool externalPool = ForkJoinPool.commonPool();

        // Act
        initializer.setExternalExecutor(externalPool);
        boolean startResult = initializer.start();

        // Assert
        assertTrue("Start should succeed with external executor", startResult);
    }

    /**
     * Tests that multiple start() calls are handled correctly.
     *
     * Verifies that subsequent calls to start() after the first one
     * don't cause issues and that get() works properly.
     */
    @Test(timeout = 4000)
    public void testMultipleStartCalls() throws Throwable {
        // Arrange
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<Exception>();

        // Act: Call start multiple times
        initializer.start();
        initializer.start(); // Second call should be safe
        initializer.get(); // Should work after start

        // Assert
        assertTrue("Initializer should be marked as started", initializer.isStarted());
    }

    /**
     * Tests that setExternalExecutor throws IllegalStateException after start().
     *
     * Verifies the proper error handling when trying to change the executor
     * after initialization has begun.
     */
    @Test(timeout = 4000)
    public void testSetExternalExecutorAfterStart_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>(null);
        initializer.start();
        initializer.start(); // Multiple starts should be safe

        // Act & Assert: Setting executor after start should fail
        try {
            initializer.setExternalExecutor(null);
            fail("Expected IllegalStateException when setting executor after start");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
        }
    }

    /**
     * Tests that setExternalExecutor throws IllegalStateException after get().
     *
     * Verifies that once initialization is complete, the executor cannot be changed.
     */
    @Test(timeout = 4000)
    public void testSetExternalExecutorAfterGet_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();
        initializer.start();
        initializer.get(); // Complete initialization

        // Act & Assert: Setting executor after get should fail
        ForkJoinPool newPool = new ForkJoinPool();
        try {
            initializer.setExternalExecutor(newPool);
            fail("Expected IllegalStateException when setting executor after get");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
        }
    }

    /**
     * Tests getActiveExecutor() behavior before start().
     *
     * Verifies that getActiveExecutor() returns null when called
     * before the initializer is started.
     */
    @Test(timeout = 4000)
    public void testGetActiveExecutorBeforeStart_ReturnsNull() throws Throwable {
        // Arrange
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<Exception>();

        // Act & Assert
        ExecutorService activeExecutor = initializer.getActiveExecutor();
        assertNull("Active executor should be null before start", activeExecutor);
    }

    /**
     * Tests that getFuture() throws IllegalStateException before start().
     *
     * Verifies proper error handling when trying to access the Future
     * before initialization has begun.
     */
    @Test(timeout = 4000)
    public void testGetFutureBeforeStart_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();

        // Act & Assert
        try {
            initializer.getFuture();
            fail("Expected IllegalStateException when getting future before start");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    /**
     * Tests getFuture() with external executor before start().
     *
     * Verifies that even with an external executor set, getFuture()
     * still requires start() to be called first.
     */
    @Test(timeout = 4000)
    public void testGetFutureWithExternalExecutorBeforeStart_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        ForkJoinPool externalPool = new ForkJoinPool();
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<Object>(externalPool);

        // Act & Assert
        try {
            initializer.getFuture();
            fail("Expected IllegalStateException even with external executor");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    /**
     * Tests getTaskCount() method.
     *
     * Verifies that the default task count is 1, which represents
     * the single background initialization task.
     */
    @Test(timeout = 4000)
    public void testGetTaskCount_ReturnsOne() throws Throwable {
        // Arrange
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<Exception>();

        // Act & Assert
        int taskCount = initializer.getTaskCount();
        assertEquals("Default task count should be 1", 1, taskCount);
    }

    /**
     * Tests various method calls and exception handling scenarios.
     *
     * This test covers multiple edge cases and verifies that
     * initialize() can be called and exception handling works correctly.
     */
    @Test(timeout = 4000)
    public void testVariousMethodCalls() throws Throwable {
        // Arrange
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Object> poolInitializer = new BackgroundInitializer<Object>(commonPool);
        BackgroundInitializer<Delayed> defaultInitializer = new BackgroundInitializer<Delayed>();

        // Act: Test multiple getTaskCount calls
        poolInitializer.getTaskCount();
        poolInitializer.getTaskCount();

        // Test initialize method can be called directly
        poolInitializer.initialize();

        // Test exception creation scenarios
        MockException mockException = new MockException("Test exception message");
        StackTraceElement[] stackTrace = new StackTraceElement[4];
        stackTrace[0] = new StackTraceElement("TestClass", "testMethod", "TestFile.java", 0);
        stackTrace[1] = new StackTraceElement("AnotherClass", "anotherMethod", "AnotherFile.java", -2077);

        // Test invalid StackTraceElement creation
        try {
            new StackTraceElement(null, "", "", 1);
            fail("Expected NullPointerException for null declaring class");
        } catch(NullPointerException e) {
            verifyException("java.util.Objects", e);
            assertEquals("Declaring class is null", e.getMessage());
        }
    }

    /**
     * Tests getTypedException() method.
     *
     * Verifies that ConcurrentException can be properly converted
     * to a typed exception.
     */
    @Test(timeout = 4000)
    public void testGetTypedException() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();
        ConcurrentException concurrentException = new ConcurrentException();

        // Act
        Exception typedException = initializer.getTypedException(concurrentException);

        // Assert
        assertNotNull("Typed exception should not be null", typedException);
    }

    /**
     * Tests complex exception handling scenarios.
     *
     * Verifies proper handling of nested exceptions and various
     * error conditions while maintaining proper state.
     */
    @Test(timeout = 4000)
    public void testComplexExceptionHandling() throws Throwable {
        // Arrange
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Exception> poolInitializer = new BackgroundInitializer<Exception>(commonPool);
        BackgroundInitializer<Delayed> defaultInitializer = new BackgroundInitializer<Delayed>();

        // Act: Test various state queries
        commonPool.isTerminating();

        // Create nested exception scenario
        MockThrowable mockThrowable = new MockThrowable("Nested error");
        ConcurrentException nestedConcurrentException = new ConcurrentException(mockThrowable);
        Exception nestedException = defaultInitializer.getTypedException(nestedConcurrentException);

        poolInitializer.getTypedException(nestedException);
        poolInitializer.isInitialized();
        poolInitializer.isStarted();

        // Test get() before start() on different initializer
        BackgroundInitializer<Object> uninitializedInitializer = new BackgroundInitializer<Object>(commonPool);
        try {
            uninitializedInitializer.get();
            fail("Expected IllegalStateException when calling get() before start()");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    /**
     * Tests isInitialized() behavior during initialization lifecycle.
     *
     * Note: This test contains unstable assertions due to timing dependencies
     * in concurrent execution. The behavior may vary between test runs.
     */
    @Test(timeout = 4000)
    public void testIsInitializedTiming() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();

        // Act & Assert: Check initial state
        boolean initializedBeforeStart = initializer.isInitialized();
        // Note: Assertion marked as unstable in original code
        // assertFalse(initializedBeforeStart);

        initializer.start();
        boolean initializedAfterStart = initializer.isInitialized();

        // Note: These assertions are marked as unstable due to timing
        // assertTrue(initializer.isStarted());
        // assertTrue(initializedAfterStart);
    }

    /**
     * Tests the static builder() method.
     *
     * Verifies that the builder pattern is supported and returns
     * a non-null builder instance.
     */
    @Test(timeout = 4000)
    public void testBuilderMethod() throws Throwable {
        // Act
        BackgroundInitializer.Builder<BackgroundInitializer<Delayed>, Delayed> builder =
            BackgroundInitializer.builder();

        // Assert
        assertNotNull("Builder should not be null", builder);
    }

    /**
     * Tests get() method before start().
     *
     * Verifies that calling get() before start() throws
     * the appropriate IllegalStateException.
     */
    @Test(timeout = 4000)
    public void testGetBeforeStart_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<Delayed>();

        // Act & Assert
        try {
            initializer.get();
            fail("Expected IllegalStateException when calling get() before start()");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    /**
     * Tests get() method after close() but before start().
     *
     * Verifies that calling close() doesn't change the requirement
     * that start() must be called before get().
     */
    @Test(timeout = 4000)
    public void testGetAfterCloseButBeforeStart_ThrowsIllegalStateException() throws Throwable {
        // Arrange
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<Exception>();
        BackgroundInitializer.builder(); // Test builder method call
        initializer.close();

        // Act & Assert
        try {
            initializer.get();
            fail("Expected IllegalStateException when calling get() before start(), even after close()");
        } catch(IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
            assertEquals("start() must be called first!", e.getMessage());
        }
    }
}
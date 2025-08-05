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
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockException;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, 
useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class BackgroundInitializer_ESTest extends BackgroundInitializer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void start_WithThreadPoolThatRejectsTasks_ThrowsRejectedExecutionException() {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        ThreadFactory threadFactory = mock(ThreadFactory.class, new ViolatedAssumptionAnswer());
        doReturn(null, null).when(threadFactory).newThread(any(Runnable.class));
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, timeUnit, queue, threadFactory);
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>(executor);
        
        try {
            initializer.start();
            fail("Expected RejectedExecutionException");
        } catch (RejectedExecutionException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void get_AfterSuccessfulInitialization_ReturnsResult() throws Exception {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        Delayed result = initializer.get();
        assertNull(result);
        assertTrue(initializer.isInitialized());
    }

    @Test(timeout = 4000)
    public void isInitialized_BeforeInitializationComplete_ReturnsFalse() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        assertFalse(initializer.isInitialized());
    }

    @Test(timeout = 4000)
    public void setExternalExecutor_AfterStart_ThrowsIllegalStateException() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        ForkJoinPool executor = new ForkJoinPool();
        
        try {
            initializer.setExternalExecutor(executor);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Cannot set ExecutorService after start()!", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void getFuture_BeforeStart_ThrowsIllegalStateException() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        try {
            initializer.getFuture();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void get_BeforeStart_ThrowsIllegalStateException() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        try {
            initializer.get();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void getTaskCount_ReturnsOne() {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        assertEquals(1, initializer.getTaskCount());
    }

    @Test(timeout = 4000)
    public void getTypedException_WithConcurrentException_ReturnsSameException() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        ConcurrentException input = new ConcurrentException();
        Exception result = initializer.getTypedException(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void builder_CreatedSuccessfully() {
        assertNotNull(BackgroundInitializer.builder());
    }

    @Test(timeout = 4000)
    public void start_WhenCalledTwice_ReturnsFalseSecondTime() throws Exception {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        assertTrue(initializer.start());
        assertFalse(initializer.start());
        initializer.get(); // Should complete without error
    }

    @Test(timeout = 4000)
    public void getActiveExecutor_BeforeStart_ReturnsNull() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        assertNull(initializer.getActiveExecutor());
    }

    @Test(timeout = 4000)
    public void setExternalExecutor_BeforeStart_Succeeds() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        ForkJoinPool executor = ForkJoinPool.commonPool();
        initializer.setExternalExecutor(executor);
        initializer.start();
        assertTrue(initializer.isStarted());
    }

    @Test(timeout = 4000)
    public void isInitialized_AfterSuccessfulInitialization_ReturnsTrue() throws Exception {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        initializer.get(); // Wait for completion
        assertTrue(initializer.isInitialized());
    }

    @Test(timeout = 4000)
    public void get_AfterCloseWithoutStart_ThrowsIllegalStateException() {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        initializer.close();
        try {
            initializer.get();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("start() must be called first!", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void getTypedException_WithNestedException_ReturnsCorrectType() {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        MockThrowable cause = new MockThrowable("Test error");
        ConcurrentException exception = new ConcurrentException(cause);
        Exception result = initializer.getTypedException(exception);
        assertSame(exception, result);
    }
}
package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.concurrent.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class BackgroundInitializerTest extends BackgroundInitializerTestScaffolding {

    @Test(timeout = 4000)
    public void testRejectedExecutionException() throws Throwable {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        ThreadFactory mockThreadFactory = mock(ThreadFactory.class, new ViolatedAssumptionAnswer());
        doReturn(null, null).when(mockThreadFactory).newThread(any(Runnable.class));
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, timeUnit, queue, mockThreadFactory);
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>(executor);

        try {
            initializer.start();
            fail("Expecting exception: RejectedExecutionException");
        } catch (RejectedExecutionException e) {
            verifyException("java.util.concurrent.ThreadPoolExecutor$AbortPolicy", e);
        }
    }

    @Test(timeout = 4000)
    public void testBackgroundInitializerStartAndGet() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        assertFalse(initializer.isStarted());

        boolean started = initializer.start();
        assertTrue(started);

        Delayed result = initializer.get();
        assertNull(result);

        boolean initialized = initializer.isInitialized();
        assertEquals(started, initialized);
        assertTrue(initialized);
    }

    @Test(timeout = 4000)
    public void testBackgroundInitializerNotInitialized() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        boolean started = initializer.start();
        boolean initialized = initializer.isInitialized();

        assertFalse(initialized == started);
        assertFalse(initialized);
    }

    @Test(timeout = 4000)
    public void testForkJoinPoolInitialization() throws Throwable {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>(commonPool);
        initializer.start();
        initializer.getFuture();

        BackgroundInitializer<Exception> exceptionInitializer = new BackgroundInitializer<>();
        exceptionInitializer.getActiveExecutor();
        exceptionInitializer.isInitialized();

        BackgroundInitializer<BackgroundInitializer<Exception>> nestedInitializer = new BackgroundInitializer<>();
        ConcurrentException concurrentException = new ConcurrentException();
        nestedInitializer.getTypedException(concurrentException);
        nestedInitializer.setExternalExecutor(null);

        ExecutorService externalExecutor = exceptionInitializer.getExternalExecutor();
        assertNull(externalExecutor);
    }

    @Test(timeout = 4000)
    public void testExternalExecutorSetBeforeStart() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        initializer.setExternalExecutor(commonPool);

        boolean started = initializer.start();
        assertTrue(started);

        boolean shouldThrowIOExceptions = FileSystemHandling.shouldAllThrowIOExceptions();
        assertEquals(started, shouldThrowIOExceptions);
    }

    @Test(timeout = 4000)
    public void testMultipleStartCalls() throws Throwable {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        initializer.start();
        initializer.start();
        initializer.get();

        assertTrue(initializer.isStarted());
    }

    @Test(timeout = 4000)
    public void testSetExternalExecutorAfterStart() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        initializer.start();

        try {
            initializer.setExternalExecutor(null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetExternalExecutorAfterStartWithForkJoinPool() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        initializer.start();
        initializer.get();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            initializer.setExternalExecutor(forkJoinPool);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetActiveExecutorReturnsNull() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        BackgroundInitializer<Exception> exceptionInitializer = new BackgroundInitializer<>();
        
        ExecutorService activeExecutor = exceptionInitializer.getActiveExecutor();
        assertNull(activeExecutor);
    }

    @Test(timeout = 4000)
    public void testGetFutureBeforeStart() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        try {
            initializer.getFuture();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetFutureBeforeStartWithForkJoinPool() throws Throwable {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<>(forkJoinPool);

        try {
            initializer.getFuture();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetTaskCount() throws Throwable {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        int taskCount = initializer.getTaskCount();
        assertEquals(1, taskCount);
    }

    @Test(timeout = 4000)
    public void testInitializeWithMockException() throws Throwable {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<>(commonPool);
        initializer.getTaskCount();
        initializer.getTaskCount();

        BackgroundInitializer<Delayed> delayedInitializer = new BackgroundInitializer<>();
        MockException mockException = new MockException("Mock Exception");
        StackTraceElement[] stackTraceElements = new StackTraceElement[4];
        stackTraceElements[0] = new StackTraceElement("ClassName", "methodName", "fileName", 0);
        stackTraceElements[1] = new StackTraceElement("ClassName", "methodName", "fileName", -2077);
        initializer.initialize();

        try {
            new StackTraceElement(null, "", "", 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetTypedException() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        ConcurrentException concurrentException = new ConcurrentException();
        Exception typedException = initializer.getTypedException(concurrentException);
        assertNotNull(typedException);
    }

    @Test(timeout = 4000)
    public void testGetTypedExceptionWithMockThrowable() throws Throwable {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>(commonPool);
        BackgroundInitializer<Delayed> delayedInitializer = new BackgroundInitializer<>();
        
        MockThrowable mockThrowable = new MockThrowable("Mock Throwable");
        ConcurrentException concurrentException = new ConcurrentException(mockThrowable);
        Exception typedException = delayedInitializer.getTypedException(concurrentException);
        initializer.getTypedException(typedException);

        initializer.isInitialized();
        initializer.isStarted();

        BackgroundInitializer<Object> objectInitializer = new BackgroundInitializer<>(commonPool);
        try {
            objectInitializer.get();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsInitializedBeforeAndAfterStart() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        boolean initializedBeforeStart = initializer.isInitialized();
        initializer.start();
        boolean initializedAfterStart = initializer.isInitialized();

        // Unstable assertions
        // assertFalse(initializedBeforeStart);
        // assertTrue(initializer.isStarted());
        // assertTrue(initializedAfterStart);
    }

    @Test(timeout = 4000)
    public void testBuilderCreation() throws Throwable {
        BackgroundInitializer.Builder<BackgroundInitializer<Delayed>, Delayed> builder = BackgroundInitializer.builder();
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void testGetBeforeStart() throws Throwable {
        BackgroundInitializer<Delayed> initializer = new BackgroundInitializer<>();
        try {
            initializer.get();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseBeforeGet() throws Throwable {
        BackgroundInitializer<Exception> initializer = new BackgroundInitializer<>();
        BackgroundInitializer.builder();
        initializer.close();

        try {
            initializer.get();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }
}
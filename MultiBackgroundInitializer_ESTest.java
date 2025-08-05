package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.MultiBackgroundInitializer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockException;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MultiBackgroundInitializerTest extends MultiBackgroundInitializerTestScaffolding {

    @Test(timeout = 4000)
    public void testInitializationWithoutStart() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        assertFalse(initializer.isStarted());

        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();
        assertTrue(results.isSuccessful());
    }

    @Test(timeout = 4000)
    public void testAddInitializerAfterStartThrowsException() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        initializer.start();

        BackgroundInitializer<Object> backgroundInitializer = new BackgroundInitializer<>();
        try {
            initializer.addInitializer("test", backgroundInitializer);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetExceptionForNonExistentInitializer() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();

        try {
            results.getException("nonExistent");
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$MultiBackgroundInitializerResults", e);
        }
    }

    @Test(timeout = 4000)
    public void testSuccessfulInitializationWithExecutorService() throws Throwable {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer(executor);

        BackgroundInitializer<Object> backgroundInitializer = new BackgroundInitializer<>(executor);
        initializer.addInitializer("test", backgroundInitializer);

        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();
        assertTrue(results.isSuccessful());
    }

    @Test(timeout = 4000)
    public void testCloseBeforeInitialization() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        initializer.close();
        assertFalse(initializer.isStarted());
    }

    @Test(timeout = 4000)
    public void testInitializeTwiceThrowsException() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        initializer.initialize();

        try {
            initializer.initialize();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.lang3.concurrent.BackgroundInitializer", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullBackgroundInitializerThrowsException() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();

        try {
            initializer.addInitializer("test", null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullNameThrowsException() throws Throwable {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer(forkJoinPool);
        BackgroundInitializer<Object> backgroundInitializer = new BackgroundInitializer<>(forkJoinPool);

        try {
            initializer.addInitializer(null, backgroundInitializer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetResultObjectForNonExistentInitializer() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();

        try {
            results.getResultObject("nonExistent");
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.lang3.concurrent.MultiBackgroundInitializer$MultiBackgroundInitializerResults", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetTypedException() throws Throwable {
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        MockException mockException = new MockException("Test exception");

        Exception exception = initializer.getTypedException(mockException);
        assertNotNull(exception);
    }
}
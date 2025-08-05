package com.google.common.util.concurrent;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.*;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockThread;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class FuturesGetChecked_ESTest extends FuturesGetChecked_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIsCheckedException() throws Throwable {
        // Test if Exception is recognized as a checked exception
        Class<Exception> exceptionClass = Exception.class;
        assertTrue(FuturesGetChecked.isCheckedException(exceptionClass));
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithForkJoinTask() throws Throwable {
        // Test getChecked with a ForkJoinTask
        Callable<Exception> mockCallable = mock(Callable.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockCallable).call();
        ForkJoinTask<Exception> task = ForkJoinTask.adapt(mockCallable).fork();
        FuturesGetChecked.getChecked(task, Exception.class, 422L, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithCompletedFuture() throws Throwable {
        // Test getChecked with a completed CompletableFuture
        SQLException sqlException = new SQLException("8Ov");
        CompletableFuture<Exception> completedFuture = CompletableFuture.completedFuture(sqlException);
        FuturesGetChecked.getChecked(completedFuture, Exception.class);
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithSupplier() throws Throwable {
        // Test getChecked with a Supplier
        Supplier<Exception> mockSupplier = mock(Supplier.class, new ViolatedAssumptionAnswer());
        SQLInvalidAuthorizationSpecException sqlException = new SQLInvalidAuthorizationSpecException();
        doReturn(sqlException).when(mockSupplier).get();
        CompletableFuture<Exception> future = CompletableFuture.supplyAsync(mockSupplier);
        FuturesGetChecked.getChecked(FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE, future, Exception.class);
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithNullFutureAndTimeout() throws Throwable {
        // Test getChecked with a null Future and timeout
        try {
            FuturesGetChecked.getChecked(null, Exception.class, -4303L, TimeUnit.SECONDS);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.FuturesGetChecked", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithNullFuture() throws Throwable {
        // Test getChecked with a null Future
        try {
            FuturesGetChecked.getChecked(null, Exception.class);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.FuturesGetChecked", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithValidatorAndNullFuture() throws Throwable {
        // Test getChecked with a validator and null Future
        FuturesGetChecked.GetCheckedTypeValidator validator = FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator();
        try {
            FuturesGetChecked.getChecked(validator, null, Exception.class);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.FuturesGetChecked", e);
        }
    }

    @Test(timeout = 4000)
    public void testCheckExceptionClassValidityWithNull() throws Throwable {
        // Test checkExceptionClassValidity with a null class
        try {
            FuturesGetChecked.checkExceptionClassValidity(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetCheckedTypeValidatorHolderInstantiation() {
        // Test instantiation of GetCheckedTypeValidatorHolder
        new FuturesGetChecked.GetCheckedTypeValidatorHolder();
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithMockThread() throws Throwable {
        // Test getChecked with a MockThread
        MockThread mockThread = new MockThread(MockThread.currentThread(), "KEY");
        ForkJoinTask<Delayed> task = ForkJoinTask.adapt(mockThread, null);
        try {
            FuturesGetChecked.getChecked(task, Exception.class, 0L, TimeUnit.NANOSECONDS);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithSupplierAndNull() throws Throwable {
        // Test getChecked with a Supplier returning null
        Supplier<Class<Delayed>> mockSupplier = mock(Supplier.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockSupplier).get();
        CompletableFuture<Class<Delayed>> future = CompletableFuture.supplyAsync(mockSupplier);
        FuturesGetChecked.getChecked(future, Exception.class);
    }

    @Test(timeout = 4000)
    public void testGetCheckedWithForkJoinPool() throws Throwable {
        // Test getChecked with a ForkJoinPool
        FuturesGetChecked.GetCheckedTypeValidator validator = FuturesGetChecked.classValueValidator();
        ForkJoinPool pool = new ForkJoinPool(798, ForkJoinPool.defaultForkJoinWorkerThreadFactory, MockThread.getDefaultUncaughtExceptionHandler(), false);
        Callable<Class<Exception>> mockCallable = mock(Callable.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockCallable).call();
        ForkJoinTask<Class<Exception>> task = pool.submit(mockCallable);
        Class<Exception> result = FuturesGetChecked.getChecked(validator, task, Exception.class);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testCheckExceptionClassValidity() throws Throwable {
        // Test checkExceptionClassValidity with a valid class
        FuturesGetChecked.checkExceptionClassValidity(Exception.class);
    }

    @Test(timeout = 4000)
    public void testWeakSetValidator() {
        // Test weakSetValidator method
        assertNotNull(FuturesGetChecked.weakSetValidator());
    }
}
package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.util.concurrent.FuturesGetChecked;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.lang.MockThread;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class FuturesGetChecked_ESTest extends FuturesGetChecked_ESTest_scaffolding {

    private static final Class<Exception> EXCEPTION_CLASS = Exception.class;
    private static final long TEST_TIMEOUT = 4000;

    // Tests for exception validation
    @Test(timeout = TEST_TIMEOUT)
    public void isCheckedException_returnsTrueForExceptionClass() {
        FuturesGetChecked.isCheckedException(EXCEPTION_CLASS);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void checkExceptionClassValidity_validatesExceptionClassSuccessfully() {
        FuturesGetChecked.checkExceptionClassValidity(EXCEPTION_CLASS);
    }

    // Tests for validator functionality
    @Test(timeout = TEST_TIMEOUT)
    public void weakSetValidator_returnsInstanceWithoutException() {
        FuturesGetChecked.weakSetValidator();
    }

    // Tests for successful getChecked operations
    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_handlesCompletedFutureWithException() throws Throwable {
        SQLException exception = new SQLException("DB error");
        CompletableFuture<Exception> future = CompletableFuture.completedFuture(exception);
        FuturesGetChecked.getChecked(future, EXCEPTION_CLASS);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withValidatorHandlesAsyncException() throws Throwable {
        // Setup validator and async future
        FuturesGetChecked.GetCheckedTypeValidator validator = 
            FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
        SQLInvalidAuthorizationSpecException exception = 
            new SQLInvalidAuthorizationSpecException();
        Supplier<Exception> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(exception);
        CompletableFuture<Exception> future = CompletableFuture.supplyAsync(supplier);
        
        FuturesGetChecked.getChecked(validator, future, EXCEPTION_CLASS);
    }

    // Tests for null handling
    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withNullFutureThrowsNullPointerException() {
        try {
            FuturesGetChecked.getChecked(null, EXCEPTION_CLASS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withValidatorAndNullFutureThrowsNullPointerException() {
        FuturesGetChecked.GetCheckedTypeValidator validator = 
            FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator();
        try {
            FuturesGetChecked.getChecked(validator, null, EXCEPTION_CLASS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withNullFutureAndTimeoutThrowsNullPointerException() {
        try {
            FuturesGetChecked.getChecked(null, EXCEPTION_CLASS, -4303L, TimeUnit.SECONDS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Tests for exception class validation
    @Test(timeout = TEST_TIMEOUT)
    public void checkExceptionClassValidity_withNullClassThrowsNullPointerException() {
        try {
            FuturesGetChecked.checkExceptionClassValidity(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Tests for timeout behavior
    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withTimeoutAndImmediateExpirationThrowsException() throws Throwable {
        // Setup task that will time out immediately
        Thread thread = MockThread.currentThread();
        MockThread taskThread = new MockThread(thread, "TaskThread");
        ForkJoinTask<Delayed> task = ForkJoinTask.adapt(taskThread, null);
        
        try {
            FuturesGetChecked.getChecked(task, EXCEPTION_CLASS, 0L, TimeUnit.NANOSECONDS);
            fail("Expected Exception due to timeout");
        } catch (Exception e) {
            // Expected
        }
    }

    // Tests for null result handling
    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_handlesNullResultFromFuture() throws Throwable {
        Supplier<Class<Delayed>> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(null);
        CompletableFuture<Class<Delayed>> future = CompletableFuture.supplyAsync(supplier);
        FuturesGetChecked.getChecked(future, EXCEPTION_CLASS);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void getChecked_withValidatorHandlesNullResult() throws Throwable {
        // Setup validator and async task
        FuturesGetChecked.GetCheckedTypeValidator validator = 
            FuturesGetChecked.classValueValidator();
        ForkJoinPool pool = new ForkJoinPool();
        Callable<Class<Exception>> callable = mock(Callable.class);
        when(callable.call()).thenReturn(null);
        ForkJoinTask<Class<Exception>> task = pool.submit(callable);
        
        Class<Exception> result = FuturesGetChecked.getChecked(
            validator, task, EXCEPTION_CLASS);
        assertNull("Expected null result from future", result);
    }
}
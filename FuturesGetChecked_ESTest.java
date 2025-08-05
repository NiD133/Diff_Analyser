package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.util.concurrent.FuturesGetChecked;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Test suite for FuturesGetChecked utility class.
 * Tests the functionality of getting checked exceptions from Future objects.
 */
public class FuturesGetCheckedTest {

    @Test(timeout = 4000)
    public void isCheckedException_withExceptionClass_shouldValidateSuccessfully() throws Throwable {
        // Given
        Class<Exception> exceptionClass = Exception.class;
        
        // When & Then - should not throw any exception
        FuturesGetChecked.isCheckedException(exceptionClass);
    }

    @Test(timeout = 4000)
    public void getChecked_withForkJoinTaskAndTimeout_shouldReturnResult() throws Throwable {
        // Given
        Callable<Exception> mockCallable = mock(Callable.class);
        when(mockCallable.call()).thenReturn(null);
        
        ForkJoinTask<Exception> task = ForkJoinTask.adapt(mockCallable);
        ForkJoinTask<Exception> forkedTask = task.fork();
        
        Class<Exception> exceptionClass = Exception.class;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        long timeout = 422L;
        
        // When & Then - should complete without throwing exception
        FuturesGetChecked.getChecked(forkedTask, exceptionClass, timeout, timeUnit);
    }

    @Test(timeout = 4000)
    public void getChecked_withCompletedFuture_shouldReturnResult() throws Throwable {
        // Given
        SQLException sqlException = new SQLException("Test SQL Exception");
        CompletableFuture<Exception> completedFuture = CompletableFuture.completedFuture(sqlException);
        Class<Exception> exceptionClass = Exception.class;
        
        // When & Then - should complete without throwing exception
        FuturesGetChecked.getChecked(completedFuture, exceptionClass);
    }

    @Test(timeout = 4000)
    public void getChecked_withWeakSetValidatorAndAsyncSupplier_shouldReturnResult() throws Throwable {
        // Given
        FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator validator = 
            FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
        
        Class<Exception> exceptionClass = Exception.class;
        SQLInvalidAuthorizationSpecException authException = new SQLInvalidAuthorizationSpecException();
        
        Supplier<Exception> mockSupplier = mock(Supplier.class);
        when(mockSupplier.get()).thenReturn(authException);
        
        CompletableFuture<Exception> asyncFuture = CompletableFuture.supplyAsync(mockSupplier);
        
        // When & Then - should complete without throwing exception
        FuturesGetChecked.getChecked(validator, asyncFuture, exceptionClass);
    }

    @Test(timeout = 4000)
    public void getChecked_withNullFutureAndTimeout_shouldThrowNullPointerException() throws Throwable {
        // Given
        Class<Exception> exceptionClass = Exception.class;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        long negativeTimeout = -4303L;
        
        // When & Then
        try {
            FuturesGetChecked.getChecked(null, exceptionClass, negativeTimeout, timeUnit);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected behavior - null future should throw NPE
            assertTrue("Exception should be from FuturesGetChecked", 
                e.getStackTrace()[0].getClassName().contains("FuturesGetChecked"));
        }
    }

    @Test(timeout = 4000)
    public void getChecked_withNullFuture_shouldThrowNullPointerException() throws Throwable {
        // Given
        Class<Exception> exceptionClass = Exception.class;
        
        // When & Then
        try {
            FuturesGetChecked.getChecked(null, exceptionClass);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected behavior - null future should throw NPE
            assertTrue("Exception should be from FuturesGetChecked", 
                e.getStackTrace()[0].getClassName().contains("FuturesGetChecked"));
        }
    }

    @Test(timeout = 4000)
    public void getChecked_withValidatorAndNullFuture_shouldThrowNullPointerException() throws Throwable {
        // Given
        Class<Exception> exceptionClass = Exception.class;
        FuturesGetChecked.GetCheckedTypeValidator validator = 
            FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator();
        
        // When & Then
        try {
            FuturesGetChecked.getChecked(validator, null, exceptionClass);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected behavior - null future should throw NPE
            assertTrue("Exception should be from FuturesGetChecked", 
                e.getStackTrace()[0].getClassName().contains("FuturesGetChecked"));
        }
    }

    @Test(timeout = 4000)
    public void checkExceptionClassValidity_withNullClass_shouldThrowNullPointerException() throws Throwable {
        // When & Then
        try {
            FuturesGetChecked.checkExceptionClassValidity(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected behavior - null exception class should throw NPE
        }
    }

    @Test(timeout = 4000)
    public void getCheckedTypeValidatorHolder_canBeInstantiated() throws Throwable {
        // When & Then - should be able to create instance without exception
        new FuturesGetChecked.GetCheckedTypeValidatorHolder();
    }

    @Test(timeout = 4000)
    public void getChecked_withFailingTask_shouldThrowException() throws Throwable {
        // Given
        Class<Exception> exceptionClass = Exception.class;
        Runnable failingRunnable = () -> {
            throw new RuntimeException("Task failure");
        };
        
        ForkJoinTask<Object> failingTask = ForkJoinTask.adapt(failingRunnable, null);
        TimeUnit timeUnit = TimeUnit.NANOSECONDS;
        long immediateTimeout = 0L;
        
        // When & Then
        try {
            FuturesGetChecked.getChecked(failingTask, exceptionClass, immediateTimeout, timeUnit);
            fail("Expected Exception to be thrown");
        } catch (Exception e) {
            // Expected behavior - failing task should throw exception
        }
    }

    @Test(timeout = 4000)
    public void getChecked_withNullResultFuture_shouldReturnNull() throws Throwable {
        // Given
        Supplier<Object> nullSupplier = mock(Supplier.class);
        when(nullSupplier.get()).thenReturn(null);
        
        CompletableFuture<Object> nullResultFuture = CompletableFuture.supplyAsync(nullSupplier);
        Class<Exception> exceptionClass = Exception.class;
        
        // When
        Object result = FuturesGetChecked.getChecked(nullResultFuture, exceptionClass);
        
        // Then
        assertNull("Result should be null when future completes with null", result);
    }

    @Test(timeout = 4000)
    public void getChecked_withClassValueValidator_shouldReturnResult() throws Throwable {
        // Given
        FuturesGetChecked.GetCheckedTypeValidator validator = FuturesGetChecked.classValueValidator();
        
        Callable<Class<Exception>> mockCallable = mock(Callable.class);
        when(mockCallable.call()).thenReturn(null);
        
        ForkJoinTask<Class<Exception>> task = ForkJoinTask.adapt(mockCallable);
        Class<Exception> exceptionClass = Exception.class;
        
        // When
        Class<Exception> result = FuturesGetChecked.getChecked(validator, task, exceptionClass);
        
        // Then
        assertNull("Result should be null when callable returns null", result);
    }

    @Test(timeout = 4000)
    public void checkExceptionClassValidity_withValidExceptionClass_shouldComplete() throws Throwable {
        // Given
        Class<Exception> validExceptionClass = Exception.class;
        
        // When & Then - should complete without throwing exception
        FuturesGetChecked.checkExceptionClassValidity(validExceptionClass);
    }

    @Test(timeout = 4000)
    public void weakSetValidator_shouldReturnValidator() throws Throwable {
        // When & Then - should return validator without throwing exception
        FuturesGetChecked.weakSetValidator();
    }
}
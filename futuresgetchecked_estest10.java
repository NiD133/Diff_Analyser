package com.google.common.util.concurrent;

import org.junit.Test;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FuturesGetChecked_ESTestTest10 extends FuturesGetChecked_ESTest_scaffolding {

    /**
     * Tests that `getChecked` with a timeout throws the specified exception type
     * when the future does not complete within the given time. The cause of the
     * thrown exception should be a TimeoutException.
     */
    @Test(timeout = 4000)
    public void getCheckedWithTimeout_throwsSpecifiedExceptionWhenFutureTimesOut() {
        // Arrange: Create a Future that will never complete to simulate a timeout.
        // A ForkJoinTask adapted from a Runnable that is never executed serves this purpose.
        Runnable nonCompletingRunnable = () -> { /* This will never run */ };
        Future<Void> incompleteFuture = ForkJoinTask.adapt(nonCompletingRunnable, null);

        Class<Exception> exceptionTypeToThrow = Exception.class;
        long zeroTimeout = 0L;
        TimeUnit timeoutUnit = TimeUnit.NANOSECONDS;

        // Act & Assert
        try {
            FuturesGetChecked.getChecked(incompleteFuture, exceptionTypeToThrow, zeroTimeout, timeoutUnit);
            fail("Expected an Exception to be thrown due to timeout, but nothing was thrown.");
        } catch (Exception thrownException) {
            // Verify that the thrown exception is of the specified type.
            assertEquals(
                "The thrown exception should be of the specified type.",
                exceptionTypeToThrow,
                thrownException.getClass());

            // Verify that the cause of the exception is a TimeoutException.
            Throwable cause = thrownException.getCause();
            assertNotNull("The cause of the thrown exception should not be null.", cause);
            assertTrue(
                "The cause should be an instance of TimeoutException.",
                cause instanceof TimeoutException);
        }
    }
}
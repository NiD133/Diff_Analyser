package com.google.common.util.concurrent;

import static org.junit.Assert.assertNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Tests for {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Tests that getChecked returns null without throwing an exception when the input Future
     * is already successfully completed with a null value.
     */
    @Test
    public void getChecked_withCompletedFutureReturningNull_returnsNull() throws Exception {
        // Arrange: Create a future that is already successfully completed with a null result.
        // Using Futures.immediateFuture is much clearer than mocking a complex execution chain.
        Future<Object> completedFuture = Futures.immediateFuture(null);
        Class<Exception> exceptionType = Exception.class;

        // Act: Call the method under test with a short timeout.
        Object result = FuturesGetChecked.getChecked(completedFuture, exceptionType, 1, TimeUnit.SECONDS);

        // Assert: Verify that the method returns the null value from the future.
        assertNull("Expected getChecked to return the null result from the completed future.", result);
    }
}
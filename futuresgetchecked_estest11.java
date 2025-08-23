package com.google.common.util.concurrent;

import static org.junit.Assert.assertNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * Test suite for {@link FuturesGetChecked}.
 *
 * Note: The original test class name "FuturesGetChecked_ESTestTest11" was uninformative.
 * Renaming it to "FuturesGetCheckedTest" clarifies the subject of the test.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that getChecked returns null when called on a future that has
     * successfully completed with a null value.
     *
     * Note: The original test name "test10" was generic. The new name describes the specific
     * scenario being tested.
     */
    @Test
    public void getChecked_onCompletedFutureWithNull_returnsNull() throws Exception {
        // Arrange: Create a future that has already successfully completed with a null value.
        // This is simpler and more direct than the original's use of a mocked Supplier.
        Future<Object> successfullyCompletedFuture = CompletableFuture.completedFuture(null);
        Class<Exception> exceptionType = Exception.class;

        // Act: Call the method under test.
        Object result = FuturesGetChecked.getChecked(successfullyCompletedFuture, exceptionType);

        // Assert: The result should be null, as that was the future's value.
        // The original test lacked an explicit assertion, making its intent unclear.
        assertNull("Expected getChecked to return the null value from the completed future.", result);
    }
}
package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * Tests for {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that getChecked throws a NullPointerException when the provided Future is null.
     */
    @Test
    public void getChecked_withNullFuture_throwsNullPointerException() {
        // Arrange: Set up the inputs for the method call.
        // The specific validator and exception type are not critical for this test,
        // as the null check on the Future should happen first.
        GetCheckedTypeValidator validator =
                FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator();
        Class<Exception> exceptionType = Exception.class;
        Future<?> nullFuture = null;

        // Act & Assert: Call the method and verify that it throws the expected exception.
        assertThrows(
                NullPointerException.class,
                () -> FuturesGetChecked.getChecked(validator, nullFuture, exceptionType));
    }
}
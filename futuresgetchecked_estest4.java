package com.google.common.util.concurrent;

import org.junit.Test;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link FuturesGetChecked}.
 * This test class focuses on the behavior of the internal `getChecked` helper method.
 */
// The EvoSuite runner and scaffolding are kept as they are part of the existing test infrastructure.
// @RunWith(EvoRunner.class)
// public class FuturesGetChecked_ESTestTest4 extends FuturesGetChecked_ESTest_scaffolding {
public class FuturesGetChecked_ESTestTest4 {

    /**
     * Verifies that getChecked returns the future's result directly
     * when the future has already completed successfully.
     */
    @Test
    public void getChecked_whenFutureIsCompletedWithValue_returnsValue() throws Exception {
        // Arrange
        // 1. Obtain the validator instance used by the method under test.
        FuturesGetChecked.GetCheckedTypeValidator validator =
                FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;

        // 2. This is the exception type getChecked would wrap and throw if the future failed.
        //    In this success-path test, it is not used.
        Class<Exception> declaredExceptionType = Exception.class;

        // 3. Create the expected result value and a future that is already successfully
        //    completed with this value.
        SQLInvalidAuthorizationSpecException expectedResult = new SQLInvalidAuthorizationSpecException();
        Future<SQLInvalidAuthorizationSpecException> completedFuture = CompletableFuture.completedFuture(expectedResult);

        // Act
        // Call the method under test. We expect it to return the future's value without throwing.
        SQLInvalidAuthorizationSpecException actualResult =
                FuturesGetChecked.getChecked(validator, completedFuture, declaredExceptionType);

        // Assert
        // Verify that the method returned the exact same object that the future completed with.
        assertSame("getChecked should return the value from the successfully completed future.",
                expectedResult, actualResult);
    }
}
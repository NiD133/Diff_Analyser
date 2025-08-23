package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithPrivateConstructor;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Futures#getChecked(Future, Class)}, focusing on the validation of the provided
 * exception class.
 */
class FuturesGetCheckedTest {

  /**
   * Verifies that {@code getChecked} fails fast with an {@link IllegalArgumentException} when the
   * provided exception class does not have a public constructor that can be used for wrapping the
   * original cause.
   *
   * <p>The {@code getChecked} method needs to instantiate the given exception type if the future
   * fails. To do this, it requires the exception class to have a public constructor that accepts a
   * {@code Throwable} or a {@code String}. This test ensures that this precondition is enforced.
   */
  @Test
  void getChecked_whenExceptionTypeLacksPublicConstructor_throwsIllegalArgumentException() {
    // The @SuppressWarnings annotation is necessary because static analysis tools would
    // normally flag this as an invalid type to pass to getChecked. This test is specifically
    // verifying that this invalidity is handled correctly at runtime.
    @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));

    // The test's main goal is to verify that an IllegalArgumentException is thrown.
    // Further assertions on the exception message could be added for more robustness,
    // but verifying the exception type is often sufficient.
  }
}
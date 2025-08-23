package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithoutThrowableConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the exception-wrapping behavior of {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedExceptionWrappingTest {

  /**
   * Tests that getChecked can wrap a cause using {@code initCause()} as a fallback when the
   * target exception type does not have a constructor that accepts a {@code Throwable} cause.
   */
  @Test
  public void getChecked_whenExceptionTypeLacksCauseConstructor_wrapsOriginalCause() {
    // Arrange: A future that has failed with a known checked exception.
    // The target wrapper exception is specifically designed without a (Throwable) constructor.

    // Act: Call getChecked, expecting it to throw the wrapper exception.
    ExceptionWithoutThrowableConstructor thrown =
        assertThrows(
            ExceptionWithoutThrowableConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));

    // Assert: Verify that the original failure cause was attached to the new exception.
    // This confirms that getChecked successfully used the initCause() fallback mechanism.
    assertThat(thrown).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
    assertThat(thrown).hasMessageThat().contains(CHECKED_EXCEPTION.getMessage());
  }
}
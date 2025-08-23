package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithBadConstructor;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.Assert.assertThrows;

import junit.framework.TestCase;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 */
public class FuturesGetCheckedTestTest11 extends TestCase {

  /**
   * Tests that {@code getChecked} throws an {@code IllegalArgumentException} when the future has
   * failed, but the requested exception class does not have a constructor that takes a {@code
   * Throwable} cause. This is because {@code getChecked} cannot instantiate the custom exception
   * to wrap the original failure.
   */
  public void testGetChecked_whenExceptionTypeLacksCauseConstructor_throwsIllegalArgumentException() {
    // Act & Assert: Call getChecked with a failed future and an exception class that lacks a
    // constructor accepting a `Throwable` cause.
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class));

    // Assert that the error message clearly identifies the problematic exception class,
    // confirming that the operation failed for the expected reason.
    assertThat(thrown).hasMessageThat().contains(ExceptionWithBadConstructor.class.getName());
  }
}
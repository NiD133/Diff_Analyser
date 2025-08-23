package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithManyConstructors;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 */
class FuturesGetCheckedTest {

  /**
   * Tests that {@code getChecked} correctly wraps a future's cause in a new exception.
   * Specifically, it verifies that when the target exception class has multiple constructors,
   * {@code getChecked} prefers the constructor that accepts both a {@code String} message and a
   * {@code Throwable} cause. This ensures the resulting exception is as informative as possible.
   */
  @Test
  void getChecked_whenWrappingException_prefersConstructorWithBothStringAndCause() {
    // Arrange: The ExceptionWithManyConstructors class is designed so that only its
    // constructor with (String, Throwable) parameters sets the `usedExpectedConstructor` flag.
    // The input future has already failed with a checked exception.

    // Act: Call getChecked, which should catch the future's exception and wrap it.
    ExceptionWithManyConstructors thrown =
        assertThrows(
            ExceptionWithManyConstructors.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));

    // Assert: Verify that the most specific constructor (String, Throwable) was used.
    assertThat(thrown.usedExpectedConstructor).isTrue();
  }
}
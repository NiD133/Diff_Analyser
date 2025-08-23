package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithSomePrivateConstructors;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Futures#getChecked(Future, Class)}.
 */
class FuturesGetCheckedTest {

  @Test
  void getChecked_whenExceptionTypeHasPrivateConstructors_wrapsCauseInIt() {
    // Arrange: A future that is known to fail with a specific checked exception.
    ListenableFuture<?> failingFuture = FAILED_FUTURE_CHECKED_EXCEPTION;
    Class<ExceptionWithSomePrivateConstructors> wrapperExceptionType =
        ExceptionWithSomePrivateConstructors.class;

    // Act: Call getChecked, which is expected to wrap the original failure in the given type.
    ExceptionWithSomePrivateConstructors thrown =
        assertThrows(
            wrapperExceptionType,
            () -> getChecked(failingFuture, wrapperExceptionType));

    // Assert: Verify that the cause of the thrown exception is the original failure.
    assertThat(thrown).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }
}
package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_OTHER_THROWABLE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.OTHER_THROWABLE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.util.concurrent.Future;
import junit.framework.TestCase;

/**
 * Tests for the timed version of {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 */
public class FuturesGetCheckedTestTest19 extends TestCase {

  /**
   * Tests that when a future fails with an ExecutionException, getChecked wraps the underlying
   * cause in the specified exception type. This test specifically covers a cause that is a generic
   * {@link Throwable}.
   */
  public void testGetChecked_timed_whenFutureFailsWithThrowable_wrapsCauseInSpecifiedException() {
    // Arrange: FAILED_FUTURE_OTHER_THROWABLE is a future that has already failed with an
    // ExecutionException wrapping OTHER_THROWABLE. A zero timeout ensures the method
    // returns immediately.
    Future<Object> failedFuture = FAILED_FUTURE_OTHER_THROWABLE;
    Class<TwoArgConstructorException> exceptionToWrapIn = TwoArgConstructorException.class;

    // Act: Call getChecked on the failed future, expecting it to throw our custom exception.
    TwoArgConstructorException thrown =
        assertThrows(
            exceptionToWrapIn,
            () -> getChecked(failedFuture, exceptionToWrapIn, 0, SECONDS));

    // Assert: The exception thrown by getChecked should have the original throwable as its cause.
    assertThat(thrown).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }
}
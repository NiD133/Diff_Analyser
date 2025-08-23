package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.util.concurrent.Future;
import junit.framework.TestCase;

/**
 * Tests for {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 */
public class FuturesGetCheckedTest extends TestCase {

  /**
   * Tests that getChecked with a timeout, when called on a future that has already failed,
   * correctly wraps the original cause in the specified exception type.
   */
  public void testGetCheckedWithTimeout_whenFutureFails_wrapsCauseInSpecifiedException() {
    // Arrange: A future that has already failed with a known checked exception.
    // The timeout is set to 0 to ensure the method doesn't block.
    Future<String> failedFuture = FAILED_FUTURE_CHECKED_EXCEPTION;
    Exception originalCause = CHECKED_EXCEPTION;

    // Act: Call getChecked and expect it to throw our custom exception type.
    TwoArgConstructorException thrownException =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(failedFuture, TwoArgConstructorException.class, 0, SECONDS));

    // Assert: Verify that the thrown exception has the original exception as its cause.
    assertThat(thrownException).hasCauseThat().isEqualTo(originalCause);
  }
}
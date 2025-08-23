package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;

/**
 * Tests for the timeout behavior of {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 */
public class FuturesGetCheckedTimedExceptionTest extends TestCase {

  /**
   * Tests that when a timed `getChecked` call times out, it throws the specified exception type
   * with a {@link TimeoutException} as its cause.
   */
  public void testGetChecked_withTimeout_whenFutureDoesNotComplete_wrapsTimeoutException() {
    // Arrange: Create a future that will never complete to force a timeout.
    SettableFuture<String> incompleteFuture = SettableFuture.create();

    // Act: Call getChecked with a zero timeout, which should fail immediately.
    // We expect it to throw the specified custom exception type.
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(incompleteFuture, TwoArgConstructorException.class, 0, SECONDS));

    // Assert: Verify that the thrown exception was caused by a TimeoutException,
    // as per the contract of the timed getChecked method.
    assertThat(thrown).hasCauseThat().isInstanceOf(TimeoutException.class);
  }
}
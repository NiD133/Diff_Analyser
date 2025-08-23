package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithBadConstructor;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the exception-handling behavior of {@link Futures#getChecked(Future, Class)},
 * specifically focusing on the validation of the provided exception type's constructors.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedConstructorTest {

  /**
   * Tests that {@code getChecked} validates the provided exception type's constructors eagerly.
   *
   * <p>If the exception type doesn't have a constructor that {@code getChecked} can use (e.g., one
   * that accepts a {@code Throwable} cause), it should fail immediately with an {@code
   * IllegalArgumentException}. This validation occurs even if the future is already successful, in
   * which case the exception would never need to be constructed.
   */
  @Test
  public void getChecked_invalidExceptionType_throwsIllegalArgumentExceptionEvenForSuccessfulFuture() {
    // Arrange: Create a future that has already completed successfully, and identify an
    // exception type that lacks a constructor usable by getChecked.
    Future<String> successfulFuture = immediateFuture("a successful result");
    Class<ExceptionWithBadConstructor> invalidExceptionType = ExceptionWithBadConstructor.class;

    // Act & Assert: Verify that getChecked fails fast when the exception class is invalid,
    // without waiting for or depending on the future's result.
    assertThrows(
        IllegalArgumentException.class, () -> getChecked(successfulFuture, invalidExceptionType));
  }
}
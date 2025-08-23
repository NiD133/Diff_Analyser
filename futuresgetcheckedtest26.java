package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorRuntimeException;
import static org.junit.Assert.assertThrows;

import java.util.concurrent.Future;
import junit.framework.TestCase;

/**
 * Tests for {@link Futures#getChecked(Future, Class)}, focusing on the validation of the exception
 * type parameter.
 */
public class FuturesGetCheckedTest extends TestCase {

  /**
   * The getChecked method is designed to wrap causes in *checked* exceptions. This test verifies
   * that it throws an IllegalArgumentException if the specified exception type is a
   * RuntimeException (an *unchecked* exception), as this violates the method's contract.
   */
  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType") // Intentionally testing illegal usage
  public void testGetChecked_whenExceptionTypeIsRuntimeException_throwsIllegalArgumentException() {
    // Arrange
    Future<?> failedFuture = FAILED_FUTURE_CHECKED_EXCEPTION;
    Class<TwoArgConstructorRuntimeException> invalidExceptionType =
        TwoArgConstructorRuntimeException.class;

    // Act & Assert
    IllegalArgumentException thrown =
        assertThrows(
            "getChecked should reject RuntimeException types",
            IllegalArgumentException.class,
            () -> getChecked(failedFuture, invalidExceptionType));

    assertThat(thrown).hasMessageThat().contains("must not be a RuntimeException");
  }
}
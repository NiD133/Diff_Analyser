package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithManyConstructorsButOnlyOneThrowable;
import java.util.concurrent.Future;
import junit.framework.TestCase;

/**
 * Tests for the constructor-selection logic within {@link Futures#getChecked(Future, Class)}.
 */
public class FuturesGetCheckedTest extends TestCase {

  /**
   * Tests that getChecked selects the most specific constructor available (one with a String message
   * and a Throwable cause) when wrapping a future's exception.
   */
  public void testGetChecked_whenWrappingException_prefersConstructorWithBothMessageAndCause() {
    // Arrange: Define the custom exception type getChecked should instantiate.
    // This specific exception class has multiple constructors. The test verifies that
    // getChecked can find and use the one that preserves the most information.
    Class<ExceptionWithManyConstructorsButOnlyOneThrowable> wrapperExceptionType =
        ExceptionWithManyConstructorsButOnlyOneThrowable.class;

    // Act: Call getChecked on a future that has failed with a known cause.
    // We expect it to throw an instance of our custom wrapper exception.
    ExceptionWithManyConstructorsButOnlyOneThrowable thrownException =
        assertThrows(
            wrapperExceptionType,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, wrapperExceptionType));

    // Assert: Verify the thrown exception was created correctly.
    // The original exception cause should be preserved in the 'antecedent' field.
    assertThat(thrownException.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
    // The message indicates that a constructor accepting a String was also used.
    assertThat(thrownException).hasMessageThat().contains("mymessage");
  }
}
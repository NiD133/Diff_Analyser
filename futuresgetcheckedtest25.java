package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithGoodAndBadConstructor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithGoodAndBadConstructor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;

/**
 * Tests for {@link Futures#getChecked(Future, Class, long, TimeUnit)} focusing on exception
 * constructor selection.
 */
public class FuturesGetCheckedTestTest25 extends TestCase {

  /**
   * Tests that getChecked correctly selects a constructor that accepts a Throwable cause, even when
   * other unsuitable constructors are present.
   */
  public void testGetChecked_whenExceptionHasSuitableAndUnsuitableConstructors_usesSuitableOne() {
    // Arrange: A future that has already failed with a known checked exception. The custom
    // exception type we want to wrap it in has multiple constructors, but only one is suitable
    // for getChecked (i.e., it accepts a Throwable cause).
    Future<String> failedFuture = FAILED_FUTURE_CHECKED_EXCEPTION;
    Class<ExceptionWithGoodAndBadConstructor> wrapperExceptionType =
        ExceptionWithGoodAndBadConstructor.class;

    // Act: Call getChecked, which should find the correct constructor, create an instance of the
    // wrapper exception, and throw it.
    ExceptionWithGoodAndBadConstructor thrown =
        assertThrows(
            wrapperExceptionType,
            () -> getChecked(failedFuture, wrapperExceptionType, 1, SECONDS));

    // Assert: The thrown exception's cause should be the original exception from the future.
    // This confirms that getChecked successfully used the constructor that accepts a Throwable.
    assertThat(thrown).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }
}
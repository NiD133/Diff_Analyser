package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithBadConstructor;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTest {

  /**
   * Verifies that {@code getChecked} validates the provided exception type eagerly. If the
   * exception type is invalid (e.g., lacks a suitable constructor), the method should immediately
   * throw an {@link IllegalArgumentException}, even if the input {@link Future} has already
   * completed successfully.
   */
  @Test
  public void getChecked_withInvalidExceptionType_throwsIllegalArgumentExceptionForSuccessfulFuture() {
    // Arrange: Create a successfully completed future and an invalid exception type.
    // ExceptionWithBadConstructor is a type that getChecked cannot instantiate.
    Future<String> successfulFuture = immediateFuture("a successful result");
    Class<ExceptionWithBadConstructor> invalidExceptionType = ExceptionWithBadConstructor.class;

    // Act & Assert: Verify that calling getChecked with these inputs throws the expected exception
    // because the validation of the exception type fails before the future's result is checked.
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(successfulFuture, invalidExceptionType, 1, SECONDS));
  }
}
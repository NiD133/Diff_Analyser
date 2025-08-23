package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_ERROR;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.util.concurrent.ExecutionError;
import org.junit.Test;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)} that focus on how it
 * handles a {@code Future} that fails with an {@link Error}.
 */
public class FuturesGetCheckedErrorHandlingTest {

  /**
   * Verifies that when the input future fails with an {@link Error}, {@code getChecked} wraps it in
   * an {@link ExecutionError} rather than the declared exception type. The original {@code Error}
   * should be preserved as the cause.
   */
  @Test
  public void getChecked_whenFutureFailsWithError_throwsExecutionError()
      throws TwoArgConstructorException {
    // The signature of getChecked is `getChecked(Future<V>, Class<X>) throws X`.
    // The compiler thus requires this method to declare that it throws X
    // (TwoArgConstructorException), even though this test asserts that a
    // different exception (ExecutionError) is thrown for this specific input.

    ExecutionError thrown =
        assertThrows(
            "getChecked should wrap a causal Error in an ExecutionError",
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));

    assertThat(thrown).hasCauseThat().isEqualTo(ERROR);
  }
}
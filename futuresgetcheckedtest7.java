package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_OTHER_THROWABLE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.OTHER_THROWABLE;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(Future, Class)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTest {

  @Test
  public void getChecked_whenFutureFailsWithGenericThrowable_wrapsItInSpecifiedException() {
    // Arrange: A future that has failed with a generic Throwable. The getChecked method
    // has special handling for causes that are not an Error or RuntimeException.
    // The test constants FAILED_FUTURE_OTHER_THROWABLE and OTHER_THROWABLE set up this scenario.

    // Act: Call getChecked, which should catch the future's failure and wrap it.
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));

    // Assert: The thrown exception's cause should be the original Throwable from the future.
    assertThat(thrown).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }
}
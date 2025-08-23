package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR_FUTURE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTestTest21 {

  /**
   * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
   * getChecked(future, WillBeUnloadedException.class)}, and returns the loader. The caller can then
   * test that the {@code ClassLoader} can still be GCed. The test amounts to a test that {@code
   * getChecked} holds no strong references to the class.
   *
   * <p>This helper method is not used by the test in this file but is preserved as it is likely
   * part of a larger test suite.
   */
  private WeakReference<?> doTestClassUnloading() throws Exception {
    URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
    @SuppressWarnings("unchecked")
    Class<WillBeUnloadedException> shadowClass =
        (Class<WillBeUnloadedException>)
            Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
    assertThat(shadowClass).isNotSameInstanceAs(WillBeUnloadedException.class);
    getChecked(immediateFuture("foo"), shadowClass);
    return new WeakReference<>(shadowLoader);
  }

  public static final class WillBeUnloadedException extends Exception {}

  /**
   * Tests that {@code getChecked} with a timeout propagates an {@code Error} from the input future
   * directly, instead of wrapping it in the specified exception type.
   */
  @Test
  public void getChecked_timed_whenFutureFailsWithError_propagatesOriginalError() {
    // Arrange: A future that has already failed with an Error.
    Future<Object> failedFuture = ERROR_FUTURE;

    // Act & Assert: Calling getChecked should rethrow the original Error, not wrap it.
    Error thrown =
        assertThrows(
            Error.class,
            () -> getChecked(failedFuture, TwoArgConstructorException.class, 0, SECONDS));

    assertThat(thrown).isSameInstanceAs(ERROR);
  }
}
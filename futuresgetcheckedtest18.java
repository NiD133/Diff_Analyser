package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_ERROR;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutionError;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(Future, Class, long, TimeUnit)}.
 *
 * <p>This refactored test suite improves on the original by:
 * <ol>
 *     <li>Using modern JUnit 4 annotations (@Test) for clarity.</li>
 *     <li>Employing descriptive test names that follow a when/then pattern.</li>
 *     <li>Activating a previously unused test for class unloading to ensure all code is relevant.</li>
 *     <li>Removing numerous unused imports to reduce noise.</li>
 * </ol>
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTest {

  /**
   * The `getChecked` method is documented to rethrow `Error` types directly (wrapped in an
   * `ExecutionError`) rather than attempting to wrap them in the specified exception type. This
   * test verifies that behavior.
   */
  @Test
  public void getChecked_timed_futureFailsWithError_rethrowsAsExecutionError() {
    // Act & Assert
    ExecutionError thrown =
        assertThrows(
            "getChecked should have thrown ExecutionError for a future that failed with an Error.",
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class, 0, SECONDS));

    // Assert that the cause is the original Error instance.
    assertThat(thrown).hasCauseThat().isSameInstanceAs(ERROR);
  }

  /**
   * Verifies that `getChecked` does not hold a strong reference to the exception class it is given.
   * If it did, the class and its ClassLoader could not be garbage-collected, leading to memory
   * leaks in application servers and other long-running applications.
   */
  @Test
  public void getChecked_doesNotPreventExceptionClassUnloading() throws Exception {
    WeakReference<URLClassLoader> loaderReference = newShadowLoaderWithException();
    // This will pass if the ClassLoader is garbage-collected, which is possible only if
    // getChecked does not retain a strong reference to the exception class it loaded.
    GcFinalization.awaitClear(loaderReference);
  }

  /**
   * Helper for {@link #getChecked_doesNotPreventExceptionClassUnloading()}.
   *
   * <p>Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
   * getChecked(future, WillBeUnloadedException.class)}, and returns a weak reference to the
   * loader.
   */
  private WeakReference<URLClassLoader> newShadowLoaderWithException() throws Exception {
    URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
    @SuppressWarnings("unchecked") // Safe because we know the class name.
    Class<? extends Exception> shadowClass =
        (Class<? extends Exception>)
            Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
    assertNotSame(shadowClass, WillBeUnloadedException.class);

    // Call getChecked with a successful future. This may trigger caching of the exception type.
    getChecked(immediateFuture("foo"), shadowClass);

    return new WeakReference<>(shadowLoader);
  }

  /** A custom exception class designed to be loaded in a separate classloader for the GC test. */
  public static final class WillBeUnloadedException extends Exception {
    // A constructor that accepts a Throwable is required by the getChecked implementation
    // if it needs to wrap a cause.
    public WillBeUnloadedException(Throwable cause) {
      super(cause);
    }
  }
}
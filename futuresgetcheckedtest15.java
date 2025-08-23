package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import java.util.concurrent.CancellationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class, long,
 * java.util.concurrent.TimeUnit)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTest {

  /**
   * A simple exception class used for the ClassLoader unloading test. It must be public to be
   * accessible for loading by a different ClassLoader.
   */
  public static final class ExceptionToUnload extends Exception {}

  /**
   * Verifies that getChecked on a cancelled future immediately throws a CancellationException.
   */
  @Test
  public void getChecked_withTimeout_onCancelledFuture_throwsCancellationException()
      throws TwoArgConstructorException {
    // Arrange
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true); // Mark the future as cancelled

    // Act & Assert
    // getChecked should propagate the CancellationException immediately,
    // regardless of the specified exception type to map to.
    assertThrows(
        CancellationException.class,
        () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
  }

  /**
   * Verifies that getChecked does not hold a strong reference to the exception class it's passed.
   * If it did, the ClassLoader that loaded the class could not be garbage collected, resulting in a
   * memory leak.
   */
  @Test
  public void getChecked_doesNotPreventClassLoaderUnloading() throws Exception {
    // Arrange: Set up the test in a temporary classloader and get a weak reference to it.
    WeakReference<URLClassLoader> loaderReference = setupAndRunClassUnloadingTest();

    // Act & Assert: Force garbage collection and assert that the weak reference is cleared,
    // proving the ClassLoader was successfully unloaded.
    GcFinalization.awaitClear(loaderReference);
  }

  /**
   * Sets up the class unloading test scenario.
   *
   * <p>It creates a new ClassLoader, loads a custom exception class within it, and then calls
   * {@code getChecked} with that class.
   *
   * @return a {@link WeakReference} to the temporary ClassLoader, which can be used to check if it
   *     has been garbage collected.
   */
  private WeakReference<URLClassLoader> setupAndRunClassUnloadingTest() throws Exception {
    // Create a new, isolated ClassLoader.
    URLClassLoader tempClassLoader = new URLClassLoader(parseJavaClassPath(), null);

    // Load the exception class using the temporary ClassLoader.
    @SuppressWarnings("unchecked")
    Class<ExceptionToUnload> exceptionClassInTempLoader =
        (Class<ExceptionToUnload>)
            Class.forName(ExceptionToUnload.class.getName(), false, tempClassLoader);

    // Sanity check: ensure the class from the temp loader is distinct from the one in the
    // system loader.
    assertNotSame(
        "The exception class should be loaded by the temporary ClassLoader",
        exceptionClassInTempLoader,
        ExceptionToUnload.class);

    // Act: Call getChecked with a successful future and the dynamically loaded exception class.
    // The goal is to ensure that getChecked doesn't cache a strong reference to the class,
    // which would prevent its ClassLoader from being garbage collected.
    // Since the future succeeds, getChecked will just return the value "foo" and no
    // exception will be thrown.
    getChecked(immediateFuture("foo"), exceptionClassInTempLoader);

    return new WeakReference<>(tempClassLoader);
  }
}
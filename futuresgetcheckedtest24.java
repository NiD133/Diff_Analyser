package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithBadConstructor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class, long,
 * java.util.concurrent.TimeUnit)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTest {

  /** An exception class to be loaded in a separate ClassLoader for the memory leak test. */
  public static final class WillBeUnloadedException extends Exception {}

  /**
   * Tests that `getChecked` validates the provided exception class. When the class lacks a usable
   * constructor (e.g., one that accepts a `Throwable` cause), `getChecked` should fail fast with an
   * {@link IllegalArgumentException} rather than attempting to wrap the future's cause.
   */
  @Test
  public void getChecked_withTimeout_whenExceptionTypeHasNoValidConstructor_throwsIllegalArgumentException() {
    // FAILED_FUTURE_CHECKED_EXCEPTION is a future that has failed with a checked exception.
    // ExceptionWithBadConstructor is a custom exception type that does not have a constructor
    // suitable for wrapping the original cause.
    assertThrows(
        IllegalArgumentException.class,
        () ->
            getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION,
                ExceptionWithBadConstructor.class,
                1,
                SECONDS));
  }

  /**
   * Verifies that `getChecked` does not hold strong references to the exception class it is given.
   * This prevents memory leaks by ensuring the class's ClassLoader can be garbage collected.
   */
  @Test
  public void getChecked_doesNotPreventClassLoaderUnloading() throws Exception {
    // Arrange: Load an exception class in a custom loader and get a weak reference to the loader.
    WeakReference<ClassLoader> loaderReference = loadClassAndCallGetCheckedInCustomLoader();

    // Act & Assert: Force garbage collection and confirm the loader was unloaded.
    // GcFinalization.awaitClear() will throw an exception if the reference is not cleared,
    // failing the test.
    GcFinalization.awaitClear(loaderReference);
  }

  /**
   * Creates a new ClassLoader, loads {@link WillBeUnloadedException} within it, calls {@code
   * getChecked} with that class, and returns a weak reference to the loader.
   */
  private WeakReference<ClassLoader> loadClassAndCallGetCheckedInCustomLoader() throws Exception {
    URLClassLoader customLoader = new URLClassLoader(parseJavaClassPath(), null);
    @SuppressWarnings("unchecked") // Safe cast: we know the class name corresponds to an Exception.
    Class<? extends Exception> exceptionClassInLoader =
        (Class<? extends Exception>)
            Class.forName(WillBeUnloadedException.class.getName(), false, customLoader);

    // Sanity check to ensure the class was loaded by our custom loader, not the system one.
    assertNotSame(
        "Expected class to be loaded by custom loader",
        exceptionClassInLoader,
        WillBeUnloadedException.class);

    // Call getChecked with the dynamically loaded exception class. This is the action we are
    // testing for memory leaks. We use a successful future because we only care about the
    // handling of the exception *type*, not about creating an instance of it.
    getChecked(immediateFuture("some value"), exceptionClassInLoader);

    return new WeakReference<>(customLoader);
  }
}
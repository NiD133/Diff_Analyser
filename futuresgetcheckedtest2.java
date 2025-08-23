package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 *
 * <p>This class is a subset of tests for {@code getChecked}, focusing on interruption behavior and
 * class unloading. The original test suite was likely split, resulting in the "Test2" suffix.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTestTest2 {

  @Test
  public void getChecked_whenThreadIsInterrupted_wrapsInterruptedExceptionAndPreservesStatus() {
    // Arrange
    SettableFuture<String> future = SettableFuture.create();
    Thread.currentThread().interrupt(); // Interrupt the thread before calling getChecked.

    try {
      // Act
      TwoArgConstructorException thrown =
          assertThrows(
              "getChecked should have thrown an exception",
              TwoArgConstructorException.class,
              () -> getChecked(future, TwoArgConstructorException.class));

      // Assert
      // The InterruptedException should be wrapped in the specified exception type.
      assertThat(thrown).hasCauseThat().isInstanceOf(InterruptedException.class);
      // The thread's interrupted status should be preserved.
      assertTrue("Interrupted status should be preserved", Thread.currentThread().isInterrupted());
    } finally {
      // Cleanup: Clear the interrupted status to avoid affecting subsequent tests.
      Thread.interrupted();
    }
  }

  /**
   * Tests that {@code getChecked} does not hold a strong reference to the exception class, which
   * would prevent it and its ClassLoader from being garbage collected.
   */
  @Test
  public void getChecked_doesNotPreventClassUnloading() throws Exception {
    WeakReference<URLClassLoader> loaderReference = doTestClassUnloadingAndReturnLoaderReference();
    GcFinalization.awaitClear(loaderReference);
  }

  /**
   * Sets up a scenario for testing class unloading.
   *
   * <p>It loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
   * getChecked} with it, and then returns a {@link WeakReference} to the loader. The caller can
   * then force garbage collection and assert that the loader was unloaded, proving no strong
   * references are held.
   *
   * @return a {@link WeakReference} to the temporary {@code ClassLoader} used in the test.
   */
  private WeakReference<URLClassLoader> doTestClassUnloadingAndReturnLoaderReference()
      throws Exception {
    URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
    @SuppressWarnings("unchecked")
    Class<WillBeUnloadedException> shadowClass =
        (Class<WillBeUnloadedException>)
            Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
    assertNotSame(
        "The shadow class should be loaded by a different classloader",
        shadowClass,
        WillBeUnloadedException.class);

    // Call getChecked with an exception type from a different classloader.
    // This is a simple call that doesn't throw; we're just testing for memory leaks.
    getChecked(immediateFuture("foo"), shadowClass);

    return new WeakReference<>(shadowLoader);
  }

  /** An exception class to be loaded in a separate ClassLoader for the class unloading test. */
  public static final class WillBeUnloadedException extends Exception {}
}
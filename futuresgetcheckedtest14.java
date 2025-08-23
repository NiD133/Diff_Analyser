package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import junit.framework.TestCase;

/** Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class, long, java.util.concurrent.TimeUnit)}. */
public class FuturesGetCheckedTestTest14 extends TestCase {

  /**
   * Sets up a scenario to test that {@code getChecked} does not hold strong references to an
   * exception class, which would prevent its class loader from being garbage collected.
   *
   * <p>This method loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls
   * {@code getChecked} with it, and then returns a {@code WeakReference} to the loader. A caller
   * can then use this reference to verify that the loader is eventually garbage-collected.
   *
   * @return a {@code WeakReference} to the temporary class loader.
   */
  private WeakReference<URLClassLoader> setupScenarioForClassLoaderGcTest() throws Exception {
    // 1. Create a new "shadow" class loader, isolated from the system class loader.
    URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);

    // 2. Load the exception class using the shadow loader.
    @SuppressWarnings("unchecked") // We know the name corresponds to a subclass of WillBeUnloadedException.
    Class<WillBeUnloadedException> shadowClass =
        (Class<WillBeUnloadedException>)
            Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);

    // Sanity check: The shadow-loaded class should be a different instance from the one
    // loaded by the test's class loader.
    assertNotSame(
        "The shadow class should be a different instance from the test's class.",
        shadowClass,
        WillBeUnloadedException.class);

    // 3. Call getChecked with the shadow-loaded class. This is the action under test.
    // We expect getChecked to not retain a strong reference to `shadowClass`.
    getChecked(immediateFuture("foo"), shadowClass);

    // 4. Return a WeakReference to the loader. The caller can now check if it gets GC'd.
    return new WeakReference<>(shadowLoader);
  }

  /** An exception class used for testing class loader garbage collection. */
  public static final class WillBeUnloadedException extends Exception {}

  public void testGetCheckedTimed_whenInterrupted_wrapsInterruptedException() {
    // ARRANGE
    SettableFuture<String> future = SettableFuture.create();
    // Interrupt the thread before calling the method under test.
    Thread.currentThread().interrupt();

    try {
      // ACT & ASSERT: Verify that getChecked throws the expected exception.
      TwoArgConstructorException thrown =
          assertThrows(
              TwoArgConstructorException.class,
              () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));

      // Further assertions on the exception and thread state.
      assertThat(thrown).hasCauseThat().isInstanceOf(InterruptedException.class);
      // The interrupted flag should still be set after the exception is caught.
      assertThat(Thread.currentThread().isInterrupted()).isTrue();

    } finally {
      // CLEANUP: Clear the interrupted status to prevent side effects on subsequent tests.
      Thread.interrupted();
    }
  }
}
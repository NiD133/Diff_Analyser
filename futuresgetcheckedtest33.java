package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.assertNotSame;

import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for memory behavior of {@link Futures#getChecked(Future, Class)}, specifically regarding
 * class and ClassLoader unloading.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedClassUnloadingTest {

  /**
   * A simple exception class designed to be loaded in a custom class loader. This allows us to test
   * if the class loader can be garbage collected after use.
   */
  public static final class UnloadableException extends Exception {}

  /**
   * Verifies that {@code Futures.getChecked} does not hold a strong reference to the exception
   * class it is given. A strong reference would prevent the class's {@code ClassLoader} from being
   * garbage collected, causing a memory leak.
   *
   * <p>The test works by:
   *
   * <ol>
   *   <li>Creating a new {@code ClassLoader}.
   *   <li>Loading a custom exception class using this new loader.
   *   <li>Calling {@code getChecked} with the dynamically loaded class.
   *   <li>Asserting that the {@code ClassLoader} can be garbage collected afterward.
   * </ol>
   */
  @Test
  @AndroidIncompatible // "Parent ClassLoader may not be null"
  public void getChecked_doesNotPreventClassLoaderUnloading() throws Exception {
    // Arrange: Create a weak reference to a custom class loader after it has been used by
    // getChecked().
    WeakReference<URLClassLoader> loaderReference = createLoaderAndCallGetCheckedWithItsClass();

    // Act & Assert: Force garbage collection and assert that the weak reference has been cleared.
    // This proves the ClassLoader was collected, meaning getChecked did not leak a reference.
    GcFinalization.awaitClear(loaderReference);
  }

  /**
   * Creates a new {@link URLClassLoader}, loads the {@link UnloadableException} class with it,
   * calls {@code getChecked} using that class, and then returns a {@link WeakReference} to the
   * class loader.
   *
   * @return a weak reference to the class loader, which can be used to check if it has been
   *     garbage collected.
   */
  private WeakReference<URLClassLoader> createLoaderAndCallGetCheckedWithItsClass()
      throws Exception {
    // Create a custom loader. Setting the parent to null is important for this test, as it
    // helps ensure the loader can be garbage collected independently.
    URLClassLoader customLoader = new URLClassLoader(parseJavaClassPath(), null);

    // Load the exception class using the custom loader.
    @SuppressWarnings("unchecked")
    Class<UnloadableException> exceptionClassInCustomLoader =
        (Class<UnloadableException>)
            Class.forName(UnloadableException.class.getName(), false, customLoader);

    // Sanity check: The class from the custom loader is a different object
    // from the one in the test's own class loader.
    assertNotSame(
        "The loaded class should be from the custom loader",
        UnloadableException.class,
        exceptionClassInCustomLoader);

    // Use the dynamically loaded class in the method under test.
    // This is the step that could potentially create a strong reference and a memory leak.
    getChecked(immediateFuture("some value"), exceptionClassInCustomLoader);

    // Return a weak reference to the loader. If getChecked holds no strong references to
    // anything from this loader, the loader itself should be garbage collectable.
    return new WeakReference<>(customLoader);
  }
}
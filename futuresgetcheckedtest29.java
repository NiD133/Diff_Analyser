package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithWrongTypesConstructor;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}, focusing on exception
 * type validation and memory behavior.
 */
@DisplayName("Futures.getChecked exception behavior")
class FuturesGetCheckedBehaviorTest {

  /**
   * A custom exception class used specifically for the class loader unloading test. It is defined
   * here to be self-contained within the test context. It must have a constructor that accepts a
   * Throwable to be considered a valid type by {@code getChecked}.
   */
  public static final class WillBeUnloadedException extends Exception {
    public WillBeUnloadedException(Throwable cause) {
      super(cause);
    }
  }

  @Test
  @DisplayName("should throw IllegalArgumentException for an exception type with no valid constructor")
  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType") // Suppress warning for intentionally using an invalid type
  void getChecked_withExceptionTypeMissingValidConstructor_throwsIllegalArgumentException() {
    // ARRANGE: An exception type that cannot be used by getChecked because it lacks a
    // constructor that accepts a Throwable cause.
    Class<ExceptionWithWrongTypesConstructor> invalidExceptionType =
        ExceptionWithWrongTypesConstructor.class;
    ListenableFuture<?> failedFuture = FAILED_FUTURE_CHECKED_EXCEPTION;

    // ACT & ASSERT: Calling getChecked with this invalid type should fail fast.
    // The method needs a way to wrap the original failure cause, and without a suitable
    // constructor, it cannot proceed.
    var thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> getChecked(failedFuture, invalidExceptionType),
            "Expected getChecked to throw IAE for an exception type with no (Throwable) constructor");

    // For extra clarity and robustness, assert on the expected error message.
    assertThat(thrown)
        .hasMessageThat()
        .contains(
            "must be a checked exception type with a public constructor that accepts a "
                + "Throwable cause");
  }

  @Test
  @DisplayName("should not hold strong references, allowing class loaders to be garbage collected")
  void getChecked_doesNotPreventClassLoaderUnloading() throws Exception {
    // ARRANGE: Create a weak reference to a class loader that will be used to call getChecked.
    WeakReference<ClassLoader> loaderReference = createAndUseClassLoaderWithGetChecked();

    // ACT: Force garbage collection to attempt to reclaim the class loader.
    // GcFinalization.awaitFullGc() is a testing utility that tries to reliably trigger GC.
    GcFinalization.awaitFullGc();

    // ASSERT: The weak reference should now be cleared, proving that getChecked did not
    // retain any strong references to the exception class or its loader, thus preventing a leak.
    assertThat(loaderReference.get()).isNull();
  }

  /**
   * Helper method to perform the class loading and getChecked call in an isolated context.
   *
   * @return a WeakReference to the class loader after it has been used.
   */
  private WeakReference<ClassLoader> createAndUseClassLoaderWithGetChecked() throws Exception {
    // 1. Create a new, isolated class loader.
    URLClassLoader customLoader = new URLClassLoader(parseJavaClassPath(), null);

    // 2. Load our test exception class using the new loader. This creates a "shadow"
    // version of the class, distinct from the one in the test's own class loader.
    @SuppressWarnings("unchecked")
    Class<WillBeUnloadedException> shadowExceptionClass =
        (Class<WillBeUnloadedException>)
            Class.forName(WillBeUnloadedException.class.getName(), false, customLoader);
    assertNotSame(
        shadowExceptionClass,
        WillBeUnloadedException.class,
        "The shadow class should be loaded by a different class loader.");

    // 3. Call getChecked with the shadow class. This triggers internal caching mechanisms.
    // The test verifies that this caching does not create a memory leak. We use a successful
    // future because we only need to trigger the class validation and caching logic.
    assertDoesNotThrow(() -> getChecked(immediateFuture("success"), shadowExceptionClass));

    // 4. Return a weak reference to the loader. The strong reference 'customLoader'
    // will go out of scope when this method returns, making the loader eligible for GC.
    return new WeakReference<>(customLoader);
  }
}
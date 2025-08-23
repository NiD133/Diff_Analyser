package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;

import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 */
@NullUnmarked
class FuturesGetCheckedTest {

    /**
     * A custom exception class designed to be loaded in a separate class loader
     * to test for potential memory leaks.
     */
    public static final class WillBeUnloadedException extends Exception {}

    @Test
    void getChecked_whenFutureSucceeds_returnsValue() throws TwoArgConstructorException {
        // Act
        String result = getChecked(immediateFuture("foo"), TwoArgConstructorException.class);

        // Assert
        assertThat(result).isEqualTo("foo");
    }

    /**
     * This test verifies that getChecked does not hold a strong reference to the exception class
     * it is given. By loading the exception class in a temporary class loader, we can assert that
     * the loader can be garbage-collected after getChecked is called, which would be impossible
     * if a strong reference were retained.
     */
    @Test
    void getChecked_doesNotHoldReferenceToExceptionClass_allowingClassLoaderToUnload() throws Exception {
        // Arrange
        WeakReference<URLClassLoader> loaderReference =
                createAndUseClassLoaderWithCustomException();

        // Act & Assert
        // This will pass only if the class loader is no longer strongly referenced and can be GC'd.
        GcFinalization.awaitClear(loaderReference);
    }

    /**
     * Creates a new class loader, loads {@link WillBeUnloadedException} within it, calls
     * {@code getChecked} with the dynamically loaded class, and returns a weak reference to the
     * loader.
     */
    private WeakReference<URLClassLoader> createAndUseClassLoaderWithCustomException() throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);

        @SuppressWarnings("unchecked") // Safe cast: we know the class name corresponds to the type.
        Class<WillBeUnloadedException> shadowClass =
                (Class<WillBeUnloadedException>)
                        Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);

        // Ensure the class was loaded by our temporary loader, not the system one.
        assertThat(shadowClass).isNotSameInstanceAs(WillBeUnloadedException.class);

        // Call the method under test with the dynamically loaded exception class.
        getChecked(immediateFuture("foo"), shadowClass);

        return new WeakReference<>(shadowLoader);
    }
}
package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR_FUTURE;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Futures#getChecked(java.util.concurrent.Future, Class)}.
 */
@RunWith(JUnit4.class)
public class FuturesGetCheckedTestTest9 {

    /**
     * Tests that when the input future fails with an {@link Error}, {@code getChecked} re-throws the
     * original {@code Error} instance, regardless of the specified exception type.
     */
    @Test
    public void getChecked_futureFailsWithError_rethrowsOriginalError() {
        // Act & Assert
        // Call getChecked on a future that has failed with an Error.
        // It should propagate the original Error, not wrap it.
        Error thrown =
                assertThrows(Error.class, () -> getChecked(ERROR_FUTURE, TwoArgConstructorException.class));

        // Verify that the thrown error is the exact same instance we started with.
        assertThat(thrown).isSameInstanceAs(ERROR);
    }

    /**
     * A helper method for testing that {@code getChecked} does not hold a strong reference to the
     * exception class, allowing its ClassLoader to be garbage collected. This method is likely
     * called by other tests that perform the GC and assertion.
     *
     * @return a WeakReference to the ClassLoader used to load the exception class.
     */
    private WeakReference<URLClassLoader> doTestClassUnloading() throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
        @SuppressWarnings("unchecked")
        Class<WillBeUnloadedException> shadowClass =
                (Class<WillBeUnloadedException>)
                        Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
        assertNotSame(shadowClass, WillBeUnloadedException.class);

        // Call getChecked with the class loaded from the temporary ClassLoader.
        getChecked(immediateFuture("foo"), shadowClass);

        return new WeakReference<>(shadowLoader);
    }

    /**
     * A custom exception class designed to be loaded in a separate ClassLoader to test for potential
     * memory leaks in {@code getChecked}.
     */
    public static final class WillBeUnloadedException extends Exception {
    }
}
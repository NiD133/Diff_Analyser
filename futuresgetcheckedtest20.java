package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION_FUTURE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
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
public class FuturesGetCheckedTestTest20 {

    /**
     * A simple exception class intended to be loaded in a separate classloader to test for potential
     * memory leaks.
     */
    public static final class WillBeUnloadedException extends Exception {}

    @Test
    public void getChecked_timed_whenFutureFailsWithRuntimeException_propagatesOriginalException()
            throws TwoArgConstructorException {
        // Arrange: A future that has already failed with a specific RuntimeException.
        Future<?> failedFuture = RUNTIME_EXCEPTION_FUTURE;

        // The getChecked method is declared to throw TwoArgConstructorException in this case.
        // However, the contract of getChecked specifies that if the future's cause is a
        // RuntimeException or Error, it should be rethrown directly, not wrapped. This test
        // verifies that behavior. The 'throws' clause is still required by the compiler.

        // Act & Assert
        RuntimeException thrown =
                assertThrows(
                        RuntimeException.class,
                        () -> getChecked(failedFuture, TwoArgConstructorException.class, 0, SECONDS));

        assertThat(thrown).isSameInstanceAs(RUNTIME_EXCEPTION);
    }

    /**
     * Sets up a test scenario for verifying that {@code getChecked} does not hold strong references
     * to the exception classes it handles, which would prevent classloader unloading.
     *
     * <p>This method loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls
     * {@code getChecked} with it, and returns a weak reference to the loader. The caller is expected
     * to perform garbage collection and assert that the weak reference has been cleared.
     *
     * @return a {@link WeakReference} to the classloader, which can be used to check if it was
     *     garbage collected.
     */
    private WeakReference<URLClassLoader> getWeakReferenceOfClassLoaderAfterCallingGetChecked()
            throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);

        @SuppressWarnings("unchecked")
        Class<WillBeUnloadedException> shadowClass =
                (Class<WillBeUnloadedException>)
                        Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
        assertThat(shadowClass).isNotSameInstanceAs(WillBeUnloadedException.class);

        // Call getChecked with the class from the new classloader.
        getChecked(immediateFuture("foo"), shadowClass);

        // The caller can now verify that this classloader can be garbage collected.
        return new WeakReference<>(shadowLoader);
    }
}
package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION_FUTURE;
import static org.junit.Assert.assertThrows;

import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import java.util.concurrent.Future;
import junit.framework.TestCase;

/**
 * Tests for {@link Futures#getChecked(Future, Class)}.
 */
public class FuturesGetCheckedTest extends TestCase {

    /**
     * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
     * getChecked(future, WillBeUnloadedException.class)}, and returns the loader. The caller can then
     * test that the {@code ClassLoader} can still be GCed. The test amounts to a test that {@code
     * getChecked} holds no strong references to the class.
     *
     * <p>This helper method is used by other tests in the full suite to verify classloader
     * garbage collection.
     */
    private WeakReference<?> doTestClassUnloading() throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
        @SuppressWarnings("unchecked")
        Class<WillBeUnloadedException> shadowClass =
                (Class<WillBeUnloadedException>)
                        Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
        assertNotSame(shadowClass, WillBeUnloadedException.class);
        getChecked(immediateFuture("foo"), shadowClass);
        return new WeakReference<>(shadowLoader);
    }

    /**
     * An exception class designed to be loaded in a separate classloader to test for memory leaks.
     */
    public static final class WillBeUnloadedException extends Exception {}

    /**
     * Verifies that when a future fails with a RuntimeException, getChecked rethrows the original
     * cause directly instead of wrapping it.
     */
    public void testGetChecked_whenFutureFailsWithRuntimeException_rethrownsCauseDirectly()
            throws TwoArgConstructorException {
        // Arrange: A future that has already failed with a specific RuntimeException.
        Future<?> futureFailingWithRuntimeException = RUNTIME_EXCEPTION_FUTURE;
        RuntimeException originalCause = RUNTIME_EXCEPTION;

        // Act: Call getChecked, expecting it to rethrow the original RuntimeException.
        RuntimeException thrown =
                assertThrows(
                        RuntimeException.class,
                        () -> getChecked(futureFailingWithRuntimeException, TwoArgConstructorException.class));

        // Assert: The thrown exception is the exact same instance as the original cause.
        assertThat(thrown).isSameInstanceAs(originalCause);
    }
}
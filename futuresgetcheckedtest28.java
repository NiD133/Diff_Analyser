package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.ERROR_FUTURE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_CHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_ERROR;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_OTHER_THROWABLE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.FAILED_FUTURE_UNCHECKED_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.OTHER_THROWABLE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.RUNTIME_EXCEPTION_FUTURE;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.UNCHECKED_EXCEPTION;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;
import com.google.common.testing.GcFinalization;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithBadConstructor;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithGoodAndBadConstructor;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithManyConstructors;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithManyConstructorsButOnlyOneThrowable;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithPrivateConstructor;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithSomePrivateConstructors;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithWrongTypesConstructor;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.ExceptionWithoutThrowableConstructor;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorException;
import com.google.common.util.concurrent.FuturesGetCheckedInputs.TwoArgConstructorRuntimeException;
import java.lang.ref.WeakReference;
import java.net.URLClassLoader;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class FuturesGetCheckedTestTest28 extends TestCase {

    /**
     * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
     * getChecked(future, WillBeUnloadedException.class)}, and returns the loader. The caller can then
     * test that the {@code ClassLoader} can still be GCed. The test amounts to a test that {@code
     * getChecked} holds no strong references to the class.
     */
    private WeakReference<?> doTestClassUnloading() throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
        @SuppressWarnings("unchecked")
        Class<WillBeUnloadedException> shadowClass = (Class<WillBeUnloadedException>) Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
        assertNotSame(shadowClass, WillBeUnloadedException.class);
        getChecked(immediateFuture("foo"), shadowClass);
        return new WeakReference<>(shadowLoader);
    }

    public static final class WillBeUnloadedException extends Exception {
    }

    @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
    public void testGetCheckedUntimed_exceptionClassNoPublicConstructor() throws ExceptionWithPrivateConstructor {
        assertThrows(IllegalArgumentException.class, () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));
    }
}

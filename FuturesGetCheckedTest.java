/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link Futures#getChecked(Future, Class)}. This suite is structured into sections
 * for untimed gets, timed gets, exception constructor resolution, and class unloading behavior.
 */
@RunWith(JUnit4.class)
@NullUnmarked
public class FuturesGetCheckedTest {

    // region Untimed getChecked() tests

    @Test
    public void getCheckedUntimed_whenFutureSucceeds_returnsValue() throws TwoArgConstructorException {
        assertThat(getChecked(immediateFuture("foo"), TwoArgConstructorException.class))
                .isEqualTo("foo");
    }

    @Test
    public void getCheckedUntimed_whenThreadIsInterrupted_throwsExceptionWrappingInterruptedException() {
        SettableFuture<String> future = SettableFuture.create();
        Thread.currentThread().interrupt();
        try {
            getChecked(future, TwoArgConstructorException.class);
            fail("Expected TwoArgConstructorException to be thrown");
        } catch (TwoArgConstructorException expected) {
            assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
            assertTrue("Interrupted status should be preserved", Thread.currentThread().isInterrupted());
        } finally {
            // Clear the interrupted status for subsequent tests.
            Thread.interrupted();
        }
    }

    @Test
    public void getCheckedUntimed_whenFutureIsCancelled_throwsCancellationException() {
        SettableFuture<String> future = SettableFuture.create();
        future.cancel(true);
        assertThrows(
                CancellationException.class, () -> getChecked(future, TwoArgConstructorException.class));
    }

    @Test
    public void getCheckedUntimed_whenFutureFailsWithCheckedException_wrapsAndThrows() {
        TwoArgConstructorException e =
                assertThrows(
                        TwoArgConstructorException.class,
                        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));
        assertThat(e).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
    }

    @Test
    public void getCheckedUntimed_whenFutureFailsWithUncheckedException_throwsUncheckedExecutionException() {
        UncheckedExecutionException e =
                assertThrows(
                        UncheckedExecutionException.class,
                        () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));
        assertThat(e).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
    }

    @Test
    public void getCheckedUntimed_whenFutureFailsWithError_throwsExecutionError() {
        ExecutionError e =
                assertThrows(
                        ExecutionError.class,
                        () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));
        assertThat(e).hasCauseThat().isEqualTo(ERROR);
    }

    @Test
    public void getCheckedUntimed_whenFutureFailsWithOtherThrowable_wrapsAndThrows() {
        TwoArgConstructorException e =
                assertThrows(
                        TwoArgConstructorException.class,
                        () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));
        assertThat(e).hasCauseThat().isEqualTo(OTHER_THROWABLE);
    }

    @Test
    public void getCheckedUntimed_whenFutureHasRuntimeException_rethrowsAsis() {
        RuntimeException e =
                assertThrows(
                        RuntimeException.class,
                        () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));
        assertThat(e).isEqualTo(RUNTIME_EXCEPTION);
    }

    @Test
    public void getCheckedUntimed_whenFutureHasError_rethrowsAsis() {
        Error e =
                assertThrows(
                        Error.class, () -> getChecked(ERROR_FUTURE, TwoArgConstructorException.class));
        assertThat(e).isEqualTo(ERROR);
    }

    // endregion

    // region Timed getChecked() tests

    @Test
    public void getCheckedTimed_whenFutureSucceeds_returnsValue() throws TwoArgConstructorException {
        assertThat(getChecked(immediateFuture("foo"), TwoArgConstructorException.class, 0, SECONDS))
                .isEqualTo("foo");
    }

    @Test
    public void getCheckedTimed_whenTimeoutOccurs_throwsExceptionWrappingTimeoutException() {
        SettableFuture<String> future = SettableFuture.create();
        TwoArgConstructorException e =
                assertThrows(
                        TwoArgConstructorException.class,
                        () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).hasCauseThat().isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getCheckedTimed_whenThreadIsInterrupted_throwsExceptionWrappingInterruptedException() {
        SettableFuture<String> future = SettableFuture.create();
        Thread.currentThread().interrupt();
        try {
            getChecked(future, TwoArgConstructorException.class, 0, SECONDS);
            fail("Expected TwoArgConstructorException to be thrown");
        } catch (TwoArgConstructorException expected) {
            assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
            assertTrue("Interrupted status should be preserved", Thread.currentThread().isInterrupted());
        } finally {
            // Clear the interrupted status for subsequent tests.
            Thread.interrupted();
        }
    }

    @Test
    public void getCheckedTimed_whenFutureIsCancelled_throwsCancellationException() {
        SettableFuture<String> future = SettableFuture.create();
        future.cancel(true);
        assertThrows(
                CancellationException.class,
                () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
    }

    @Test
    public void getCheckedTimed_whenFutureFailsWithCheckedException_wrapsAndThrows() {
        TwoArgConstructorException e =
                assertThrows(
                        TwoArgConstructorException.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
    }

    @Test
    public void getCheckedTimed_whenFutureFailsWithUncheckedException_throwsUncheckedExecutionException() {
        UncheckedExecutionException e =
                assertThrows(
                        UncheckedExecutionException.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_UNCHECKED_EXCEPTION,
                                        TwoArgConstructorException.class,
                                        0,
                                        SECONDS));
        assertThat(e).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
    }

    @Test
    public void getCheckedTimed_whenFutureFailsWithError_throwsExecutionError() {
        ExecutionError e =
                assertThrows(
                        ExecutionError.class,
                        () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).hasCauseThat().isEqualTo(ERROR);
    }

    @Test
    public void getCheckedTimed_whenFutureFailsWithOtherThrowable_wrapsAndThrows() {
        TwoArgConstructorException e =
                assertThrows(
                        TwoArgConstructorException.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).hasCauseThat().isEqualTo(OTHER_THROWABLE);
    }

    @Test
    public void getCheckedTimed_whenFutureHasRuntimeException_rethrowsAsis() {
        RuntimeException e =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).isEqualTo(RUNTIME_EXCEPTION);
    }

    @Test
    public void getCheckedTimed_whenFutureHasError_rethrowsAsis() {
        Error e =
                assertThrows(
                        Error.class,
                        () -> getChecked(ERROR_FUTURE, TwoArgConstructorException.class, 0, SECONDS));
        assertThat(e).isEqualTo(ERROR);
    }

    // endregion

    // region Exception Constructor Resolution tests

    @Test
    public void constructorResolution_withInvalidConstructor_throwsIllegalArgumentOnSuccessfulFuture() {
        assertThrows(
                IllegalArgumentException.class,
                () -> getChecked(immediateFuture("x"), ExceptionWithBadConstructor.class));
    }

    @Test
    public void constructorResolution_withInvalidConstructor_throwsIllegalArgumentOnFailedFuture() {
        assertThrows(
                IllegalArgumentException.class,
                () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class));
    }

    @Test
    public void constructorResolution_withGoodAndBadConstructors_usesValidConstructor()
            throws Exception {
        ExceptionWithGoodAndBadConstructor e =
                assertThrows(
                        ExceptionWithGoodAndBadConstructor.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class));
        assertThat(e).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
    }

    @Test
    @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
    public void constructorResolution_whenExceptionTypeIsRuntimeException_throwsIllegalArgument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorRuntimeException.class));
    }

    @Test
    public void constructorResolution_withSomePrivateConstructors_usesPublicConstructor() {
        assertThrows(
                ExceptionWithSomePrivateConstructors.class,
                () ->
                        getChecked(
                                FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithSomePrivateConstructors.class));
    }

    @Test
    @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
    public void constructorResolution_withOnlyPrivateConstructors_throwsIllegalArgument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));
    }

    @Test
    @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
    public void constructorResolution_withWrongParameterTypes_throwsIllegalArgument() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithWrongTypesConstructor.class));
    }

    @Test
    public void constructorResolution_prefersStringThenThrowableConstructor() {
        ExceptionWithManyConstructors e =
                assertThrows(
                        ExceptionWithManyConstructors.class,
                        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));
        assertThat(e.usedExpectedConstructor).isTrue();
    }

    @Test
    public void constructorResolution_whenNoThrowableConstructor_usesInitCause() {
        ExceptionWithoutThrowableConstructor e =
                assertThrows(
                        ExceptionWithoutThrowableConstructor.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));
        assertThat(e).hasMessageThat().contains("mymessage");
        assertThat(e).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
    }

    @Test
    public void constructorResolution_prefersConstructorWithThrowableParameter() {
        ExceptionWithManyConstructorsButOnlyOneThrowable e =
                assertThrows(
                        ExceptionWithManyConstructorsButOnlyOneThrowable.class,
                        () ->
                                getChecked(
                                        FAILED_FUTURE_CHECKED_EXCEPTION,
                                        ExceptionWithManyConstructorsButOnlyOneThrowable.class));
        assertThat(e).hasMessageThat().contains("mymessage");
        assertThat(e.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
    }

    // endregion

    // region Class Unloading tests

    public static final class WillBeUnloadedException extends Exception {}

    @Test
    @AndroidIncompatible // "Parent ClassLoader may not be null"; maybe avoidable if we try?
    public void getChecked_doesNotPreventClassLoaderUnloading() throws Exception {
        WeakReference<?> classLoaderRef = doTestClassUnloading();
        GcFinalization.awaitClear(classLoaderRef);
    }

    /**
     * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
     * getChecked(future, WillBeUnloadedException.class)}, and returns a weak reference to the loader.
     * The caller can then assert that the {@code ClassLoader} can be garbage collected, proving that
     * {@code getChecked} holds no strong references to the exception class.
     */
    private WeakReference<URLClassLoader> doTestClassUnloading() throws Exception {
        URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
        @SuppressWarnings("unchecked")
        Class<WillBeUnloadedException> shadowClass =
                (Class<WillBeUnloadedException>)
                        Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
        assertThat(shadowClass).isNotSameInstanceAs(WillBeUnloadedException.class);
        getChecked(immediateFuture("foo"), shadowClass);
        return new WeakReference<>(shadowLoader);
    }

    // endregion
}
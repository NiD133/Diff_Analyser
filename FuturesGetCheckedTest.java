/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
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

/** Unit tests for {@link Futures#getChecked(Future, Class)}. */
@NullUnmarked
public class FuturesGetCheckedTest extends TestCase {
  // ==================== UNTIMED GET TESTS ====================

  public void testGetCheckedUntimed_successfulFuture() throws TwoArgConstructorException {
    String result = getChecked(immediateFuture("foo"), TwoArgConstructorException.class);
    assertThat(result).isEqualTo("foo");
  }

  public void testGetCheckedUntimed_interruptedThreadThrows() {
    SettableFuture<String> future = SettableFuture.create();
    Thread.currentThread().interrupt();

    try {
      getChecked(future, TwoArgConstructorException.class);
      fail("Expected TwoArgConstructorException due to interruption");
    } catch (TwoArgConstructorException expected) {
      assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertWithMessage("Thread interrupt flag should remain set")
          .that(Thread.currentThread().isInterrupted())
          .isTrue();
    } finally {
      Thread.interrupted(); // Clear interrupt status
    }
  }

  public void testGetCheckedUntimed_cancelledFutureThrowsCancellation() {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);

    assertThrows(
        "Cancelled future should throw CancellationException",
        CancellationException.class,
        () -> getChecked(future, TwoArgConstructorException.class));
  }

  public void testGetCheckedUntimed_checkedExecutionExceptionWrapped() {
    TwoArgConstructorException exception =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));

    assertThat(exception).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_uncheckedExecutionExceptionThrownAsIs() {
    UncheckedExecutionException exception =
        assertThrows(
            UncheckedExecutionException.class,
            () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));

    assertThat(exception).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_errorInFutureThrownAsError() {
    ExecutionError error =
        assertThrows(
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));

    assertThat(error).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedUntimed_otherThrowableWrapped() {
    TwoArgConstructorException exception =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));

    assertThat(exception).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedUntimed_runtimeExceptionThrownAsIs() {
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));

    assertThat(exception).isEqualTo(RUNTIME_EXCEPTION);
  }

  public void testGetCheckedUntimed_errorThrownAsIs() {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class);
      fail("Expected Error to be thrown");
    } catch (Error expected) {
      assertThat(expected).isEqualTo(ERROR);
    }
  }

  public void testGetCheckedUntimed_badExceptionConstructorFailsForSuccessfulInput() {
    assertThrows(
        "ExceptionWithBadConstructor should fail due to invalid constructor",
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture("x"), ExceptionWithBadConstructor.class));
  }

  public void testGetCheckedUntimed_badExceptionConstructorFailsForFailedFuture() {
    assertThrows(
        "ExceptionWithBadConstructor should fail even when future contains exception",
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class));
  }

  public void testGetCheckedUntimed_exceptionWithGoodAndBadConstructorWrapsOriginal() {
    ExceptionWithGoodAndBadConstructor exception =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class));

    assertThat(exception).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // ==================== TIMED GET TESTS ====================

  public void testGetCheckedTimed_successfulFuture() throws TwoArgConstructorException {
    String result =
        getChecked(immediateFuture("foo"), TwoArgConstructorException.class, 0, SECONDS);
    assertThat(result).isEqualTo("foo");
  }

  public void testGetCheckedTimed_interruptedThreadThrows() {
    SettableFuture<String> future = SettableFuture.create();
    Thread.currentThread().interrupt();

    try {
      getChecked(future, TwoArgConstructorException.class, 0, SECONDS);
      fail("Expected TwoArgConstructorException due to interruption");
    } catch (TwoArgConstructorException expected) {
      assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertWithMessage("Thread interrupt flag should remain set")
          .that(Thread.currentThread().isInterrupted())
          .isTrue();
    } finally {
      Thread.interrupted(); // Clear interrupt status
    }
  }

  public void testGetCheckedTimed_cancelledFutureThrowsCancellation() {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);

    assertThrows(
        "Cancelled future should throw CancellationException",
        CancellationException.class,
        () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
  }

  public void testGetCheckedTimed_checkedExecutionExceptionWrapped() {
    TwoArgConstructorException exception =
        assertThrows(
            TwoArgConstructorException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class, 0, SECONDS));

    assertThat(exception).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_uncheckedExecutionExceptionThrownAsIs() {
    UncheckedExecutionException exception =
        assertThrows(
            UncheckedExecutionException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_UNCHECKED_EXCEPTION,
                    TwoArgConstructorException.class,
                    0,
                    SECONDS));

    assertThat(exception).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_errorInFutureThrownAsError() {
    ExecutionError error =
        assertThrows(
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class, 0, SECONDS));

    assertThat(error).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedTimed_otherThrowableWrapped() {
    TwoArgConstructorException exception =
        assertThrows(
            TwoArgConstructorException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_OTHER_THROWABLE,
                    TwoArgConstructorException.class,
                    0,
                    SECONDS));

    assertThat(exception).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedTimed_runtimeExceptionThrownAsIs() {
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () ->
                getChecked(
                    RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class, 0, SECONDS));

    assertThat(exception).isEqualTo(RUNTIME_EXCEPTION);
  }

  public void testGetCheckedTimed_errorThrownAsIs() {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class, 0, SECONDS);
      fail("Expected Error to be thrown");
    } catch (Error expected) {
      assertThat(expected).isEqualTo(ERROR);
    }
  }

  public void testGetCheckedTimed_timeoutExceptionWrapped() {
    SettableFuture<String> future = SettableFuture.create();
    TwoArgConstructorException exception =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));

    assertThat(exception).hasCauseThat().isInstanceOf(TimeoutException.class);
  }

  public void testGetCheckedTimed_badExceptionConstructorFailsForSuccessfulInput() {
    assertThrows(
        "ExceptionWithBadConstructor should fail due to invalid constructor",
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture("x"), ExceptionWithBadConstructor.class, 1, SECONDS));
  }

  public void testGetCheckedTimed_badExceptionConstructorFailsForFailedFuture() {
    assertThrows(
        "ExceptionWithBadConstructor should fail even when future contains exception",
        IllegalArgumentException.class,
        () ->
            getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class, 1, SECONDS));
  }

  public void testGetCheckedTimed_exceptionWithGoodAndBadConstructorWrapsOriginal() {
    ExceptionWithGoodAndBadConstructor exception =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION,
                    ExceptionWithGoodAndBadConstructor.class,
                    1,
                    SECONDS));

    assertThat(exception).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // ==================== EXCEPTION CONSTRUCTION EDGE CASES ====================

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassIsRuntimeExceptionFails() {
    assertThrows(
        "Should reject RuntimeException exceptionClass",
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorRuntimeException.class));
  }

  public void testGetCheckedUntimed_exceptionClassWithSomePrivateConstructorsSucceeds() {
    ExceptionWithSomePrivateConstructors exception =
        assertThrows(
            ExceptionWithSomePrivateConstructors.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithSomePrivateConstructors.class));

    assertThat(exception).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassNoPublicConstructorFails() {
    assertThrows(
        "ExceptionWithPrivateConstructor should fail due to no public constructor",
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassPublicConstructorWrongTypeFails() {
    assertThrows(
        "ExceptionWithWrongTypesConstructor should fail due to incompatible constructor",
        IllegalArgumentException.class,
        () ->
            getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithWrongTypesConstructor.class));
  }

  public void testGetCheckedUntimed_exceptionClassPrefersStringConstructor() {
    ExceptionWithManyConstructors exception =
        assertThrows(
            ExceptionWithManyConstructors.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));

    assertWithMessage("Expected constructor with String parameter to be used")
        .that(exception.usedExpectedConstructor)
        .isTrue();
  }

  public void testGetCheckedUntimed_exceptionWithoutThrowableConstructorUsesInitCause() {
    ExceptionWithoutThrowableConstructor exception =
        assertThrows(
            ExceptionWithoutThrowableConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));

    assertThat(exception)
        .hasMessageThat()
        .contains("mymessage");
    assertThat(exception).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_prefersConstructorWithThrowableParameter() {
    ExceptionWithManyConstructorsButOnlyOneThrowable exception =
        assertThrows(
            ExceptionWithManyConstructorsButOnlyOneThrowable.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION,
                    ExceptionWithManyConstructorsButOnlyOneThrowable.class));

    assertThat(exception)
        .hasMessageThat()
        .contains("mymessage");
    assertThat(exception.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
  }

  // ==================== CLASS UNLOADING TEST ====================

  public static final class WillBeUnloadedException extends Exception {}

  @AndroidIncompatible // "Parent ClassLoader may not be null"; maybe avoidable if we try?
  public void testGetChecked_classUnloading() throws Exception {
    WeakReference<?> classLoaderRef = loadAndUseExceptionInSeparateClassLoader();
    GcFinalization.awaitClear(classLoaderRef);
  }

  /**
   * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls
   * {@code getChecked(future, WillBeUnloadedException.class)}, and returns the loader.
   * Verifies that {@code getChecked} doesn't prevent class unloading.
   */
  private WeakReference<?> loadAndUseExceptionInSeparateClassLoader() throws Exception {
    URLClassLoader shadowLoader = new URLClassLoader(parseJavaClassPath(), null);
    @SuppressWarnings("unchecked")
    Class<WillBeUnloadedException> shadowClass =
        (Class<WillBeUnloadedException>)
            Class.forName(WillBeUnloadedException.class.getName(), false, shadowLoader);
    assertNotSame(
        "Should load class in separate classloader",
        shadowClass,
        WillBeUnloadedException.class);
    
    getChecked(immediateFuture("foo"), shadowClass);
    return new WeakReference<>(shadowLoader);
  }
}
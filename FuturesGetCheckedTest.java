/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

/** Unit tests for Futures#getChecked(Future, Class). */
@NullUnmarked
public class FuturesGetCheckedTest extends TestCase {

  // Common timeout for "timed" tests to keep them fast and deterministic.
  private static final long ZERO_SECONDS = 0L;

  // ---------------------------------------------------------------------------
  // Untimed getChecked(...) tests
  // ---------------------------------------------------------------------------

  public void testGetCheckedUntimed_success() throws TwoArgConstructorException {
    assertThat(getChecked(immediateFuture("foo"), TwoArgConstructorException.class))
        .isEqualTo("foo");
  }

  public void testGetCheckedUntimed_interrupted() {
    SettableFuture<String> future = SettableFuture.create();
    assertInterruptedCausesGetCheckedUntimed(future, TwoArgConstructorException.class);
  }

  public void testGetCheckedUntimed_cancelled() throws TwoArgConstructorException {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);

    assertThrows(
        CancellationException.class, () -> getChecked(future, TwoArgConstructorException.class));
  }

  public void testGetCheckedUntimed_executionExceptionChecked() {
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_executionExceptionUnchecked()
      throws TwoArgConstructorException {
    UncheckedExecutionException thrown =
        assertThrows(
            UncheckedExecutionException.class,
            () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_executionExceptionError() throws TwoArgConstructorException {
    ExecutionError thrown =
        assertThrows(
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedUntimed_executionExceptionOtherThrowable() {
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedUntimed_runtimeException() throws TwoArgConstructorException {
    RuntimeException thrown =
        assertThrows(
            RuntimeException.class,
            () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));
    assertThat(thrown).isSameInstanceAs(RUNTIME_EXCEPTION);
  }

  public void testGetCheckedUntimed_error() throws TwoArgConstructorException {
    Error thrown =
        assertThrows(Error.class, () -> getChecked(ERROR_FUTURE, TwoArgConstructorException.class));
    assertThat(thrown).isSameInstanceAs(ERROR);
  }

  public void testGetCheckedUntimed_badExceptionConstructor_failsEvenForSuccessfulInput()
      throws Exception {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture("x"), ExceptionWithBadConstructor.class));
  }

  public void testGetCheckedUntimed_badExceptionConstructor_wrapsOriginalChecked()
      throws Exception {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class));
  }

  public void testGetCheckedUntimed_withGoodAndBadExceptionConstructor() throws Exception {
    ExceptionWithGoodAndBadConstructor thrown =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () -> getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class));
    assertThat(thrown).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // ---------------------------------------------------------------------------
  // Timed getChecked(..., timeout, unit) tests
  // ---------------------------------------------------------------------------

  public void testGetCheckedTimed_success() throws TwoArgConstructorException {
    assertThat(getChecked0Sec(immediateFuture("foo"), TwoArgConstructorException.class))
        .isEqualTo("foo");
  }

  public void testGetCheckedTimed_interrupted() {
    SettableFuture<String> future = SettableFuture.create();
    assertInterruptedCausesGetCheckedTimed(future, TwoArgConstructorException.class);
  }

  public void testGetCheckedTimed_cancelled() throws TwoArgConstructorException {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);

    assertThrows(
        CancellationException.class,
        () -> getChecked0Sec(future, TwoArgConstructorException.class));
  }

  public void testGetCheckedTimed_executionExceptionChecked() {
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked0Sec(
                FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_executionExceptionUnchecked() throws TwoArgConstructorException {
    UncheckedExecutionException thrown =
        assertThrows(
            UncheckedExecutionException.class,
            () -> getChecked0Sec(
                FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_executionExceptionError() throws TwoArgConstructorException {
    ExecutionError thrown =
        assertThrows(
            ExecutionError.class,
            () -> getChecked0Sec(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedTimed_executionExceptionOtherThrowable() {
    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked0Sec(
                FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedTimed_runtimeException() throws TwoArgConstructorException {
    RuntimeException thrown =
        assertThrows(
            RuntimeException.class,
            () -> getChecked0Sec(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));
    assertThat(thrown).isSameInstanceAs(RUNTIME_EXCEPTION);
  }

  public void testGetCheckedTimed_error() throws TwoArgConstructorException {
    Error thrown =
        assertThrows(
            Error.class, () -> getChecked0Sec(ERROR_FUTURE, TwoArgConstructorException.class));
    assertThat(thrown).isSameInstanceAs(ERROR);
  }

  public void testGetCheckedTimed_timeoutException() {
    SettableFuture<String> future = SettableFuture.create();

    TwoArgConstructorException thrown =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked0Sec(future, TwoArgConstructorException.class));
    assertThat(thrown).hasCauseThat().isInstanceOf(TimeoutException.class);
  }

  public void testGetCheckedTimed_badExceptionConstructor_failsEvenForSuccessfulInput()
      throws Exception {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture("x"), ExceptionWithBadConstructor.class, 1, SECONDS));
  }

  public void testGetCheckedTimed_badExceptionConstructor_wrapsOriginalChecked() throws Exception {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class, 1, SECONDS));
  }

  public void testGetCheckedTimed_withGoodAndBadExceptionConstructor() {
    ExceptionWithGoodAndBadConstructor thrown =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () -> getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION,
                ExceptionWithGoodAndBadConstructor.class,
                1,
                SECONDS));
    assertThat(thrown).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // ---------------------------------------------------------------------------
  // Exception-construction edge cases through untimed getChecked(...)
  // ---------------------------------------------------------------------------

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassIsRuntimeException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorRuntimeException.class));
  }

  public void testGetCheckedUntimed_exceptionClassSomePrivateConstructors() {
    assertThrows(
        ExceptionWithSomePrivateConstructors.class,
        () -> getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithSomePrivateConstructors.class));
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassNoPublicConstructor()
      throws ExceptionWithPrivateConstructor {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassPublicConstructorWrongType()
      throws ExceptionWithWrongTypesConstructor {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithWrongTypesConstructor.class));
  }

  public void testGetCheckedUntimed_exceptionClassPrefersStringConstructor() {
    ExceptionWithManyConstructors thrown =
        assertThrows(
            ExceptionWithManyConstructors.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));
    assertThat(thrown.usedExpectedConstructor).isTrue();
  }

  public void testGetCheckedUntimed_exceptionClassUsedInitCause() {
    ExceptionWithoutThrowableConstructor thrown =
        assertThrows(
            ExceptionWithoutThrowableConstructor.class,
            () -> getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));
    assertThat(thrown).hasMessageThat().contains("mymessage");
    assertThat(thrown).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testPrefersConstructorWithThrowableParameter() {
    ExceptionWithManyConstructorsButOnlyOneThrowable thrown =
        assertThrows(
            ExceptionWithManyConstructorsButOnlyOneThrowable.class,
            () -> getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION,
                    ExceptionWithManyConstructorsButOnlyOneThrowable.class));
    assertThat(thrown).hasMessageThat().contains("mymessage");
    assertThat(thrown.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
  }

  // ---------------------------------------------------------------------------
  // Class unloading: ensure getChecked doesn't hold a strong ref to the exception class
  // ---------------------------------------------------------------------------

  public static final class WillBeUnloadedException extends Exception {}

  @AndroidIncompatible // "Parent ClassLoader may not be null"; maybe avoidable if we try?
  public void testGetChecked_classUnloading() throws Exception {
    WeakReference<?> loaderRef = doTestClassUnloading();
    GcFinalization.awaitClear(loaderRef);
  }

  /**
   * Loads WillBeUnloadedException in a separate ClassLoader, calls
   * getChecked(future, WillBeUnloadedException.class), and returns a weak reference to the loader
   * so the caller can assert it becomes collectible. This verifies getChecked doesn't keep a strong
   * reference to the exception class.
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

  /*
   * TODO(cpovirk): It would be great to run all these tests (including class unloading) in an
   * environment that forces Futures.getChecked to its fallback WeakSetValidator. One awful way of
   * doing so would be to derive a separate test library by using remove_from_jar to strip out
   * ClassValueValidator.
   *
   * Fortunately, we get pretty good coverage "by accident": We run all these tests against the
   * backport, where ClassValueValidator is not present.
   */

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private static <V, X extends Exception> V getChecked0Sec(Future<V> future, Class<X> exceptionClass)
      throws X {
    return getChecked(future, exceptionClass, ZERO_SECONDS, SECONDS);
  }

  private static <X extends Exception> void assertInterruptedCausesGetCheckedUntimed(
      Future<?> future, Class<X> wrapper) {
    Thread.currentThread().interrupt();
    try {
      X thrown = assertThrows(wrapper, () -> getChecked(future, wrapper));
      assertThat(thrown).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      // Clear interrupt for other tests.
      Thread.interrupted();
    }
  }

  private static <X extends Exception> void assertInterruptedCausesGetCheckedTimed(
      Future<?> future, Class<X> wrapper) {
    Thread.currentThread().interrupt();
    try {
      X thrown =
          assertThrows(wrapper, () -> getChecked(future, wrapper, ZERO_SECONDS, SECONDS));
      assertThat(thrown).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      // Clear interrupt for other tests.
      Thread.interrupted();
    }
  }
}
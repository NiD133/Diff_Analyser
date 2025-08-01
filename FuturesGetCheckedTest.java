package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.util.concurrent.ClassPathUtil.parseJavaClassPath;
import static com.google.common.util.concurrent.Futures.getChecked;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.FuturesGetCheckedInputs.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertThrows;

import com.google.common.testing.GcFinalization;
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

  // Untimed get() tests

  public void testGetCheckedUntimed_success() throws TwoArgConstructorException {
    assertEquals("foo", getChecked(immediateFuture("foo"), TwoArgConstructorException.class));
  }

  public void testGetCheckedUntimed_interrupted() {
    SettableFuture<String> future = SettableFuture.create();
    Thread.currentThread().interrupt();
    try {
      getChecked(future, TwoArgConstructorException.class);
      fail();
    } catch (TwoArgConstructorException expected) {
      assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
  }

  public void testGetCheckedUntimed_cancelled() throws TwoArgConstructorException {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);
    assertThrows(
        CancellationException.class, () -> getChecked(future, TwoArgConstructorException.class));
  }

  public void testGetCheckedUntimed_executionExceptionChecked() {
    TwoArgConstructorException expected =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(expected).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_executionExceptionUnchecked()
      throws TwoArgConstructorException {
    UncheckedExecutionException expected =
        assertThrows(
            UncheckedExecutionException.class,
            () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));
    assertThat(expected).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedUntimed_executionExceptionError() throws TwoArgConstructorException {
    ExecutionError expected =
        assertThrows(
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));
    assertThat(expected).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedUntimed_executionExceptionOtherThrowable() {
    TwoArgConstructorException expected =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));
    assertThat(expected).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedUntimed_runtimeException() throws TwoArgConstructorException {
    RuntimeException expected =
        assertThrows(
            RuntimeException.class,
            () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));
    assertEquals(RUNTIME_EXCEPTION, expected);
  }

  public void testGetCheckedUntimed_error() throws TwoArgConstructorException {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class);
    } catch (Error expected) {
      assertEquals(ERROR, expected);
      return;
    }
    fail();
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
    ExceptionWithGoodAndBadConstructor expected =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class));
    assertThat(expected).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // Timed get() tests

  public void testGetCheckedTimed_success() throws TwoArgConstructorException {
    assertEquals(
        "foo", getChecked(immediateFuture("foo"), TwoArgConstructorException.class, 0, SECONDS));
  }

  public void testGetCheckedTimed_interrupted() {
    SettableFuture<String> future = SettableFuture.create();
    Thread.currentThread().interrupt();
    try {
      getChecked(future, TwoArgConstructorException.class, 0, SECONDS);
      fail();
    } catch (TwoArgConstructorException expected) {
      assertThat(expected).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
  }

  public void testGetCheckedTimed_cancelled() throws TwoArgConstructorException {
    SettableFuture<String> future = SettableFuture.create();
    future.cancel(true);
    assertThrows(
        CancellationException.class,
        () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
  }

  public void testGetCheckedTimed_executionExceptionChecked() {
    TwoArgConstructorException expected =
        assertThrows(
            TwoArgConstructorException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class, 0, SECONDS));
    assertThat(expected).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_executionExceptionUnchecked() throws TwoArgConstructorException {
    UncheckedExecutionException expected =
        assertThrows(
            UncheckedExecutionException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_UNCHECKED_EXCEPTION,
                    TwoArgConstructorException.class,
                    0,
                    SECONDS));
    assertThat(expected).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testGetCheckedTimed_executionExceptionError() throws TwoArgConstructorException {
    ExecutionError expected =
        assertThrows(
            ExecutionError.class,
            () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class, 0, SECONDS));
    assertThat(expected).hasCauseThat().isEqualTo(ERROR);
  }

  public void testGetCheckedTimed_executionExceptionOtherThrowable() {
    TwoArgConstructorException expected =
        assertThrows(
            TwoArgConstructorException.class,
            () ->
                getChecked(
                    FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class, 0, SECONDS));
    assertThat(expected).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testGetCheckedTimed_runtimeException() throws TwoArgConstructorException {
    RuntimeException expected =
        assertThrows(
            RuntimeException.class,
            () ->
                getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class, 0, SECONDS));
    assertEquals(RUNTIME_EXCEPTION, expected);
  }

  public void testGetCheckedTimed_error() throws TwoArgConstructorException {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class, 0, SECONDS);
    } catch (Error expected) {
      assertEquals(ERROR, expected);
      return;
    }
    fail();
  }

  public void testGetCheckedTimed_timeoutException() {
    SettableFuture<String> future = SettableFuture.create();
    TwoArgConstructorException expected =
        assertThrows(
            TwoArgConstructorException.class,
            () -> getChecked(future, TwoArgConstructorException.class, 0, SECONDS));
    assertThat(expected).hasCauseThat().isInstanceOf(TimeoutException.class);
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
        () ->
            getChecked(
                FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class, 1, SECONDS));
  }

  public void testGetCheckedTimed_withGoodAndBadExceptionConstructor() {
    ExceptionWithGoodAndBadConstructor expected =
        assertThrows(
            ExceptionWithGoodAndBadConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION,
                    ExceptionWithGoodAndBadConstructor.class,
                    1,
                    SECONDS));
    assertThat(expected).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // Edge case tests of the exception-construction code through untimed get():

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testGetCheckedUntimed_exceptionClassIsRuntimeException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorRuntimeException.class));
  }

  public void testGetCheckedUntimed_exceptionClassSomePrivateConstructors() {
    assertThrows(
        ExceptionWithSomePrivateConstructors.class,
        () ->
            getChecked(
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
        () ->
            getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithWrongTypesConstructor.class));
  }

  public void testGetCheckedUntimed_exceptionClassPrefersStringConstructor() {
    ExceptionWithManyConstructors expected =
        assertThrows(
            ExceptionWithManyConstructors.class,
            () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));
    assertTrue(expected.usedExpectedConstructor);
  }

  public void testGetCheckedUntimed_exceptionClassUsedInitCause() {
    ExceptionWithoutThrowableConstructor expected =
        assertThrows(
            ExceptionWithoutThrowableConstructor.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));
    assertThat(expected).hasMessageThat().contains("mymessage");
    assertThat(expected).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testPrefersConstructorWithThrowableParameter() {
    ExceptionWithManyConstructorsButOnlyOneThrowable exception =
        assertThrows(
            ExceptionWithManyConstructorsButOnlyOneThrowable.class,
            () ->
                getChecked(
                    FAILED_FUTURE_CHECKED_EXCEPTION,
                    ExceptionWithManyConstructorsButOnlyOneThrowable.class));
    assertThat(exception).hasMessageThat().contains("mymessage");
    assertThat(exception.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
  }

  // Class unloading test:

  public static final class WillBeUnloadedException extends Exception {}

  @AndroidIncompatible // "Parent ClassLoader may not be null"; maybe avoidable if we try?
  public void testGetChecked_classUnloading() throws Exception {
    WeakReference<?> classUsedByGetChecked = doTestClassUnloading();
    GcFinalization.awaitClear(classUsedByGetChecked);
  }

  /**
   * Loads {@link WillBeUnloadedException} in a separate {@code ClassLoader}, calls {@code
   * getChecked(future, WillBeUnloadedException.class)}, and returns the loader. The caller can then
   * test that the {@code ClassLoader} can still be GCed. The test amounts to a test that {@code
   * getChecked} holds no strong references to the class.
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
}
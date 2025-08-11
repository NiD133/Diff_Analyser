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
  
  // Test constants for better readability
  private static final String SUCCESS_VALUE = "foo";
  private static final String TEST_VALUE = "x";
  private static final long ZERO_TIMEOUT = 0;
  private static final long ONE_SECOND_TIMEOUT = 1;
  
  // =============================================================================
  // SUCCESS CASES - Testing successful future completion
  // =============================================================================
  
  public void testSuccessfulFuture_withoutTimeout_returnsValue() throws TwoArgConstructorException {
    String result = getChecked(immediateFuture(SUCCESS_VALUE), TwoArgConstructorException.class);
    
    assertEquals(SUCCESS_VALUE, result);
  }

  public void testSuccessfulFuture_withTimeout_returnsValue() throws TwoArgConstructorException {
    String result = getChecked(
        immediateFuture(SUCCESS_VALUE), 
        TwoArgConstructorException.class, 
        ZERO_TIMEOUT, 
        SECONDS);
    
    assertEquals(SUCCESS_VALUE, result);
  }

  // =============================================================================
  // INTERRUPTION CASES - Testing thread interruption handling
  // =============================================================================

  public void testInterruptedThread_withoutTimeout_wrapsInterruptedException() {
    SettableFuture<String> incompleteFuture = SettableFuture.create();
    Thread.currentThread().interrupt();
    
    try {
      TwoArgConstructorException exception = assertThrows(
          TwoArgConstructorException.class,
          () -> getChecked(incompleteFuture, TwoArgConstructorException.class));
      
      assertThat(exception).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue("Thread interrupt flag should be preserved", Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted(); // Clear interrupt flag
    }
  }

  public void testInterruptedThread_withTimeout_wrapsInterruptedException() {
    SettableFuture<String> incompleteFuture = SettableFuture.create();
    Thread.currentThread().interrupt();
    
    try {
      TwoArgConstructorException exception = assertThrows(
          TwoArgConstructorException.class,
          () -> getChecked(incompleteFuture, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
      
      assertThat(exception).hasCauseThat().isInstanceOf(InterruptedException.class);
      assertTrue("Thread interrupt flag should be preserved", Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted(); // Clear interrupt flag
    }
  }

  // =============================================================================
  // CANCELLATION CASES - Testing cancelled future handling
  // =============================================================================

  public void testCancelledFuture_withoutTimeout_throwsCancellationException() {
    SettableFuture<String> cancelledFuture = SettableFuture.create();
    cancelledFuture.cancel(true);
    
    assertThrows(
        CancellationException.class, 
        () -> getChecked(cancelledFuture, TwoArgConstructorException.class));
  }

  public void testCancelledFuture_withTimeout_throwsCancellationException() {
    SettableFuture<String> cancelledFuture = SettableFuture.create();
    cancelledFuture.cancel(true);
    
    assertThrows(
        CancellationException.class,
        () -> getChecked(cancelledFuture, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
  }

  // =============================================================================
  // EXECUTION EXCEPTION CASES - Testing various exception wrapping scenarios
  // =============================================================================

  public void testCheckedExecutionException_withoutTimeout_wrapsInTargetException() {
    TwoArgConstructorException wrappedException = assertThrows(
        TwoArgConstructorException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testCheckedExecutionException_withTimeout_wrapsInTargetException() {
    TwoArgConstructorException wrappedException = assertThrows(
        TwoArgConstructorException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testUncheckedException_withoutTimeout_wrapsInUncheckedExecutionException() {
    UncheckedExecutionException wrappedException = assertThrows(
        UncheckedExecutionException.class,
        () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testUncheckedException_withTimeout_wrapsInUncheckedExecutionException() {
    UncheckedExecutionException wrappedException = assertThrows(
        UncheckedExecutionException.class,
        () -> getChecked(FAILED_FUTURE_UNCHECKED_EXCEPTION, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(UNCHECKED_EXCEPTION);
  }

  public void testError_withoutTimeout_wrapsInExecutionError() {
    ExecutionError wrappedError = assertThrows(
        ExecutionError.class,
        () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class));
    
    assertThat(wrappedError).hasCauseThat().isEqualTo(ERROR);
  }

  public void testError_withTimeout_wrapsInExecutionError() {
    ExecutionError wrappedError = assertThrows(
        ExecutionError.class,
        () -> getChecked(FAILED_FUTURE_ERROR, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertThat(wrappedError).hasCauseThat().isEqualTo(ERROR);
  }

  public void testOtherThrowable_withoutTimeout_wrapsInTargetException() {
    TwoArgConstructorException wrappedException = assertThrows(
        TwoArgConstructorException.class,
        () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  public void testOtherThrowable_withTimeout_wrapsInTargetException() {
    TwoArgConstructorException wrappedException = assertThrows(
        TwoArgConstructorException.class,
        () -> getChecked(FAILED_FUTURE_OTHER_THROWABLE, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertThat(wrappedException).hasCauseThat().isEqualTo(OTHER_THROWABLE);
  }

  // =============================================================================
  // RUNTIME EXCEPTION AND ERROR PROPAGATION - Testing direct propagation
  // =============================================================================

  public void testRuntimeException_withoutTimeout_propagatesDirectly() {
    RuntimeException propagatedException = assertThrows(
        RuntimeException.class,
        () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class));
    
    assertEquals(RUNTIME_EXCEPTION, propagatedException);
  }

  public void testRuntimeException_withTimeout_propagatesDirectly() {
    RuntimeException propagatedException = assertThrows(
        RuntimeException.class,
        () -> getChecked(RUNTIME_EXCEPTION_FUTURE, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertEquals(RUNTIME_EXCEPTION, propagatedException);
  }

  public void testError_withoutTimeout_propagatesDirectly() {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class);
      fail("Expected Error to be thrown");
    } catch (Error actualError) {
      assertEquals("Error should propagate unchanged", ERROR, actualError);
    }
  }

  public void testError_withTimeout_propagatesDirectly() {
    try {
      getChecked(ERROR_FUTURE, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS);
      fail("Expected Error to be thrown");
    } catch (Error actualError) {
      assertEquals("Error should propagate unchanged", ERROR, actualError);
    }
  }

  // =============================================================================
  // TIMEOUT CASES - Testing timeout-specific behavior
  // =============================================================================

  public void testTimeout_wrapsTimeoutExceptionInTargetException() {
    SettableFuture<String> neverCompletingFuture = SettableFuture.create();
    
    TwoArgConstructorException wrappedException = assertThrows(
        TwoArgConstructorException.class,
        () -> getChecked(neverCompletingFuture, TwoArgConstructorException.class, ZERO_TIMEOUT, SECONDS));
    
    assertThat(wrappedException).hasCauseThat().isInstanceOf(TimeoutException.class);
  }

  // =============================================================================
  // EXCEPTION CONSTRUCTOR VALIDATION - Testing exception class requirements
  // =============================================================================

  public void testInvalidExceptionConstructor_withSuccessfulFuture_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture(TEST_VALUE), ExceptionWithBadConstructor.class));
  }

  public void testInvalidExceptionConstructor_withTimedSuccessfulFuture_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(immediateFuture(TEST_VALUE), ExceptionWithBadConstructor.class, ONE_SECOND_TIMEOUT, SECONDS));
  }

  public void testInvalidExceptionConstructor_withFailedFuture_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class));
  }

  public void testInvalidExceptionConstructor_withTimedFailedFuture_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithBadConstructor.class, ONE_SECOND_TIMEOUT, SECONDS));
  }

  public void testMixedConstructorQuality_withoutTimeout_usesValidConstructor() {
    ExceptionWithGoodAndBadConstructor wrappedException = assertThrows(
        ExceptionWithGoodAndBadConstructor.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class));
    
    assertThat(wrappedException).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  public void testMixedConstructorQuality_withTimeout_usesValidConstructor() {
    ExceptionWithGoodAndBadConstructor wrappedException = assertThrows(
        ExceptionWithGoodAndBadConstructor.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithGoodAndBadConstructor.class, ONE_SECOND_TIMEOUT, SECONDS));
    
    assertThat(wrappedException).hasCauseThat().isSameInstanceAs(CHECKED_EXCEPTION);
  }

  // =============================================================================
  // CONSTRUCTOR SELECTION EDGE CASES - Testing constructor selection logic
  // =============================================================================

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testRuntimeExceptionClass_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, TwoArgConstructorRuntimeException.class));
  }

  public void testSomePrivateConstructors_usesPublicConstructor() {
    assertThrows(
        ExceptionWithSomePrivateConstructors.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithSomePrivateConstructors.class));
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testAllPrivateConstructors_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithPrivateConstructor.class));
  }

  @SuppressWarnings("FuturesGetCheckedIllegalExceptionType")
  public void testWrongConstructorParameterTypes_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithWrongTypesConstructor.class));
  }

  public void testMultipleValidConstructors_prefersStringConstructor() {
    ExceptionWithManyConstructors wrappedException = assertThrows(
        ExceptionWithManyConstructors.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructors.class));
    
    assertTrue("Should use the preferred String constructor", wrappedException.usedExpectedConstructor);
  }

  public void testNoThrowableConstructor_usesInitCause() {
    ExceptionWithoutThrowableConstructor wrappedException = assertThrows(
        ExceptionWithoutThrowableConstructor.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithoutThrowableConstructor.class));
    
    assertThat(wrappedException).hasMessageThat().contains("mymessage");
    assertThat(wrappedException).hasCauseThat().isEqualTo(CHECKED_EXCEPTION);
  }

  public void testSingleThrowableConstructor_prefersThrowableOverString() {
    ExceptionWithManyConstructorsButOnlyOneThrowable wrappedException = assertThrows(
        ExceptionWithManyConstructorsButOnlyOneThrowable.class,
        () -> getChecked(FAILED_FUTURE_CHECKED_EXCEPTION, ExceptionWithManyConstructorsButOnlyOneThrowable.class));
    
    assertThat(wrappedException).hasMessageThat().contains("mymessage");
    assertThat(wrappedException.getAntecedent()).isEqualTo(CHECKED_EXCEPTION);
  }

  // =============================================================================
  // CLASS UNLOADING TEST - Testing memory leak prevention
  // =============================================================================

  public static final class WillBeUnloadedException extends Exception {}

  @AndroidIncompatible // "Parent ClassLoader may not be null"; maybe avoidable if we try?
  public void testClassUnloading_doesNotLeakClassReferences() throws Exception {
    WeakReference<?> classLoaderReference = createAndUseCustomClassLoader();
    
    GcFinalization.awaitClear(classLoaderReference);
  }

  /**
   * Creates a custom ClassLoader, loads {@link WillBeUnloadedException} from it,
   * calls getChecked with that class, and returns a weak reference to the loader.
   * This tests that getChecked doesn't hold strong references to exception classes.
   */
  private WeakReference<?> createAndUseCustomClassLoader() throws Exception {
    URLClassLoader customClassLoader = new URLClassLoader(parseJavaClassPath(), null);
    
    @SuppressWarnings("unchecked")
    Class<WillBeUnloadedException> customExceptionClass =
        (Class<WillBeUnloadedException>) Class.forName(
            WillBeUnloadedException.class.getName(), 
            false, 
            customClassLoader);
    
    // Verify we loaded a different class instance
    assertNotSame("Should load different class from custom loader", 
                  customExceptionClass, WillBeUnloadedException.class);
    
    // Use the custom class with getChecked
    getChecked(immediateFuture(SUCCESS_VALUE), customExceptionClass);
    
    return new WeakReference<>(customClassLoader);
  }

  /*
   * TODO(cpovirk): It would be great to run all these tests (including class unloading) in an
   * environment that forces Futures.getChecked to its fallback WeakSetValidator. One awful way of
   * doing so would be to derive a separate test library by using remove_from_jar to strip out
   * ClassValueValidator.
   *
   * Fortunately, we get pretty good coverage "by accident": We run all these tests against the
   * *backport*, where ClassValueValidator is not present.
   */
}
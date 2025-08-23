package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Readable, deterministic tests for FuturesGetChecked.
 *
 * This suite focuses on:
 * - Happy paths: returning values (including null) from already-completed futures.
 * - Argument validation: nulls and invalid exception classes.
 * - Exception wrapping: execution failures are rethrown as the requested checked type with the
 *   original cause attached.
 *
 * Notes:
 * - Uses simple CompletableFuture instances to avoid threads and mocking.
 * - Uses JUnit 4.13's assertThrows for clarity.
 */
public class FuturesGetCheckedTest {

  private static <T> CompletableFuture<T> failedFuture(Throwable t) {
    CompletableFuture<T> cf = new CompletableFuture<>();
    cf.completeExceptionally(t);
    return cf;
  }

  // --------------------------
  // isCheckedException
  // --------------------------

  @Test
  public void isCheckedException_recognizesCheckedAndUnchecked() {
    assertTrue(FuturesGetChecked.isCheckedException(Exception.class));          // checked
    assertTrue(FuturesGetChecked.isCheckedException(IOException.class));        // checked
    assertFalse(FuturesGetChecked.isCheckedException(RuntimeException.class));  // unchecked
  }

  // --------------------------
  // checkExceptionClassValidity
  // --------------------------

  @Test
  public void checkExceptionClassValidity_acceptsCheckedType() {
    // Does not throw for standard checked exception types
    FuturesGetChecked.checkExceptionClassValidity(Exception.class);
    FuturesGetChecked.checkExceptionClassValidity(IOException.class);
  }

  @Test
  public void checkExceptionClassValidity_rejectsNull() {
    assertThrows(
        NullPointerException.class,
        () -> FuturesGetChecked.checkExceptionClassValidity((Class<? extends Exception>) null));
  }

  @Test
  public void checkExceptionClassValidity_rejectsRuntimeExceptions() {
    // getChecked requires a checked exception type — runtime exceptions should be rejected
    assertThrows(
        IllegalArgumentException.class,
        () -> FuturesGetChecked.checkExceptionClassValidity(RuntimeException.class));
  }

  // --------------------------
  // getChecked(Future, Class)
  // --------------------------

  @Test
  public void getChecked_returnsValue_whenFutureCompletedNormally() throws Exception {
    Future<Integer> future = CompletableFuture.completedFuture(42);
    Integer result = FuturesGetChecked.getChecked(future, Exception.class);
    assertEquals(Integer.valueOf(42), result);
  }

  @Test
  public void getChecked_returnsNull_whenFutureCompletesWithNull() throws Exception {
    Future<Object> future = CompletableFuture.completedFuture(null);
    Object result = FuturesGetChecked.getChecked(future, Exception.class);
    assertNull(result);
  }

  @Test
  public void getChecked_wrapsFailureInRequestedCheckedType_preservingCause() {
    IOException cause = new IOException("boom");
    Future<Object> future = failedFuture(cause);

    Exception thrown =
        assertThrows(Exception.class, () -> FuturesGetChecked.getChecked(future, Exception.class));

    assertEquals(Exception.class, thrown.getClass());
    assertSame("Original cause should be preserved", cause, thrown.getCause());
  }

  @Test
  public void getChecked_nullFuture_throwsNPE() {
    assertThrows(
        NullPointerException.class,
        () -> FuturesGetChecked.getChecked((Future<Object>) null, Exception.class));
  }

  @Test
  public void getChecked_nullExceptionClass_throwsNPE() {
    Future<Integer> future = CompletableFuture.completedFuture(1);
    assertThrows(
        NullPointerException.class,
        () -> FuturesGetChecked.getChecked(future, (Class<Exception>) null));
  }

  // --------------------------
  // getChecked(Future, Class, timeout, unit)
  // --------------------------

  @Test
  public void getChecked_withTimeout_returnsValue_whenAlreadyDone() throws Exception {
    Future<String> future = CompletableFuture.completedFuture("ok");
    String result = FuturesGetChecked.getChecked(future, Exception.class, 10, TimeUnit.MILLISECONDS);
    assertEquals("ok", result);
  }

  @Test
  public void getChecked_withTimeout_nullFuture_throwsNPE() {
    assertThrows(
        NullPointerException.class,
        () ->
            FuturesGetChecked.getChecked(
                (Future<Object>) null, Exception.class, 1, TimeUnit.MILLISECONDS));
  }

  @Test
  public void getChecked_withTimeout_nullExceptionClass_throwsNPE() {
    Future<String> future = CompletableFuture.completedFuture("ok");
    assertThrows(
        NullPointerException.class,
        () -> FuturesGetChecked.getChecked(future, null, 1, TimeUnit.MILLISECONDS));
  }

  // --------------------------
  // Validators
  // --------------------------

  @Test
  public void weakSetValidator_validateClass_acceptsChecked_rejectsRuntime() {
    FuturesGetChecked.GetCheckedTypeValidator weakValidator = FuturesGetChecked.weakSetValidator();

    // Accepts checked
    weakValidator.validateClass(Exception.class);

    // Rejects runtime
    assertThrows(
        IllegalArgumentException.class,
        () -> weakValidator.validateClass(RuntimeException.class));
  }

  @Test
  public void classValueValidator_validateClass_acceptsChecked_rejectsRuntime() {
    // May fall back internally depending on platform, but should behave consistently.
    FuturesGetChecked.GetCheckedTypeValidator classValueValidator =
        FuturesGetChecked.classValueValidator();

    assertNotNull(classValueValidator);

    // Accepts checked
    classValueValidator.validateClass(IOException.class);

    // Rejects runtime
    assertThrows(
        IllegalArgumentException.class,
        () -> classValueValidator.validateClass(IllegalStateException.class));
  }

  @Test
  public void getChecked_withExplicitValidator_behavesLikeDefault() throws Exception {
    FuturesGetChecked.GetCheckedTypeValidator validator = FuturesGetChecked.weakSetValidator();
    Future<String> future = CompletableFuture.completedFuture("value");

    String result = FuturesGetChecked.getChecked(validator, future, Exception.class);

    assertEquals("value", result);
  }
}
package com.google.common.util.concurrent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * Tests for the Duration-based default methods on ListeningExecutorService,
 * verified via DirectExecutorService (a simple implementation).
 *
 * These tests focus on clear behavior:
 * - Null handling for Duration overloads
 * - Behavior with empty task collections
 * - awaitTermination semantics with zero timeout
 *
 * They avoid executing tasks to keep the tests simple and robust.
 */
public class ListeningExecutorServiceDurationTest {

  @Test
  public void awaitTermination_withZeroAfterShutdown_returnsTrue() throws Exception {
    DirectExecutorService executor = new DirectExecutorService();

    executor.shutdown();
    boolean terminated = executor.awaitTermination(Duration.ZERO);

    assertTrue(terminated);
  }

  @Test
  public void awaitTermination_withZeroBeforeShutdown_returnsFalse() throws Exception {
    DirectExecutorService executor = new DirectExecutorService();

    boolean terminated = executor.awaitTermination(Duration.ZERO);

    assertFalse(terminated);
  }

  @Test
  public void awaitTermination_withNullDuration_throwsNullPointerException() {
    DirectExecutorService executor = new DirectExecutorService();

    assertThrows(NullPointerException.class, () -> executor.awaitTermination(null));
  }

  @Test
  public void invokeAny_withNullDuration_throwsNullPointerException() {
    DirectExecutorService executor = new DirectExecutorService();
    Collection<Callable<Object>> tasks = new ArrayList<>(); // empty is fine for this validation

    assertThrows(NullPointerException.class, () -> executor.invokeAny(tasks, (Duration) null));
  }

  @Test
  public void invokeAny_withEmptyTasks_throwsIllegalArgumentException() {
    DirectExecutorService executor = new DirectExecutorService();
    Collection<Callable<Object>> tasks = new ArrayList<>(); // empty by design

    assertThrows(IllegalArgumentException.class, () -> executor.invokeAny(tasks, Duration.ZERO));
  }

  @Test
  public void invokeAll_withEmptyTasksAndZeroTimeout_returnsEmptyList() throws Exception {
    DirectExecutorService executor = new DirectExecutorService();
    Collection<Callable<Object>> tasks = new LinkedList<>();

    List<Future<Object>> futures = executor.invokeAll(tasks, Duration.ZERO);

    assertTrue(futures.isEmpty());
  }

  @Test
  public void invokeAll_withNullDuration_throwsNullPointerException() {
    DirectExecutorService executor = new DirectExecutorService();
    Collection<Callable<Object>> tasks = new ArrayList<>();

    assertThrows(NullPointerException.class, () -> executor.invokeAll(tasks, (Duration) null));
  }

  @Test
  public void invokeAll_withNullTasks_throwsNullPointerException() {
    DirectExecutorService executor = new DirectExecutorService();

    assertThrows(NullPointerException.class, () -> executor.invokeAll(null, Duration.ZERO));
  }
}
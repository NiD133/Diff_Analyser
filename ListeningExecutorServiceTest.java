/*
 * Copyright (C) 2020 The Guava Authors
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
import static com.google.common.util.concurrent.Futures.immediateFailedFuture;
import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public final class ListeningExecutorServiceTest extends TestCase {

  // Test constants for timeout durations
  private static final Duration TIMEOUT_DURATION_INVOKE_ANY = Duration.ofSeconds(7);
  private static final Duration TIMEOUT_DURATION_INVOKE_ALL = Duration.ofDays(365);
  private static final Duration TIMEOUT_DURATION_AWAIT_TERMINATION = Duration.ofMinutes(144);

  private Collection<? extends Callable<?>> recordedTasks;
  private long recordedTimeout;
  private TimeUnit recordedTimeUnit;

  private ListeningExecutorService executorService;

  @Override
  protected void setUp() {
    executorService = new FakeExecutorService();
  }

  /**
   * Tests that {@link ListeningExecutorService#invokeAny(Collection, Duration)}
   * correctly handles timeout conversion and task execution.
   */
  public void testInvokeAnyWithDurationTimeout() throws Exception {
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAny");

    String result = executorService.invokeAny(tasks, TIMEOUT_DURATION_INVOKE_ANY);

    assertThat(result).isEqualTo("invokeAny");
    assertRecordedTasks(tasks);
    assertRecordedTimeout(TIMEOUT_DURATION_INVOKE_ANY);
  }

  /**
   * Tests that {@link ListeningExecutorService#invokeAll(Collection, Duration)}
   * correctly handles timeout conversion and returns completed futures.
   */
  public void testInvokeAllWithDurationTimeout() throws Exception {
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAll");

    List<Future<String>> result = executorService.invokeAll(tasks, TIMEOUT_DURATION_INVOKE_ALL);

    assertThat(result).hasSize(1);
    assertThat(Futures.getDone(result.get(0))).isEqualTo("invokeAll");
    assertRecordedTasks(tasks);
    assertRecordedTimeout(TIMEOUT_DURATION_INVOKE_ALL);
  }

  /**
   * Tests that {@link ListeningExecutorService#awaitTermination(Duration)}
   * correctly converts the timeout duration.
   */
  public void testAwaitTerminationWithDurationTimeout() throws Exception {
    boolean result = executorService.awaitTermination(TIMEOUT_DURATION_AWAIT_TERMINATION);

    assertThat(result).isTrue();
    assertRecordedTimeout(TIMEOUT_DURATION_AWAIT_TERMINATION);
  }

  // Helper assertion methods
  private void assertRecordedTasks(Collection<? extends Callable<?>> expectedTasks) {
    assertThat(recordedTasks).isSameInstanceAs(expectedTasks);
  }

  private void assertRecordedTimeout(Duration expectedDuration) {
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(recordedTimeout).isEqualTo(expectedDuration.toNanos());
  }

  /**
   * Fake executor service that records parameters and returns results for testing.
   * 
   * <p>This implementation captures:
   * <ul>
   *   <li>The tasks collection passed to invokeAny/invokeAll</li>
   *   <li>The timeout parameters passed to methods</li>
   * </ul>
   * 
   * <p>It returns successful futures with the task results for testing purposes.
   */
  private class FakeExecutorService extends AbstractListeningExecutorService {
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      recordParameters(tasks, timeout, unit);
      return executeSingleTask(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
      recordParameters(tasks, timeout, unit);
      return createFutureListForSingleTask(tasks);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      recordedTimeout = timeout;
      recordedTimeUnit = unit;
      return true;
    }

    // Helper methods for FakeExecutorService
    private void recordParameters(Collection<? extends Callable<?>> tasks, long timeout, TimeUnit unit) {
      recordedTasks = tasks;
      recordedTimeout = timeout;
      recordedTimeUnit = unit;
    }

    private <T> T executeSingleTask(Collection<? extends Callable<T>> tasks) throws ExecutionException {
      try {
        return tasks.iterator().next().call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    private <T> List<Future<T>> createFutureListForSingleTask(Collection<? extends Callable<T>> tasks) {
      try {
        return Collections.singletonList(immediateFuture(tasks.iterator().next().call()));
      } catch (Exception e) {
        return Collections.singletonList(immediateFailedFuture(e));
      }
    }

    // Unimplemented methods
    @Override
    public void execute(Runnable runnable) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Runnable> shutdownNow() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShutdown() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTerminated() {
      throw new UnsupportedOperationException();
    }
  }
}
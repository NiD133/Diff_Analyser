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

/**
 * Tests that the Duration-based overloads on ListeningExecutorService delegate
 * to the long, TimeUnit methods using NANOSECONDS and preserve inputs.
 */
@NullUnmarked
public final class ListeningExecutorServiceTest extends TestCase {

  private final RecordingExecutorService executor = new RecordingExecutorService();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    executor.clearRecordings();
  }

  public void testInvokeAny_durationOverloadDelegatesWithNanos() throws Exception {
    Duration timeout = Duration.ofSeconds(7);
    Set<Callable<String>> tasks = singletonTaskReturning("invokeAny");

    String result = executor.invokeAny(tasks, timeout);

    assertThat(result).isEqualTo("invokeAny");
    assertThat(executor.lastTasks()).isSameInstanceAs(tasks);
    assertRecordedTimeoutEquals(timeout);
  }

  public void testInvokeAll_durationOverloadDelegatesWithNanos() throws Exception {
    Duration timeout = Duration.ofDays(365);
    Set<Callable<String>> tasks = singletonTaskReturning("invokeAll");

    List<Future<String>> result = executor.invokeAll(tasks, timeout);

    assertThat(result).hasSize(1);
    assertThat(Futures.getDone(result.get(0))).isEqualTo("invokeAll");
    assertThat(executor.lastTasks()).isSameInstanceAs(tasks);
    assertRecordedTimeoutEquals(timeout);
  }

  public void testAwaitTermination_durationOverloadDelegatesWithNanos() throws Exception {
    Duration timeout = Duration.ofMinutes(144);

    boolean result = executor.awaitTermination(timeout);

    assertThat(result).isTrue();
    assertRecordedTimeoutEquals(timeout);
  }

  private void assertRecordedTimeoutEquals(Duration expected) {
    assertThat(executor.lastTimeUnit()).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(executor.lastTimeoutNanos())).isEqualTo(expected);
  }

  private static <T> Set<Callable<T>> singletonTaskReturning(T value) {
    return Collections.singleton(() -> value);
  }

  /**
   * Executor that records the last tasks and timeout/unit it was invoked with,
   * then returns simple immediate results to keep tests focused on delegation.
   */
  private static final class RecordingExecutorService extends AbstractListeningExecutorService {
    private Collection<? extends Callable<?>> lastTasks;
    private long lastTimeoutNanos;
    private TimeUnit lastUnit;

    void clearRecordings() {
      lastTasks = null;
      lastTimeoutNanos = 0L;
      lastUnit = null;
    }

    Collection<? extends Callable<?>> lastTasks() {
      return lastTasks;
    }

    long lastTimeoutNanos() {
      return lastTimeoutNanos;
    }

    TimeUnit lastTimeUnit() {
      return lastUnit;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      record(tasks, timeout, unit);
      try {
        return tasks.iterator().next().call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
      record(tasks, timeout, unit);
      try {
        return Collections.singletonList(immediateFuture(tasks.iterator().next().call()));
      } catch (Exception e) {
        return Collections.singletonList(immediateFailedFuture(e));
      }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      record(null, timeout, unit);
      return true;
    }

    private void record(Collection<? extends Callable<?>> tasks, long timeout, TimeUnit unit) {
      this.lastTasks = tasks;
      this.lastTimeoutNanos = timeout;
      this.lastUnit = unit;
    }

    // Unused methods for this test.
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
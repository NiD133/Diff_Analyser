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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for the default methods in {@link ListeningExecutorService}, which provide overloads that
 * accept a {@link Duration}. This test verifies that these methods correctly delegate to their
 * corresponding {@code (long, TimeUnit)} counterparts.
 */
@RunWith(JUnit4.class)
@NullUnmarked
public final class ListeningExecutorServiceTest {

  // Fields to record the arguments passed to the (long, TimeUnit) overloads.
  private Collection<? extends Callable<?>> recordedTasks;
  private long recordedTimeoutNanos;
  private TimeUnit recordedTimeUnit;

  private final ListeningExecutorService executorService = new RecordingExecutorService();

  @Before
  public void setUp() {
    // Reset state before each test.
    recordedTasks = null;
    recordedTimeoutNanos = -1;
    recordedTimeUnit = null;
  }

  @Test
  public void invokeAny_withDuration_delegatesToTimeUnitOverload() throws Exception {
    // Arrange
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAny");
    Duration timeout = Duration.ofSeconds(7);

    // Act
    String result = executorService.invokeAny(tasks, timeout);

    // Assert
    assertThat(result).isEqualTo("invokeAny");

    // Verify correct delegation to the (long, TimeUnit) overload.
    assertThat(recordedTasks).isSameInstanceAs(tasks);
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeoutNanos)).isEqualTo(timeout);
  }

  @Test
  public void invokeAll_withDuration_delegatesToTimeUnitOverload() throws Exception {
    // Arrange
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAll");
    Duration timeout = Duration.ofDays(365);

    // Act
    List<Future<String>> result = executorService.invokeAll(tasks, timeout);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(Futures.getDone(result.get(0))).isEqualTo("invokeAll");

    // Verify correct delegation to the (long, TimeUnit) overload.
    assertThat(recordedTasks).isSameInstanceAs(tasks);
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeoutNanos)).isEqualTo(timeout);
  }

  @Test
  public void awaitTermination_withDuration_delegatesToTimeUnitOverload() throws Exception {
    // Arrange
    Duration timeout = Duration.ofMinutes(144);

    // Act
    boolean result = executorService.awaitTermination(timeout);

    // Assert
    assertThat(result).isTrue();

    // Verify correct delegation to the (long, TimeUnit) overload.
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeoutNanos)).isEqualTo(timeout);
  }

  /**
   * A test double that extends {@link AbstractListeningExecutorService} to record the arguments
   * passed to its methods. This allows us to verify that the {@code Duration}-based default methods
   * correctly delegate to the underlying {@code (long, TimeUnit)} methods.
   */
  private class RecordingExecutorService extends AbstractListeningExecutorService {
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws ExecutionException {
      recordedTasks = tasks;
      recordedTimeoutNanos = timeout;
      recordedTimeUnit = unit;
      try {
        return tasks.iterator().next().call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
      recordedTasks = tasks;
      recordedTimeoutNanos = timeout;
      recordedTimeUnit = unit;
      try {
        return Collections.singletonList(immediateFuture(tasks.iterator().next().call()));
      } catch (Exception e) {
        return Collections.singletonList(immediateFailedFuture(e));
      }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      recordedTimeoutNanos = timeout;
      recordedTimeUnit = unit;
      return true;
    }

    // Methods below are not under test and are not expected to be called.
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
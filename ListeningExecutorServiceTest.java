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
 * Tests for the Duration-based overloads in ListeningExecutorService.
 * 
 * This test verifies that the new Duration-based methods properly convert
 * Duration objects to nanoseconds when calling the underlying TimeUnit-based methods.
 */
@NullUnmarked
public final class ListeningExecutorServiceTest extends TestCase {

  private static final String INVOKE_ANY_RESULT = "invokeAny";
  private static final String INVOKE_ALL_RESULT = "invokeAll";
  private static final Duration SEVEN_SECONDS = Duration.ofSeconds(7);
  private static final Duration ONE_YEAR = Duration.ofDays(365);
  private static final Duration LONG_TIMEOUT = Duration.ofMinutes(144);

  private TestableListeningExecutorService executorService;

  @Override
  protected void setUp() {
    executorService = new TestableListeningExecutorService();
  }

  public void testInvokeAny_withDuration_convertsToNanoseconds() throws Exception {
    // Given: A task that returns a known value
    Set<Callable<String>> tasks = Collections.singleton(() -> INVOKE_ANY_RESULT);

    // When: Calling invokeAny with a Duration
    String actualResult = executorService.invokeAny(tasks, SEVEN_SECONDS);

    // Then: The result should be correct and Duration should be converted to nanoseconds
    assertThat(actualResult).isEqualTo(INVOKE_ANY_RESULT);
    assertTasksWereRecorded(tasks);
    assertDurationWasConvertedToNanoseconds(SEVEN_SECONDS);
  }

  public void testInvokeAll_withDuration_convertsToNanoseconds() throws Exception {
    // Given: A task that returns a known value
    Set<Callable<String>> tasks = Collections.singleton(() -> INVOKE_ALL_RESULT);

    // When: Calling invokeAll with a Duration
    List<Future<String>> results = executorService.invokeAll(tasks, ONE_YEAR);

    // Then: The results should be correct and Duration should be converted to nanoseconds
    assertThat(results).hasSize(1);
    assertThat(Futures.getDone(results.get(0))).isEqualTo(INVOKE_ALL_RESULT);
    assertTasksWereRecorded(tasks);
    assertDurationWasConvertedToNanoseconds(ONE_YEAR);
  }

  public void testAwaitTermination_withDuration_convertsToNanoseconds() throws Exception {
    // When: Calling awaitTermination with a Duration
    boolean terminationResult = executorService.awaitTermination(LONG_TIMEOUT);

    // Then: The result should be true and Duration should be converted to nanoseconds
    assertThat(terminationResult).isTrue();
    assertDurationWasConvertedToNanoseconds(LONG_TIMEOUT);
  }

  private void assertTasksWereRecorded(Collection<? extends Callable<?>> expectedTasks) {
    assertThat(executorService.getRecordedTasks()).isSameInstanceAs(expectedTasks);
  }

  private void assertDurationWasConvertedToNanoseconds(Duration expectedDuration) {
    assertThat(executorService.getRecordedTimeUnit()).isEqualTo(NANOSECONDS);
    Duration actualDuration = Duration.ofNanos(executorService.getRecordedTimeout());
    assertThat(actualDuration).isEqualTo(expectedDuration);
  }

  /**
   * A test double that records method calls and provides predictable responses.
   * This allows us to verify that Duration-based methods properly delegate to
   * the underlying TimeUnit-based methods with correct conversions.
   */
  private static class TestableListeningExecutorService extends AbstractListeningExecutorService {
    private Collection<? extends Callable<?>> recordedTasks;
    private long recordedTimeout;
    private TimeUnit recordedTimeUnit;

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      recordMethodCall(tasks, timeout, unit);
      return executeFirstTask(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(
        Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException {
      recordMethodCall(tasks, timeout, unit);
      return createFutureListFromTasks(tasks);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      recordTimeoutCall(timeout, unit);
      return true; // Always return true for testing
    }

    // Helper methods for recording method calls
    private void recordMethodCall(Collection<? extends Callable<?>> tasks, long timeout, TimeUnit unit) {
      this.recordedTasks = tasks;
      recordTimeoutCall(timeout, unit);
    }

    private void recordTimeoutCall(long timeout, TimeUnit unit) {
      this.recordedTimeout = timeout;
      this.recordedTimeUnit = unit;
    }

    // Helper methods for creating responses
    private <T> T executeFirstTask(Collection<? extends Callable<T>> tasks) 
        throws ExecutionException {
      try {
        return tasks.iterator().next().call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    private <T> List<Future<T>> createFutureListFromTasks(Collection<? extends Callable<T>> tasks) {
      try {
        T result = tasks.iterator().next().call();
        return Collections.singletonList(immediateFuture(result));
      } catch (Exception e) {
        return Collections.singletonList(immediateFailedFuture(e));
      }
    }

    // Getters for test verification
    public Collection<? extends Callable<?>> getRecordedTasks() {
      return recordedTasks;
    }

    public long getRecordedTimeout() {
      return recordedTimeout;
    }

    public TimeUnit getRecordedTimeUnit() {
      return recordedTimeUnit;
    }

    // Unsupported operations (not needed for these tests)
    @Override
    public void execute(Runnable runnable) {
      throw new UnsupportedOperationException("Not needed for Duration conversion tests");
    }

    @Override
    public void shutdown() {
      throw new UnsupportedOperationException("Not needed for Duration conversion tests");
    }

    @Override
    public List<Runnable> shutdownNow() {
      throw new UnsupportedOperationException("Not needed for Duration conversion tests");
    }

    @Override
    public boolean isShutdown() {
      throw new UnsupportedOperationException("Not needed for Duration conversion tests");
    }

    @Override
    public boolean isTerminated() {
      throw new UnsupportedOperationException("Not needed for Duration conversion tests");
    }
  }
}
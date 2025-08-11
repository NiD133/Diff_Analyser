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

  // Variables to record task details for verification
  private Collection<? extends Callable<?>> recordedTasks;
  private long recordedTimeout;
  private TimeUnit recordedTimeUnit;

  // Fake executor service to simulate task execution
  private final ListeningExecutorService executorService = new FakeExecutorService();

  // Test for the invokeAny method with a single task
  public void testInvokeAny() throws Exception {
    // Arrange: Create a set with a single task
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAny");

    // Act: Invoke any task with a timeout
    String result = executorService.invokeAny(tasks, Duration.ofSeconds(7));

    // Assert: Verify the result and recorded task details
    assertThat(result).isEqualTo("invokeAny");
    assertThat(recordedTasks).isSameInstanceAs(tasks);
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeout)).isEqualTo(Duration.ofSeconds(7));
  }

  // Test for the invokeAll method with a single task
  public void testInvokeAll() throws Exception {
    // Arrange: Create a set with a single task
    Set<Callable<String>> tasks = Collections.singleton(() -> "invokeAll");

    // Act: Invoke all tasks with a timeout
    List<Future<String>> result = executorService.invokeAll(tasks, Duration.ofDays(365));

    // Assert: Verify the result and recorded task details
    assertThat(result).hasSize(1);
    assertThat(Futures.getDone(result.get(0))).isEqualTo("invokeAll");
    assertThat(recordedTasks).isSameInstanceAs(tasks);
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeout)).isEqualTo(Duration.ofDays(365));
  }

  // Test for the awaitTermination method
  public void testAwaitTermination() throws Exception {
    // Act: Await termination with a timeout
    boolean result = executorService.awaitTermination(Duration.ofMinutes(144));

    // Assert: Verify the result and recorded timeout details
    assertThat(result).isTrue();
    assertThat(recordedTimeUnit).isEqualTo(NANOSECONDS);
    assertThat(Duration.ofNanos(recordedTimeout)).isEqualTo(Duration.ofMinutes(144));
  }

  // Fake executor service to simulate task execution and record details
  private class FakeExecutorService extends AbstractListeningExecutorService {
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      recordedTasks = tasks;
      recordedTimeout = timeout;
      recordedTimeUnit = unit;
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
      recordedTasks = tasks;
      recordedTimeout = timeout;
      recordedTimeUnit = unit;
      try {
        return Collections.singletonList(immediateFuture(tasks.iterator().next().call()));
      } catch (Exception e) {
        return Collections.singletonList(immediateFailedFuture(e));
      }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      recordedTimeout = timeout;
      recordedTimeUnit = unit;
      return true;
    }

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
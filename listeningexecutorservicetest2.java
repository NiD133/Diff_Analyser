package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for the default methods in {@link ListeningExecutorService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListeningExecutorServiceTestTest2 {

  // We mock the abstract class, not the interface, to allow testing the interface's
  // default methods.
  @Mock
  private AbstractListeningExecutorService executorService;

  @Test
  public void invokeAll_withDuration_delegatesToNanosOverload() throws Exception {
    // Arrange
    Set<Callable<String>> tasks = ImmutableSet.of(() -> "test");
    Duration timeout = Duration.ofDays(365);
    List<Future<String>> expectedResult = ImmutableList.of(Futures.immediateFuture("test"));

    // Tell Mockito to execute the real default method implementation on the interface.
    when(executorService.invokeAll(any(), any(Duration.class))).thenCallRealMethod();

    // Stub the method that the default method is expected to delegate to.
    when(executorService.invokeAll(any(), anyLong(), any(TimeUnit.class)))
        .thenReturn(expectedResult);

    // Act
    List<Future<String>> actualResult = executorService.invokeAll(tasks, timeout);

    // Assert
    assertThat(actualResult).isSameInstanceAs(expectedResult);

    // Capture the arguments passed to the delegated-to method to verify them.
    ArgumentCaptor<Collection<? extends Callable<String>>> tasksCaptor =
        ArgumentCaptor.forClass(Collection.class);
    ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<TimeUnit> unitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

    verify(executorService)
        .invokeAll(tasksCaptor.capture(), timeoutCaptor.capture(), unitCaptor.capture());

    assertThat(tasksCaptor.getValue()).isSameInstanceAs(tasks);
    assertThat(unitCaptor.getValue()).isEqualTo(NANOSECONDS);
    assertThat(timeoutCaptor.getValue()).isEqualTo(timeout.toNanos());
  }
}
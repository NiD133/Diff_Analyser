package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for the default methods in {@link ListeningExecutorService} that accept a {@link Duration}.
 */
@ExtendWith(MockitoExtension.class)
class ListeningExecutorServiceTest {

  @Mock
  private ListeningExecutorService executorService;

  @Test
  @DisplayName("invokeAny() with a Duration should delegate to the TimeUnit-based overload")
  void invokeAny_withDuration_delegatesToTimeUnitOverload() throws Exception {
    // Arrange
    Set<Callable<String>> tasks = Collections.singleton(() -> "success");
    Duration timeout = Duration.ofSeconds(7);
    String expectedResult = "success";

    // We need to test the default interface method.
    // 1. Tell Mockito to call the real `invokeAny(Duration)` default method.
    when(executorService.invokeAny(any(), any(Duration.class))).thenCallRealMethod();
    // 2. Stub the underlying `invokeAny(long, TimeUnit)` method that the default method calls.
    when(executorService.invokeAny(any(), any(long.class), any(TimeUnit.class)))
        .thenReturn(expectedResult);

    // Act
    String actualResult = executorService.invokeAny(tasks, timeout);

    // Assert
    assertThat(actualResult).isEqualTo(expectedResult);

    // Verify that the default method correctly delegated the call to the TimeUnit-based overload
    // with the timeout converted to nanoseconds.
    ArgumentCaptor<Collection<? extends Callable<String>>> tasksCaptor =
        ArgumentCaptor.forClass(Collection.class);
    ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<TimeUnit> unitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

    verify(executorService)
        .invokeAny(tasksCaptor.capture(), timeoutCaptor.capture(), unitCaptor.capture());

    assertThat(tasksCaptor.getValue()).isSameInstanceAs(tasks);
    assertThat(timeoutCaptor.getValue()).isEqualTo(timeout.toNanos());
    assertThat(unitCaptor.getValue()).isEqualTo(TimeUnit.NANOSECONDS);
  }
}
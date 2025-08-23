package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the default {@link Duration}-based overload methods on {@link ListeningExecutorService}.
 */
@RunWith(JUnit4.class)
public class ListeningExecutorServiceDurationOverloadTest {

  private final SpyingExecutorService spiedExecutorService = new SpyingExecutorService();
  private final ListeningExecutorService executorService = spiedExecutorService;

  /**
   * A test spy that extends {@link AbstractListeningExecutorService} to record the arguments passed
   * to the legacy {@code awaitTermination(long, TimeUnit)} method.
   */
  private static class SpyingExecutorService extends AbstractListeningExecutorService {

    private long recordedTimeout;
    private TimeUnit recordedTimeUnit;
    private boolean awaitTerminationCalled = false;

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      this.awaitTerminationCalled = true;
      this.recordedTimeout = timeout;
      this.recordedTimeUnit = unit;
      return true; // Return a fixed value for the test to verify pass-through.
    }

    // The following methods must be implemented but are not used in this test.
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

  @Test
  public void awaitTermination_withDuration_delegatesToLegacyOverload() throws InterruptedException {
    // Arrange
    Duration timeoutDuration = Duration.ofMinutes(144);

    // Act: Call the Duration-based default method.
    boolean result = executorService.awaitTermination(timeoutDuration);

    // Assert: Verify it correctly delegated to the (long, TimeUnit) overload.
    assertThat(result).isTrue();
    assertThat(spiedExecutorService.awaitTerminationCalled).isTrue();

    // The core assertion: The default method should convert the Duration to nanoseconds.
    assertThat(spiedExecutorService.recordedTimeUnit).isEqualTo(TimeUnit.NANOSECONDS);
    assertThat(spiedExecutorService.recordedTimeout).isEqualTo(timeoutDuration.toNanos());
  }
}
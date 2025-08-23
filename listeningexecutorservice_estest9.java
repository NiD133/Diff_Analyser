package com.google.common.util.concurrent;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertFalse;

/**
 * Tests for implementations of {@link ListeningExecutorService}.
 */
public class ListeningExecutorServiceTest {

    /**
     * Verifies that awaitTermination() returns false immediately when called with a zero timeout
     * on a service that has not been shut down.
     */
    @Test
    public void awaitTermination_withZeroTimeoutOnRunningService_returnsFalse() throws InterruptedException {
        // Arrange: Create a running executor service.
        // DirectExecutorService is a simple implementation that runs tasks in the calling thread.
        ListeningExecutorService executorService = new DirectExecutorService();
        Duration zeroTimeout = Duration.ZERO;

        // Act: Call awaitTermination with a zero timeout. The service has not been shut down,
        // so it should not report as terminated. The zero timeout ensures the call returns instantly.
        boolean isTerminated = executorService.awaitTermination(zeroTimeout);

        // Assert: The service should not have terminated.
        assertFalse("Expected awaitTermination to return false for a running service with a zero timeout", isTerminated);
    }
}
package com.google.common.util.concurrent;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import org.junit.Test;

/**
 * Tests for implementations of {@link ListeningExecutorService}, focusing on the behavior of
 * {@link DirectExecutorService}.
 */
public class ListeningExecutorServiceTest {

    /**
     * Verifies that a shutdown DirectExecutorService terminates immediately.
     */
    @Test
    public void awaitTermination_onShutdownDirectExecutorService_returnsTrueImmediately()
            throws InterruptedException {
        // Arrange
        // A DirectExecutorService runs tasks on the calling thread and has no thread pool to manage.
        DirectExecutorService executorService = new DirectExecutorService();
        executorService.shutdown();

        // Act
        // Since the service is shut down and has no active tasks, awaitTermination with a
        // zero timeout should return true, indicating it has terminated.
        boolean isTerminated = executorService.awaitTermination(Duration.ZERO);

        // Assert
        assertTrue(
                "A shutdown DirectExecutorService should report as terminated immediately.", isTerminated);
    }
}
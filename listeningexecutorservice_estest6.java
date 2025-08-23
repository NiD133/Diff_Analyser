package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import java.time.Duration;
import org.junit.Test;

/**
 * Tests for the default methods in {@link ListeningExecutorService},
 * focusing on contract adherence like null-handling.
 */
public class ListeningExecutorServiceTest {

    /**
     * Verifies that calling awaitTermination with a null Duration
     * throws a NullPointerException, as expected for methods that
     * don't explicitly allow null arguments.
     */
    @Test
    public void awaitTermination_withNullDuration_throwsNullPointerException() throws InterruptedException {
        // Arrange: Create an instance of a ListeningExecutorService.
        // DirectExecutorService is a simple, same-thread implementation suitable for this test.
        ListeningExecutorService executorService = new DirectExecutorService();

        // Act & Assert: Verify that passing a null duration results in a NullPointerException.
        // The assertThrows construct clearly defines the action and the expected exception in one step.
        assertThrows(NullPointerException.class, () -> executorService.awaitTermination((Duration) null));
    }
}
package com.google.common.util.concurrent;

import org.junit.Test;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for implementations of {@link ListeningExecutorService}.
 */
public class ListeningExecutorServiceTest {

    /**
     * This test verifies that calling invokeAny results in a NoClassDefFoundError
     * if a core internal dependency is missing from the classpath.
     *
     * This is an edge-case test, likely originating from an auto-generation tool
     * that discovered a brittle dependency. It confirms that the service fails predictably
     * in a broken environment.
     */
    @Test(timeout = 4000)
    public void invokeAny_whenDependencyIsMissing_throwsNoClassDefFoundError() {
        // Arrange: Set up the executor service and a task.
        // DirectExecutorService is a simple implementation that runs tasks on the calling thread.
        ListeningExecutorService executorService = new DirectExecutorService();

        // Create a mock task. Its behavior is irrelevant as the code fails before execution.
        @SuppressWarnings("unchecked") // Required for mocking a generic type
        Callable<Integer> mockTask = mock(Callable.class);
        Collection<Callable<Integer>> tasks = Collections.singletonList(mockTask);
        Duration timeout = Duration.ofSeconds(10);

        // Act & Assert: Expect an error due to the missing class.
        try {
            executorService.invokeAny(tasks, timeout);
            fail("Expected a NoClassDefFoundError, but no exception was thrown.");
        } catch (NoClassDefFoundError e) {
            // The AbstractListeningExecutorService implementation relies on TrustedListenableFutureTask.
            // This assertion confirms that the error is caused by this specific missing class.
            String expectedMissingClass = "com/google/common/util/concurrent/TrustedListenableFutureTask";
            String actualMessage = e.getMessage();

            assertTrue(
                "The error message should identify the missing class. "
                    + "Expected to contain: <" + expectedMissingClass + ">, but was: <" + actualMessage + ">",
                actualMessage != null && actualMessage.contains(expectedMissingClass)
            );
        }
    }
}
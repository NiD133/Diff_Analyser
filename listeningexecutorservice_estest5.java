package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import org.junit.Test;

/**
 * This test class contains tests for ListeningExecutorService implementations.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class ListeningExecutorService_ESTestTest5 extends ListeningExecutorService_ESTest_scaffolding {

    /**
     * Verifies that invokeAll throws a NoClassDefFoundError if a required internal class,
     * TrustedListenableFutureTask, is not available on the classpath.
     *
     * This is an unusual but important test for verifying runtime environment integrity
     * and packaging, ensuring that the necessary internal dependencies are present.
     */
    @Test
    public void invokeAll_whenDependencyIsMissing_throwsNoClassDefFoundError() {
        // Arrange: Set up the executor, a task, and a timeout.
        // We use DirectExecutorService as a concrete implementation of ListeningExecutorService.
        ListeningExecutorService executorService = new DirectExecutorService();
        Callable<Object> mockTask = mock(Callable.class);
        Collection<Callable<Object>> tasks = Collections.singleton(mockTask);
        Duration timeout = Duration.ZERO;

        // Act & Assert: Expect a NoClassDefFoundError when calling invokeAll.
        // This error is expected because the underlying implementation of invokeAll
        // in AbstractListeningExecutorService depends on the TrustedListenableFutureTask class,
        // which is assumed to be missing in the test's runtime environment.
        NoClassDefFoundError thrown = assertThrows(
                NoClassDefFoundError.class,
                () -> executorService.invokeAll(tasks, timeout));

        // Verify that the error message clearly indicates the missing class.
        String expectedMissingClass = "com/google/common/util/concurrent/TrustedListenableFutureTask";
        assertTrue(
                "The error message should identify the missing class: " + expectedMissingClass,
                thrown.getMessage().contains(expectedMissingClass));
    }
}
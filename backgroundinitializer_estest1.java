package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The test class name and inheritance are preserved from the original file.
public class BackgroundInitializer_ESTestTest1 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that BackgroundInitializer.start() propagates a RejectedExecutionException
     * if the provided ExecutorService fails to create a thread for the background task.
     */
    @Test(expected = RejectedExecutionException.class)
    public void startShouldThrowRejectedExecutionExceptionWhenExecutorCannotCreateThread() {
        // Arrange

        // 1. Create a ThreadFactory that always fails to create a new thread.
        // This is the condition that will cause the executor to reject the task.
        final ThreadFactory failingThreadFactory = mock(ThreadFactory.class);
        when(failingThreadFactory.newThread(any(Runnable.class))).thenReturn(null);

        // 2. Create an executor that uses the failing factory.
        // A SynchronousQueue has no capacity, forcing the executor to reject the task
        // immediately if a new thread cannot be created.
        final ExecutorService executor = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                failingThreadFactory);

        // 3. Create the BackgroundInitializer with the faulty executor.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>(executor);

        // Act
        // Attempt to start the initialization. This will submit a task to the
        // executor, which will fail to create a thread and thus reject the task.
        initializer.start();

        // Assert
        // The @Test(expected) annotation asserts that a RejectedExecutionException is thrown.
        // No further assertions are needed.
    }
}
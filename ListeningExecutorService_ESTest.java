/*
 * Copyright (C) 2010 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.junit.Test;

/**
 * Tests for the default methods in {@link ListeningExecutorService},
 * using {@link DirectExecutorService} as the test implementation.
 * DirectExecutorService is suitable here as it's a simple, synchronous
 * implementation that allows for predictable testing of the interface's contracts.
 */
public class ListeningExecutorServiceTest {

    @Test
    public void awaitTermination_afterShutdown_returnsTrue() throws InterruptedException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        executor.shutdown();

        // Act
        // A DirectExecutorService terminates instantly after shutdown.
        boolean terminated = executor.awaitTermination(Duration.ZERO);

        // Assert
        assertTrue("Executor should be terminated immediately after shutdown", terminated);
    }

    @Test
    public void awaitTermination_withoutShutdown_returnsFalse() throws InterruptedException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();

        // Act
        // The service has not been shut down, so it cannot terminate.
        boolean terminated = executor.awaitTermination(Duration.ZERO);

        // Assert
        assertFalse("Executor should not terminate if it has not been shut down", terminated);
    }

    @Test(expected = NullPointerException.class)
    public void awaitTermination_withNullDuration_throwsNullPointerException() throws InterruptedException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();

        // Act
        executor.awaitTermination(null);
    }

    @Test
    public void invokeAllWithDuration_withEmptyTasks_returnsEmptyList() throws InterruptedException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        List<Callable<String>> emptyTasks = Collections.emptyList();

        // Act
        List<Future<String>> futures = executor.invokeAll(emptyTasks, Duration.ofSeconds(1));

        // Assert
        assertTrue("invokeAll with an empty task collection should return an empty list", futures.isEmpty());
    }

    @Test
    public void invokeAllWithDuration_withTasks_executesAndReturnsCompletedFutures()
            throws InterruptedException, ExecutionException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        Callable<String> task = () -> "success";
        List<Callable<String>> tasks = Collections.singletonList(task);

        // Act
        List<Future<String>> futures = executor.invokeAll(tasks, Duration.ofSeconds(1));

        // Assert
        assertEquals("Should return one future for the one task submitted", 1, futures.size());
        Future<String> future = futures.get(0);
        assertTrue("Future should be done as DirectExecutorService is synchronous", future.isDone());
        assertEquals("Future should contain the result of the task", "success", future.get());
    }

    @Test(expected = NullPointerException.class)
    public void invokeAllWithDuration_withNullTasks_throwsNullPointerException() throws InterruptedException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();

        // Act
        executor.invokeAll(null, Duration.ofSeconds(1));
    }

    @Test
    public void invokeAnyWithDuration_withTask_executesAndReturnsResult()
            throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        Callable<String> task = () -> "success";
        List<Callable<String>> tasks = Collections.singletonList(task);

        // Act
        String result = executor.invokeAny(tasks, Duration.ofSeconds(1));

        // Assert
        assertEquals("Should return the result of the successfully executed task", "success", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeAnyWithDuration_withEmptyTasks_throwsIllegalArgumentException()
            throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        List<Callable<Object>> emptyTasks = Collections.emptyList();

        // Act
        executor.invokeAny(emptyTasks, Duration.ofSeconds(1));
    }

    @Test(expected = NullPointerException.class)
    public void invokeAnyWithDuration_withNullDuration_throwsNullPointerException()
            throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        DirectExecutorService executor = new DirectExecutorService();
        List<Callable<String>> tasks = Collections.singletonList(() -> "success");

        // Act
        executor.invokeAny(tasks, null);
    }
}
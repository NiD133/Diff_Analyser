package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ExecutionList}.
 */
@RunWith(JUnit4.class)
public class ExecutionListTest {

    @Test
    public void execute_withMultipleRunnables_executesAllRunnables() throws InterruptedException {
        // Arrange
        final int numberOfRunnables = 3;
        ExecutionList executionList = new ExecutionList();
        Executor executor = Executors.newCachedThreadPool();
        CountDownLatch executionLatch = new CountDownLatch(numberOfRunnables);

        // Add multiple runnables that each count down the latch upon execution.
        for (int i = 0; i < numberOfRunnables; i++) {
            executionList.add(executionLatch::countDown, executor);
        }

        assertEquals(
            "Latch count should match the number of runnables before execution.",
            numberOfRunnables,
            (int) executionLatch.getCount());

        // Act
        executionList.execute();

        // Assert
        // Await completion of all runnables, with a timeout to prevent the test from hanging.
        boolean allExecuted = executionLatch.await(1, TimeUnit.SECONDS);
        assertTrue("All runnables should have been executed within the timeout.", allExecuted);
    }
}
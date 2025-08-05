package com.google.common.util.concurrent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for ExecutionList class.
 * 
 * ExecutionList manages a collection of Runnable tasks with their associated Executors,
 * ensuring all tasks are executed when execute() is called, even if added after execution begins.
 */
public class ExecutionListTest {

    private ExecutionList executionList;
    
    @Mock
    private Executor mockExecutor;
    
    @Mock 
    private Runnable mockRunnable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executionList = new ExecutionList();
    }

    @Test
    public void testCreateEmptyExecutionList() {
        // Given: A new ExecutionList is created
        ExecutionList newList = new ExecutionList();
        
        // When: execute() is called on empty list
        newList.execute();
        
        // Then: No exceptions should be thrown
        // This test verifies that an empty ExecutionList can be safely executed
    }

    @Test
    public void testAddSingleRunnableBeforeExecution() throws InterruptedException {
        // Given: A runnable that sets a flag when executed
        final AtomicInteger executionCount = new AtomicInteger(0);
        Runnable testRunnable = new Runnable() {
            @Override
            public void run() {
                executionCount.incrementAndGet();
            }
        };
        
        // When: Adding runnable before calling execute()
        executionList.add(testRunnable, MoreExecutors.directExecutor());
        executionList.execute();
        
        // Then: The runnable should be executed exactly once
        assertEquals("Runnable should be executed exactly once", 1, executionCount.get());
    }

    @Test
    public void testAddMultipleRunnablesBeforeExecution() throws InterruptedException {
        // Given: Multiple runnables that increment a shared counter
        final AtomicInteger executionCount = new AtomicInteger(0);
        final int numberOfRunnables = 3;
        
        for (int i = 0; i < numberOfRunnables; i++) {
            Runnable testRunnable = new Runnable() {
                @Override
                public void run() {
                    executionCount.incrementAndGet();
                }
            };
            executionList.add(testRunnable, MoreExecutors.directExecutor());
        }
        
        // When: execute() is called
        executionList.execute();
        
        // Then: All runnables should be executed
        assertEquals("All runnables should be executed", numberOfRunnables, executionCount.get());
    }

    @Test
    public void testAddRunnableAfterExecution() throws InterruptedException {
        // Given: ExecutionList that has already been executed
        executionList.execute();
        
        final AtomicInteger executionCount = new AtomicInteger(0);
        Runnable lateRunnable = new Runnable() {
            @Override
            public void run() {
                executionCount.incrementAndGet();
            }
        };
        
        // When: Adding a runnable after execution has begun
        executionList.add(lateRunnable, MoreExecutors.directExecutor());
        
        // Then: The late runnable should still be executed immediately
        assertEquals("Late runnable should be executed immediately", 1, executionCount.get());
    }

    @Test
    public void testExecuteIsIdempotent() throws InterruptedException {
        // Given: A runnable that counts executions
        final AtomicInteger executionCount = new AtomicInteger(0);
        Runnable countingRunnable = new Runnable() {
            @Override
            public void run() {
                executionCount.incrementAndGet();
            }
        };
        
        executionList.add(countingRunnable, MoreExecutors.directExecutor());
        
        // When: execute() is called multiple times
        executionList.execute();
        executionList.execute();
        executionList.execute();
        
        // Then: Runnable should only be executed once
        assertEquals("execute() should be idempotent - runnable executed only once", 
                    1, executionCount.get());
    }

    @Test
    public void testConcurrentExecution() throws InterruptedException {
        // Given: Multiple runnables with a countdown latch to synchronize
        final int numberOfRunnables = 5;
        final CountDownLatch latch = new CountDownLatch(numberOfRunnables);
        final List<String> executionOrder = new ArrayList<>();
        
        for (int i = 0; i < numberOfRunnables; i++) {
            final int runnableId = i;
            Runnable testRunnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (executionOrder) {
                        executionOrder.add("Runnable-" + runnableId);
                    }
                    latch.countDown();
                }
            };
            executionList.add(testRunnable, MoreExecutors.directExecutor());
        }
        
        // When: execute() is called
        executionList.execute();
        
        // Then: All runnables should complete within reasonable time
        assertTrue("All runnables should complete within 5 seconds", 
                  latch.await(5, TimeUnit.SECONDS));
        assertEquals("All runnables should have executed", numberOfRunnables, executionOrder.size());
    }

    @Test
    public void testRunnableWithDifferentExecutors() throws InterruptedException {
        // Given: Runnables with different executors
        final CountDownLatch latch = new CountDownLatch(2);
        final List<String> executorTypes = new ArrayList<>();
        
        Runnable directRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (executorTypes) {
                    executorTypes.add("direct");
                }
                latch.countDown();
            }
        };
        
        Runnable sameThreadRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (executorTypes) {
                    executorTypes.add("sameThread");
                }
                latch.countDown();
            }
        };
        
        // When: Adding runnables with different executors
        executionList.add(directRunnable, MoreExecutors.directExecutor());
        executionList.add(sameThreadRunnable, MoreExecutors.newDirectExecutorService());
        executionList.execute();
        
        // Then: Both runnables should execute
        assertTrue("Both runnables should complete", latch.await(5, TimeUnit.SECONDS));
        assertEquals("Both executor types should be used", 2, executorTypes.size());
    }

    @Test
    public void testNullRunnableThrowsException() {
        // Given: null runnable
        // When & Then: Adding null runnable should throw NullPointerException
        try {
            executionList.add(null, MoreExecutors.directExecutor());
            fail("Expected NullPointerException when adding null runnable");
        } catch (NullPointerException expected) {
            // Expected behavior
        }
    }

    @Test
    public void testNullExecutorThrowsException() {
        // Given: null executor
        // When & Then: Adding runnable with null executor should throw NullPointerException
        try {
            executionList.add(mockRunnable, null);
            fail("Expected NullPointerException when adding null executor");
        } catch (NullPointerException expected) {
            // Expected behavior
        }
    }

    @Test
    public void testExecutorExceptionHandling() {
        // Given: An executor that throws an exception
        Executor throwingExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                throw new RuntimeException("Executor failed");
            }
        };
        
        // When: Adding runnable with throwing executor and executing
        executionList.add(mockRunnable, throwingExecutor);
        
        // Then: execute() should not throw exception (exceptions should be caught and logged)
        try {
            executionList.execute();
            // If we reach here, the exception was properly handled
        } catch (Exception e) {
            fail("ExecutionList should handle executor exceptions gracefully, but threw: " + e);
        }
    }
}
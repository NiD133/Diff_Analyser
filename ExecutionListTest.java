/*
 * Copyright (C) 2007 The Guava Authors
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

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.testing.NullPointerTester;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link ExecutionList}.
 *
 * @author Nishant Thakkar
 * @author Sven Mawson
 */
@NullUnmarked
public class ExecutionListTest extends TestCase {

  private static final int TIMEOUT_SECONDS = 1;
  
  private ExecutionList executionList;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    executionList = new ExecutionList();
  }

  public void testExecute_runsAllAddedRunnables() throws Exception {
    // Given: An execution list with multiple runnables added
    Executor threadPoolExecutor = newCachedThreadPool();
    CountDownLatch completionLatch = new CountDownLatch(3);
    
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    
    // Verify initial state - no runnables have executed yet
    assertEquals("All runnables should be pending", 3L, completionLatch.getCount());

    // When: Execute is called
    executionList.execute();

    // Then: All runnables should execute within the timeout
    assertTrue("All runnables should complete within timeout", 
               completionLatch.await(TIMEOUT_SECONDS, SECONDS));
  }

  public void testExecute_isIdempotent() {
    // Given: An execution list with one runnable that tracks execution count
    AtomicInteger executionCount = new AtomicInteger(0);
    Runnable countingRunnable = () -> executionCount.incrementAndGet();
    
    executionList.add(countingRunnable, directExecutor());

    // When: Execute is called multiple times
    executionList.execute();
    executionList.execute();

    // Then: The runnable should only execute once
    assertEquals("Runnable should execute exactly once despite multiple execute() calls", 
                 1, executionCount.get());
  }

  public void testExecute_isIdempotentUnderConcurrency() throws InterruptedException {
    // Given: An execution list with a runnable that waits for a signal
    CountDownLatch startSignal = new CountDownLatch(1);
    AtomicInteger executionCount = new AtomicInteger(0);
    
    Runnable waitingRunnable = () -> {
      try {
        startSignal.await();
        executionCount.incrementAndGet();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting", e);
      }
    };
    
    executionList.add(waitingRunnable, directExecutor());

    // When: Multiple threads call execute() concurrently
    Thread firstExecutorThread = new Thread(() -> executionList.execute());
    Thread secondExecutorThread = new Thread(() -> executionList.execute());
    
    firstExecutorThread.start();
    secondExecutorThread.start();
    
    // Verify runnable hasn't executed yet
    assertEquals("Runnable should not execute before signal", 0, executionCount.get());
    
    // Signal the runnable to proceed and wait for threads to complete
    startSignal.countDown();
    firstExecutorThread.join();
    secondExecutorThread.join();

    // Then: The runnable should execute exactly once despite concurrent execute() calls
    assertEquals("Runnable should execute exactly once despite concurrent execute() calls", 
                 1, executionCount.get());
  }

  public void testAdd_afterExecute_runsImmediately() throws Exception {
    // Given: An execution list that has already been executed
    executeListWithDummyRunnables();

    // When: A new runnable is added after execute() has been called
    CountDownLatch newRunnableLatch = new CountDownLatch(1);
    executionList.add(createCountDownRunnable(newRunnableLatch), newCachedThreadPool());

    // Then: The new runnable should execute immediately without calling execute() again
    assertTrue("Runnable added after execute() should run immediately", 
               newRunnableLatch.await(TIMEOUT_SECONDS, SECONDS));
  }

  public void testExecute_maintainsRunnableOrder() throws Exception {
    // Given: An execution list with runnables that must execute in order
    AtomicInteger executionCounter = new AtomicInteger(0);
    int numberOfRunnables = 10;
    
    for (int expectedExecutionOrder = 0; expectedExecutionOrder < numberOfRunnables; expectedExecutionOrder++) {
      final int expectedCount = expectedExecutionOrder;
      Runnable orderVerifyingRunnable = () -> {
        // This runnable will only increment if it executes in the correct order
        executionCounter.compareAndSet(expectedCount, expectedCount + 1);
      };
      executionList.add(orderVerifyingRunnable, directExecutor());
    }

    // When: Execute is called
    executionList.execute();

    // Then: All runnables should have executed in the correct order
    assertEquals("All runnables should execute in the order they were added", 
                 numberOfRunnables, executionCounter.get());
  }

  public void testExecute_catchesRunnableExceptions() {
    // Given: An execution list with a runnable that throws an exception
    executionList.add(createThrowingRunnable(), directExecutor());

    // When: Execute is called
    executionList.execute();

    // Then: The exception should be caught and the execution list should remain usable
    // (Adding another runnable should work normally)
    executionList.add(createThrowingRunnable(), directExecutor());
    // If we reach this point without an exception, the test passes
  }

  public void testNullParameterHandling() {
    // Verify that all public methods properly handle null parameters
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }

  // Helper methods for better test readability

  private void executeListWithDummyRunnables() throws Exception {
    Executor threadPoolExecutor = newCachedThreadPool();
    CountDownLatch completionLatch = new CountDownLatch(3);
    
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    executionList.add(createCountDownRunnable(completionLatch), threadPoolExecutor);
    
    executionList.execute();
    
    assertTrue("Setup runnables should complete", 
               completionLatch.await(TIMEOUT_SECONDS, SECONDS));
  }

  private Runnable createCountDownRunnable(CountDownLatch latch) {
    return new CountDownRunnable(latch);
  }

  private Runnable createThrowingRunnable() {
    return () -> {
      throw new RuntimeException("Test exception - this should be caught");
    };
  }

  /**
   * A runnable that counts down a latch when executed.
   * More descriptive than the original MockRunnable.
   */
  private static class CountDownRunnable implements Runnable {
    private final CountDownLatch latch;

    CountDownRunnable(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    public void run() {
      latch.countDown();
    }
  }
}
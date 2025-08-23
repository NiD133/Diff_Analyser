package com.google.common.util.concurrent;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import junit.framework.TestCase;

/** Tests for {@link ExecutionList}. */
public class ExecutionListTest extends TestCase {

  /**
   * This test verifies that a runnable added to an {@code ExecutionList} *after* {@code execute()}
   * has been called is executed immediately.
   */
  public void testAdd_afterExecute_executesRunnableImmediately() throws InterruptedException {
    // Arrange: Create an ExecutionList and put it into the "executed" state.
    ExecutionList executionList = new ExecutionList();
    executionList.execute();

    CountDownLatch runnableWasExecuted = new CountDownLatch(1);
    ExecutorService executor = newCachedThreadPool();

    try {
      // Act: Add a new runnable to the already-executed list.
      executionList.add(runnableWasExecuted::countDown, executor);

      // Assert: The runnable should be executed promptly, as the list has already been fired.
      assertTrue(
          "A runnable added after execute() should be executed immediately.",
          runnableWasExecuted.await(5, SECONDS));
    } finally {
      // Clean up the executor to release its resources.
      executor.shutdown();
    }
  }
}
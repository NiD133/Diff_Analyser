package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;

/**
 * Tests for the concurrent behavior of {@link ExecutionList}.
 */
public class ExecutionListTest extends TestCase {

  /**
   * This test verifies that {@code ExecutionList.execute()} is idempotent, even when called
   * concurrently from multiple threads. It ensures that the listeners are executed only once.
   */
  public void testExecute_whenCalledConcurrently_runsListenerOnlyOnce() throws InterruptedException {
    // Arrange
    ExecutionList executionList = new ExecutionList();
    AtomicInteger executionCount = new AtomicInteger(0);

    // Use a latch to ensure both threads call execute() before the listener is allowed to proceed.
    // This creates a race condition to test the idempotency of execute().
    CountDownLatch executionGate = new CountDownLatch(1);

    Runnable listener = () -> {
      try {
        // Wait for the gate to open before counting the execution.
        executionGate.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      executionCount.incrementAndGet();
    };

    executionList.add(listener, directExecutor());

    // Act
    // Create and start two threads that will call execute() concurrently.
    Runnable callExecute = executionList::execute;
    Thread thread1 = new Thread(callExecute, "Thread-1-calls-execute");
    Thread thread2 = new Thread(callExecute, "Thread-2-calls-execute");

    thread1.start();
    thread2.start();

    // Assert that the listener has not run yet because it's waiting on the latch.
    assertEquals("Listener should not have run before the gate is opened", 0, executionCount.get());

    // Open the gate, allowing the listener to proceed.
    executionGate.countDown();

    // Wait for both threads to complete their work.
    thread1.join();
    thread2.join();

    // Assert
    // Verify that the listener was executed exactly once, proving execute() is idempotent.
    assertEquals(
        "Listener should be executed exactly once despite concurrent calls to execute()",
        1,
        executionCount.get());
  }
}
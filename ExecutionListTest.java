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
 */
@NullUnmarked
public class ExecutionListTest extends TestCase {

  private final ExecutionList executionList = new ExecutionList();

  /**
   * Tests that runnables added to the list are executed when the list is executed.
   */
  public void testRunOnPopulatedList() throws Exception {
    Executor executor = newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(3);

    // Add three runnables to the execution list
    executionList.add(new MockRunnable(latch), executor);
    executionList.add(new MockRunnable(latch), executor);
    executionList.add(new MockRunnable(latch), executor);

    // Ensure the latch count is 3 before execution
    assertEquals(3L, latch.getCount());

    // Execute the list and verify all runnables are executed
    executionList.execute();
    assertTrue(latch.await(1L, SECONDS));
  }

  /**
   * Tests that executing the list multiple times does not re-run the runnables.
   */
  public void testExecute_idempotent() {
    AtomicInteger runCount = new AtomicInteger();

    // Add a runnable that increments the run count
    executionList.add(() -> runCount.getAndIncrement(), directExecutor());

    // Execute the list and verify the runnable is executed once
    executionList.execute();
    assertEquals(1, runCount.get());

    // Execute the list again and verify the runnable is not executed again
    executionList.execute();
    assertEquals(1, runCount.get());
  }

  /**
   * Tests that executing the list concurrently does not re-run the runnables.
   */
  public void testExecute_idempotentConcurrently() throws InterruptedException {
    CountDownLatch readyToRun = new CountDownLatch(1);
    AtomicInteger runCount = new AtomicInteger();

    // Add a runnable that waits for a latch before incrementing the run count
    executionList.add(() -> {
      try {
        readyToRun.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      runCount.getAndIncrement();
    }, directExecutor());

    // Create two threads that execute the list
    Runnable executeTask = executionList::execute;
    Thread thread1 = new Thread(executeTask);
    Thread thread2 = new Thread(executeTask);

    // Start both threads
    thread1.start();
    thread2.start();

    // Ensure the runnable has not yet run
    assertEquals(0, runCount.get());

    // Allow the runnable to run and wait for threads to finish
    readyToRun.countDown();
    thread1.join();
    thread2.join();

    // Verify the runnable was executed only once
    assertEquals(1, runCount.get());
  }

  /**
   * Tests that adding a runnable after the list has been executed runs it immediately.
   */
  public void testAddAfterRun() throws Exception {
    // Run the populated list test to execute the list
    testRunOnPopulatedList();

    // Add a new runnable and verify it runs immediately
    CountDownLatch latch = new CountDownLatch(1);
    executionList.add(new MockRunnable(latch), newCachedThreadPool());
    assertTrue(latch.await(1L, SECONDS));
  }

  /**
   * Tests that runnables are executed in the order they were added.
   */
  public void testOrdering() throws Exception {
    AtomicInteger counter = new AtomicInteger();

    // Add runnables that increment the counter in order
    for (int i = 0; i < 10; i++) {
      int expectedValue = i;
      executionList.add(() -> counter.compareAndSet(expectedValue, expectedValue + 1), directExecutor());
    }

    // Execute the list and verify the counter reaches 10
    executionList.execute();
    assertEquals(10, counter.get());
  }

  /**
   * Tests that exceptions thrown by runnables are caught.
   */
  public void testExceptionsCaught() {
    executionList.add(THROWING_RUNNABLE, directExecutor());
    executionList.execute();
    executionList.add(THROWING_RUNNABLE, directExecutor());
  }

  /**
   * Tests that null values are not allowed in public methods.
   */
  public void testNulls() {
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }

  /**
   * A mock runnable that decrements a latch when run.
   */
  private static class MockRunnable implements Runnable {
    final CountDownLatch latch;

    MockRunnable(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    public void run() {
      latch.countDown();
    }
  }

  private static final Runnable THROWING_RUNNABLE = () -> {
    throw new RuntimeException();
  };
}
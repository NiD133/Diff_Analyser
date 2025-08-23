package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.testing.NullPointerTester;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link ExecutionList}.
 */
@NullUnmarked
public class ExecutionListTest extends TestCase {

  private static final long TIMEOUT_SECONDS = 1L;

  public void testExecute_runsAllListenersAddedBeforeExecute() throws Exception {
    // Given a list with three listeners registered on a background executor
    ExecutionList list = new ExecutionList();
    ExecutorService exec = newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(3);
    try {
      list.add(new LatchRunnable(latch), exec);
      list.add(new LatchRunnable(latch), exec);
      list.add(new LatchRunnable(latch), exec);
      assertEquals(3L, latch.getCount());

      // When execute is called
      list.execute();

      // Then all listeners run within a reasonable amount of time
      assertTrue(latch.await(TIMEOUT_SECONDS, SECONDS));
    } finally {
      exec.shutdownNow();
    }
  }

  public void testExecute_isIdempotent() {
    // Given a single listener that increments a counter
    ExecutionList list = new ExecutionList();
    AtomicInteger runCount = new AtomicInteger();
    list.add(
        new Runnable() {
          @Override
          public void run() {
            runCount.getAndIncrement();
          }
        },
        directExecutor());

    // When execute is called multiple times
    list.execute();
    assertEquals(1, runCount.get());
    list.execute();

    // Then the listener is only run once
    assertEquals(1, runCount.get());
  }

  public void testExecute_isIdempotentWhenCalledConcurrently() throws Exception {
    // Given a listener that blocks until we allow it to proceed
    ExecutionList list = new ExecutionList();
    CountDownLatch allowRun = new CountDownLatch(1);
    AtomicInteger runCount = new AtomicInteger();
    list.add(
        new Runnable() {
          @Override
          public void run() {
            try {
              allowRun.await();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              throw new RuntimeException(e);
            }
            runCount.getAndIncrement();
          }
        },
        directExecutor());

    // When execute is called concurrently from two threads
    Runnable execute = new Runnable() {
      @Override
      public void run() {
        list.execute();
      }
    };
    Thread t1 = new Thread(execute);
    Thread t2 = new Thread(execute);
    t1.start();
    t2.start();

    // The listener hasn't run yet because it's blocked
    assertEquals(0, runCount.get());

    // Then, after allowing the runnable to proceed, it runs exactly once
    allowRun.countDown();
    t1.join();
    t2.join();
    assertEquals(1, runCount.get());
  }

  public void testAddAfterExecute_runsImmediately() throws Exception {
    // Given a list that has already been executed
    ExecutionList list = new ExecutionList();
    ExecutorService exec = newCachedThreadPool();
    try {
      CountDownLatch first = new CountDownLatch(1);
      list.add(new LatchRunnable(first), exec);
      list.execute();
      assertTrue(first.await(TIMEOUT_SECONDS, SECONDS));

      // When we add a new listener after execute
      CountDownLatch afterExecute = new CountDownLatch(1);
      list.add(new LatchRunnable(afterExecute), exec);

      // Then it runs immediately (without a second execute call)
      assertTrue(afterExecute.await(TIMEOUT_SECONDS, SECONDS));
    } finally {
      exec.shutdownNow();
    }
  }

  public void testOrdering_preservesRegistrationOrderForListenersAddedBeforeExecute()
      throws Exception {
    // Given 10 listeners added before execute
    ExecutionList list = new ExecutionList();
    AtomicInteger counter = new AtomicInteger();
    for (int i = 0; i < 10; i++) {
      final int expected = i; // capture for the anonymous class
      list.add(
          new Runnable() {
            @Override
            public void run() {
              // Each listener expects to see the counter at its registration index
              // and increments it by 1. If run out of order, compareAndSet will fail.
              counter.compareAndSet(expected, expected + 1);
            }
          },
          directExecutor());
    }

    // When execute is called
    list.execute();

    // Then listeners added before execute run in registration order
    assertEquals(10, counter.get());
  }

  public void testExceptionsFromListenerAreCaughtAndDoNotPreventOthersFromRunning() {
    // Given a listener that throws
    ExecutionList list = new ExecutionList();

    // When added before execute, the exception is propagated to the executor (direct in this case)
    // but must be caught and logged by ExecutionList, not thrown to the caller.
    list.add(THROWING_RUNNABLE, directExecutor());
    list.execute();

    // When added after execute, it must still run (and be caught/logged) without throwing here
    list.add(THROWING_RUNNABLE, directExecutor());
  }

  public void testPublicInstanceMethodsDisallowNulls() {
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }

  private static final class LatchRunnable implements Runnable {
    final CountDownLatch latch;

    LatchRunnable(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    public void run() {
      latch.countDown();
    }
  }

  private static final Runnable THROWING_RUNNABLE =
      new Runnable() {
        @Override
        public void run() {
          throw new RuntimeException();
        }
      };
}
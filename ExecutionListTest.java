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

  private ExecutionList list;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    list = new ExecutionList();
  }

  /**
   * Tests that execute() runs all runnables added to the list.
   */
  public void testExecute_runsAllRunnablesInList() throws Exception {
    // Setup: Add three runnables to the list
    CountDownLatch countDownLatch = new CountDownLatch(3);
    Executor exec = newCachedThreadPool();
    
    list.add(countDownLatch::countDown, exec);
    list.add(countDownLatch::countDown, exec);
    list.add(countDownLatch::countDown, exec);
    
    // Precondition: No runnables have executed yet
    assertEquals(3L, countDownLatch.getCount());
    
    // Execute the list
    list.execute();
    
    // Verify: All runnables complete within timeout
    assertTrue("All runnables should complete execution", 
               countDownLatch.await(1L, SECONDS));
  }

  /**
   * Tests that execute() is idempotent - subsequent calls after the first have no effect.
   */
  public void testExecute_isIdempotent() {
    AtomicInteger runCount = new AtomicInteger();
    
    list.add(runCount::incrementAndGet, directExecutor());
    
    // First execution should run the runnable
    list.execute();
    assertEquals(1, runCount.get());
    
    // Second execution should not run again
    list.execute();
    assertEquals("Runnable should not execute again", 
                 1, runCount.get());
  }

  /**
   * Tests that concurrent execute() calls result in exactly one execution of the runnable.
   */
  public void testExecute_idempotentUnderConcurrentCalls() throws InterruptedException {
    // Setup: Runnable that waits for permission to run
    AtomicInteger runCount = new AtomicInteger();
    CountDownLatch runPermission = new CountDownLatch(1);
    
    list.add(() -> {
      try {
        runPermission.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      runCount.incrementAndGet();
    }, directExecutor());

    // Start two threads that both call execute()
    Thread thread1 = new Thread(list::execute);
    Thread thread2 = new Thread(list::execute);
    thread1.start();
    thread2.start();
    
    // Verify runnable hasn't executed yet
    assertEquals(0, runCount.get());
    
    // Allow runnable to proceed
    runPermission.countDown();
    
    // Wait for threads to finish
    thread1.join();
    thread2.join();
    
    // Verify runnable executed exactly once
    assertEquals(1, runCount.get());
  }

  /**
   * Tests that adding a runnable after execute() causes immediate execution.
   */
  public void testAddAfterExecute_executesImmediately() throws Exception {
    // Setup: Execute an empty list first
    list.execute();
    
    // Add a new runnable after execution
    CountDownLatch latch = new CountDownLatch(1);
    list.add(latch::countDown, newCachedThreadPool());
    
    // Verify: Runnable executes immediately
    assertTrue("Runnable should execute immediately", 
               latch.await(1L, SECONDS));
  }

  /**
   * Tests that runnables execute in the order they were added.
   */
  public void testExecute_runsRunnablesInOrderOfAddition() {
    // Setup: Add runnables that update state in sequence
    AtomicInteger currentValue = new AtomicInteger();
    int numRunnables = 10;
    
    for (int expectedPreviousValue = 0; expectedPreviousValue < numRunnables; expectedPreviousValue++) {
      int expected = expectedPreviousValue;
      list.add(() -> 
        currentValue.compareAndSet(expected, expected + 1),
        directExecutor()
      );
    }
    
    // Execute the list
    list.execute();
    
    // Verify all runnables executed in order
    assertEquals(numRunnables, currentValue.get());
  }

  /**
   * Tests that exceptions from runnables are caught and don't prevent execution of subsequent runnables.
   */
  public void testExecute_handlesRunnableExceptionsGracefully() {
    // Setup: Add both throwing and non-throwing runnables
    AtomicInteger successCount = new AtomicInteger();
    
    // Add exception-throwing runnable
    list.add(THROWING_RUNNABLE, directExecutor());
    
    // Add successful runnable
    list.add(successCount::incrementAndGet, directExecutor());
    
    // Should handle both without propagating exceptions
    list.execute();
    
    // Verify successful runnable executed
    assertEquals(1, successCount.get());
  }

  /**
   * Tests that all public methods properly handle null parameters.
   */
  public void testNullContracts() {
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }

  // Runnable that always throws an exception
  private static final Runnable THROWING_RUNNABLE = 
      () -> { throw new RuntimeException("Test exception"); };
}
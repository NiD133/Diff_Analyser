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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.testing.NullPointerTester;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link ExecutionList}.
 *
 * @author Nishant Thakkar
 * @author Sven Mawson
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class ExecutionListTest {

  private final ExecutionList list = new ExecutionList();

  @Test
  public void execute_withListenersAddedBefore_allListenersAreExecuted() throws Exception {
    // Arrange
    Executor executor = newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(3);

    list.add(latch::countDown, executor);
    list.add(latch::countDown, executor);
    list.add(latch::countDown, executor);
    assertEquals(3, latch.getCount());

    // Act
    list.execute();

    // Assert
    // Verify that all runnables execute concurrently in a reasonable amount of time.
    assertTrue(
        "Listeners should have been executed within the timeout", latch.await(1L, SECONDS));
  }

  @Test
  public void execute_calledMultipleTimes_listenersRunOnlyOnce() {
    // Arrange
    AtomicInteger runCount = new AtomicInteger(0);
    list.add(runCount::getAndIncrement, directExecutor());

    // Act & Assert
    list.execute();
    assertEquals("Listener should be run exactly once after the first execute() call", 1, runCount.get());

    list.execute();
    assertEquals(
        "Listener should not be run again on subsequent calls to execute()", 1, runCount.get());
  }

  @Test
  public void execute_calledConcurrently_listenersRunOnlyOnce() throws InterruptedException {
    // Arrange
    // This latch ensures the listener doesn't complete its work until we allow it,
    // which gives both threads a chance to call execute() before the listener finishes.
    CountDownLatch listenerExecutionGate = new CountDownLatch(1);
    AtomicInteger runCount = new AtomicInteger(0);

    list.add(
        () -> {
          try {
            listenerExecutionGate.await();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          runCount.getAndIncrement();
        },
        directExecutor());

    Runnable executeList = list::execute;
    Thread thread1 = new Thread(executeList);
    Thread thread2 = new Thread(executeList);

    // Act
    thread1.start();
    thread2.start();
    assertEquals("Listener should not have run yet", 0, runCount.get());

    // Open the gate, allowing the listener to proceed.
    listenerExecutionGate.countDown();

    thread1.join(SECONDS.toMillis(1));
    thread2.join(SECONDS.toMillis(1));

    // Assert
    assertEquals(
        "Listener should have been executed exactly once despite concurrent execute() calls",
        1,
        runCount.get());
  }

  @Test
  public void add_afterExecute_listenerIsExecutedImmediately() throws Exception {
    // Arrange
    // Mark the list as executed by calling execute() on an empty list.
    list.execute();

    CountDownLatch latch = new CountDownLatch(1);
    Executor executor = newCachedThreadPool();

    // Act
    list.add(latch::countDown, executor);

    // Assert
    // The listener added after execute() should be run immediately.
    assertTrue(
        "Listener added after execute() should run immediately", latch.await(1L, SECONDS));
  }

  @Test
  public void execute_withMultipleListeners_listenersRunInOrderOfAddition() {
    // Arrange
    // The listener will check that it's called in the expected order and update the counter.
    // compareAndSet provides a strong guarantee of ordering.
    AtomicInteger executionOrderCounter = new AtomicInteger(0);
    for (int i = 0; i < 10; i++) {
      final int expectedOrder = i;
      list.add(
          () -> executionOrderCounter.compareAndSet(expectedOrder, expectedOrder + 1),
          directExecutor());
    }

    // Act
    list.execute();

    // Assert
    assertEquals(
        "All listeners should have executed in the order they were added",
        10,
        executionOrderCounter.get());
  }

  @Test
  public void execute_listenerThrowsException_doesNotPropagate() {
    // Arrange
    list.add(THROWING_RUNNABLE, directExecutor());

    // Act & Assert
    // The test passes if execute() does not throw an exception.
    // The exception is caught and logged internally by ExecutionList.
    list.execute();
  }

  @Test
  public void add_afterExecuteWithThrowingListener_doesNotPropagate() {
    // Arrange
    list.execute(); // Mark the list as executed.

    // Act & Assert
    // The test passes if add() does not throw an exception.
    // The listener is executed immediately, and its exception is caught and logged.
    list.add(THROWING_RUNNABLE, directExecutor());
  }

  @Test
  public void testNulls() {
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }

  private static final Runnable THROWING_RUNNABLE =
      () -> {
        throw new RuntimeException("This is an expected exception for the test.");
      };
}
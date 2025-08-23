package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExecutionList} focusing on the idempotency of the execute method.
 */
class ExecutionListTest {

  private final ExecutionList executionList = new ExecutionList();

  @Test
  void execute_whenCalledMultipleTimes_runsListenersOnlyOnce() {
    // Arrange: Create a listener that counts how many times it has been run.
    AtomicInteger executionCount = new AtomicInteger(0);
    Runnable listener = executionCount::incrementAndGet;
    executionList.add(listener, directExecutor());

    // Act: Execute the list for the first time.
    executionList.execute();

    // Assert: The listener should have been run exactly once.
    assertEquals(1, executionCount.get(),
        "Listener should be executed on the first call to execute()");

    // Act: Execute the list again.
    executionList.execute();

    // Assert: The listener should not have been run again. The count remains 1.
    assertEquals(1, executionCount.get(),
        "Listener should not be executed on subsequent calls to execute()");
  }
}
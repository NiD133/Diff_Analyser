package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link ExecutionList}. */
@RunWith(JUnit4.class)
public class ExecutionListTest {

  private final ExecutionList executionList = new ExecutionList();

  @Test
  public void execute_withDirectExecutor_executesRunnablesInAdditionOrder() {
    // Arrange
    final int numberOfRunnables = 10;
    final AtomicInteger executionCounter = new AtomicInteger(0);

    for (int i = 0; i < numberOfRunnables; i++) {
      final int expectedExecutionIndex = i;
      executionList.add(
          () -> {
            // This CAS operation verifies that runnables execute in the correct sequence.
            // It will only succeed if the counter's current value matches the expected index,
            // proving that no other runnable has run out of turn.
            executionCounter.compareAndSet(expectedExecutionIndex, expectedExecutionIndex + 1);
          },
          directExecutor());
    }

    // Act
    executionList.execute();

    // Assert
    // If all runnables executed in the correct order, each compareAndSet operation
    // would have succeeded, and the final count will match the number of runnables added.
    assertEquals(
        "All runnables should have executed in the correct order.",
        numberOfRunnables,
        executionCounter.get());
  }
}
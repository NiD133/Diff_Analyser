package com.google.common.util.concurrent;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ExecutionList} focusing on its exception handling behavior.
 *
 * <p>This test verifies that the ExecutionList correctly catches and suppresses exceptions thrown by
 * its listeners, as specified in its documentation.
 */
class ExecutionListExceptionHandlingTest {

    // A runnable that always fails, to test the exception-catching logic.
    private static final Runnable THROWING_RUNNABLE = () -> {
        throw new RuntimeException("Simulated listener failure");
    };

    private ExecutionList executionList;

    @BeforeEach
    void setUp() {
        executionList = new ExecutionList();
    }

    @Test
    @DisplayName("ExecutionList should not propagate exceptions from listeners")
    void executionListShouldNotPropagateExceptionsFromListeners() {
        // Arrange: Add a listener that is designed to throw an exception.
        executionList.add(THROWING_RUNNABLE, directExecutor());

        // Act & Assert (Scenario 1): During execute()
        // The ExecutionList's contract states it will catch and log exceptions from listeners,
        // not propagate them to the caller of execute().
        assertDoesNotThrow(
                () -> executionList.execute(),
                "execute() should not propagate exceptions from its listeners.");

        // Act & Assert (Scenario 2): When adding a listener after execute()
        // Listeners added after execution are run immediately. The same exception-catching
        // behavior should apply.
        assertDoesNotThrow(
                () -> executionList.add(THROWING_RUNNABLE, directExecutor()),
                "add() after execution should not propagate exceptions from its listeners.");
    }
}
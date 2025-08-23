package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * This test suite focuses on the behavior of the ScriptRunner class.
 * This particular test case was improved for understandability.
 */
public class ScriptRunner_ESTestTest15 { // Retaining original class name for context

    /**
     * Verifies that ScriptRunner correctly handles an empty script when the database
     * connection has auto-commit disabled.
     *
     * It should:
     * 1. Read the initial auto-commit state.
     * 2. Execute the script without changing the state (since it's already disabled).
     * 3. Attempt to commit the transaction.
     * 4. Restore the original auto-commit state in its 'finally' block.
     */
    @Test
    public void shouldRunEmptyScriptAndCorrectlyHandleAutoCommitWhenInitiallyDisabled() throws SQLException {
        // Arrange: Set up the test conditions and dependencies.

        // 1. Create a mock database connection.
        Connection mockConnection = mock(Connection.class);

        // 2. Configure the mock to report that auto-commit is initially disabled.
        // The ScriptRunner checks this state multiple times during its execution.
        when(mockConnection.getAutoCommit()).thenReturn(false);

        // 3. Instantiate the ScriptRunner with our mock connection.
        // By default, the runner's 'autoCommit' property is also false.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // 4. Provide an empty script to the runner.
        Reader emptyScriptReader = new StringReader("\n");

        // Act: Execute the method under test.
        scriptRunner.runScript(emptyScriptReader);

        // Assert: Verify the expected outcomes and interactions.

        // 1. Verify that the runner checked the auto-commit status exactly three times:
        //    - Once to save the original state.
        //    - Once to check if it needed to change the state.
        //    - Once before attempting to commit the transaction.
        verify(mockConnection, times(3)).getAutoCommit();

        // 2. Verify that a commit was attempted, as auto-commit was off.
        verify(mockConnection, times(1)).commit();

        // 3. Verify that the runner restored the original auto-commit state (false)
        //    in its 'finally' block. This is the ONLY time setAutoCommit should be called
        //    because the initial state matched the runner's desired state.
        verify(mockConnection, times(1)).setAutoCommit(false);

        // 4. Ensure no other unexpected interactions occurred with the connection.
        verifyNoMoreInteractions(mockConnection);
    }
}
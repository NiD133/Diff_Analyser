package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test suite contains refactored and more understandable tests for the ScriptRunner class.
 */
public class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner throws a RuntimeException if it enters an infinite loop
     * while processing statement results. This can happen with certain JDBC driver behaviors.
     */
    @Test
    public void runScript_whenResultProcessingEntersInfiniteLoop_throwsRuntimeException() throws Exception {
        // Arrange: Set up a scenario that causes an infinite loop.
        // The result processing loop in ScriptRunner terminates when `getUpdateCount()` is -1.
        // By mocking it to always return 0, we simulate a non-terminating loop.
        // The test environment is expected to break the loop and cause an exception.
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(false); // Indicates an update count, not a ResultSet
        when(mockStatement.getMoreResults()).thenReturn(false);
        when(mockStatement.getUpdateCount()).thenReturn(0); // This causes the infinite loop

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.getAutoCommit()).thenReturn(false);

        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setRemoveCRs(true);

        String sqlCommand = "UPDATE my_table SET a_column = 'a_value';";
        Reader scriptReader = new StringReader(sqlCommand);

        // Act & Assert: Verify that executing the script results in a RuntimeException.
        // The original test relied on a tool-specific exception being thrown. We generalize
        // this to assert that the operation fails with a descriptive RuntimeException.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            scriptRunner.runScript(scriptReader);
        });

        // The ScriptRunner should wrap the underlying error with a message indicating which command failed.
        String commandWithoutDelimiter = "UPDATE my_table SET a_column = 'a_value'";
        String expectedMessagePrefix = "Error executing: " + commandWithoutDelimiter;
        assertTrue(
            "Exception message should start with 'Error executing: <SQL command>'",
            exception.getMessage().startsWith(expectedMessagePrefix)
        );
    }
}
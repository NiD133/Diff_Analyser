package org.apache.ibatis.jdbc;

import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class focuses on the script-parsing capabilities of the ScriptRunner.
 * The original class name is kept to show a direct improvement. In a real-world scenario,
 * this test would be part of a larger, more descriptively named test suite like ScriptRunnerTest.
 */
public class ScriptRunner_ESTestTest16 extends ScriptRunner_ESTest_scaffolding {

    /**
     * Verifies that the ScriptRunner correctly identifies and skips a single-line SQL comment.
     * A script containing only a comment should not result in any database interaction.
     */
    @Test
    public void shouldIgnoreSingleLineSqlComment() throws SQLException {
        // Arrange
        // 1. Create a mock database connection.
        Connection mockConnection = mock(Connection.class);

        // 2. The ScriptRunner checks the connection's auto-commit state. We must stub this
        //    method to prevent a NullPointerException and control the test environment.
        when(mockConnection.getAutoCommit()).thenReturn(true);

        // 3. Instantiate the ScriptRunner with the mock connection.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        // Suppress console output to keep test logs clean.
        scriptRunner.setLogWriter(null);
        scriptRunner.setErrorLogWriter(null);

        // 4. Define a script that contains only a single-line SQL comment.
        Reader scriptWithOnlyComment = new StringReader("-- This is a comment and should be ignored.");

        // Act
        // Execute the script.
        scriptRunner.runScript(scriptWithOnlyComment);

        // Assert
        // The primary assertion is that no SQL statement was ever created because the
        // script runner should recognize the line as a comment and skip execution.
        verify(mockConnection, never()).createStatement();
    }
}
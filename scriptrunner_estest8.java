package org.apache.ibatis.jdbc;

import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the ScriptRunner class.
 */
class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner correctly handles and wraps exceptions that occur during
     * the processing of a result set.
     *
     * This scenario is simulated by mocking a ResultSet with an excessively large number of columns.
     * Processing such a result set would be a long-running operation, which in some test environments
     * (like the one that generated the original test) can trigger a resource limit exception.
     *
     * The test verifies that ScriptRunner catches such an underlying exception and wraps it
     * in a descriptive RuntimeException, ensuring robust error handling.
     */
    @Test
    void runScript_shouldWrapExecutionErrorInRuntimeException() throws Exception {
        // --- Arrange ---
        // 1. Simulate a ResultSet with a very large number of columns to trigger a
        //    long-running internal loop within ScriptRunner's result printing logic.
        final int EXCESSIVE_COLUMN_COUNT = 1649;

        ResultSetMetaData mockMetaData = mock(ResultSetMetaData.class);
        when(mockMetaData.getColumnCount()).thenReturn(EXCESSIVE_COLUMN_COUNT);
        when(mockMetaData.getColumnLabel(anyInt())).thenReturn("ANY_COLUMN_LABEL");

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);
        when(mockResultSet.getString(anyInt())).thenReturn("ANY_VALUE");
        // Simulate a result set with exactly one row.
        when(mockResultSet.next()).thenReturn(true, false);

        // 2. Simulate a Statement that returns the problematic ResultSet.
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(true); // Indicates a ResultSet is available.
        when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        when(mockStatement.getMoreResults()).thenReturn(false); // No more results after this one.
        when(mockStatement.getUpdateCount()).thenReturn(-1); // Standard for "no update count".

        // 3. Simulate a Connection that provides the mocked Statement.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.getAutoCommit()).thenReturn(true);

        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        Reader scriptReader = new StringReader(";"); // A simple script with one command.

        // --- Act & Assert ---
        // Execute the script and assert that ScriptRunner catches the underlying problem
        // and wraps it in its own RuntimeException.
        // Note: We use JUnit 5's assertThrows for a more readable assertion.
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            scriptRunner.runScript(scriptReader);
        });

        // Verify the exception message to confirm it originates from ScriptRunner's error handling.
        assertTrue(
            thrownException.getMessage().startsWith("Error executing: ;"),
            "Exception message should indicate an error during script execution."
        );
        assertNotNull(
            thrownException.getCause(),
            "The RuntimeException should wrap the original cause of the failure."
        );
    }
}
package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test suite focuses on the behavior of the ScriptRunner class.
 */
public class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner throws a RuntimeException when configured with a null log writer
     * and executes a script that produces a result set.
     *
     * The underlying NullPointerException is expected when the runner attempts to print the
     * result set's column headers to the null writer. This test ensures that such an
     * error is properly caught and wrapped in a descriptive RuntimeException.
     */
    @Test
    public void runScriptWithNullLogWriterShouldThrowExceptionWhenPrintingResults() {
        // Arrange
        // 1. Mock the necessary JDBC objects to simulate a query that returns a result set.
        // We only need to mock the metadata call to trigger the header-printing logic.
        ResultSetMetaData mockResultSetMetaData = mock(ResultSetMetaData.class);
        when(mockResultSetMetaData.getColumnCount()).thenReturn(1);
        when(mockResultSetMetaData.getColumnLabel(anyInt())).thenReturn("id");

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);

        Statement mockStatement = mock(Statement.class);
        // Simulate the execution of a statement that returns a result set.
        try {
            when(mockStatement.execute(anyString())).thenReturn(true);
            when(mockStatement.getResultSet()).thenReturn(mockResultSet);
        } catch (SQLException e) {
            // This is unlikely to happen with mocks, but handle it for completeness.
            fail("Mock setup failed: " + e.getMessage());
        }

        Connection mockConnection = mock(Connection.class);
        try {
            when(mockConnection.createStatement()).thenReturn(mockStatement);
        } catch (SQLException e) {
            fail("Mock setup failed: " + e.getMessage());
        }

        // 2. Instantiate and configure the ScriptRunner.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setSendFullScript(true);

        // Set the log writer to null. This is the key condition that should cause a
        // NullPointerException when the runner tries to print the results.
        scriptRunner.setLogWriter(null);

        String sqlScript = "SELECT * FROM users";
        Reader scriptReader = new StringReader(sqlScript);

        // Act & Assert
        try {
            scriptRunner.runScript(scriptReader);
            fail("Expected a RuntimeException to be thrown due to the null log writer.");
        } catch (RuntimeException e) {
            // Verify that the exception is of the expected type and wraps the correct cause.
            // The ScriptRunner is expected to wrap the underlying error in a RuntimeException.
            Throwable cause = e.getCause();
            assertNotNull("Exception cause should not be null", cause);

            // The direct cause should be an SQLException, which wraps the root NullPointerException.
            // This reflects the error handling within the ScriptRunner's executeStatement method.
            assertTrue("Cause should be an instance of SQLException", cause instanceof SQLException);
            assertTrue("Root cause should be a NullPointerException", cause.getCause() instanceof NullPointerException);

            // Check if the exception message clearly indicates the failed script and the cause.
            String expectedMessage = "Error executing: " + sqlScript + ".  Cause: java.lang.NullPointerException";
            assertEquals(expectedMessage, cause.getMessage());
        }
    }
}
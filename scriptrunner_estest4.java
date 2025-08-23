package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test suite focuses on the behavior of the ScriptRunner class.
 */
public class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner wraps and throws a RuntimeException when processing a ResultSet
     * where a column label is null, which would otherwise cause a NullPointerException.
     * This ensures robust error handling during result printing.
     */
    @Test
    public void runScriptWithFullScriptThrowsRuntimeExceptionWhenColumnLabelIsNull() throws Exception {
        // Arrange
        // 1. Mock JDBC objects to simulate the scenario where a column label is null.
        ResultSetMetaData mockMetaData = mock(ResultSetMetaData.class);
        when(mockMetaData.getColumnCount()).thenReturn(1);
        // This is the key part of the setup: simulating a null column label.
        when(mockMetaData.getColumnLabel(1)).thenReturn(null);

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);
        // We don't need to mock rs.next() as the exception occurs while printing headers.

        Statement mockStatement = mock(Statement.class);
        // Simulate executing a script that returns a result set.
        when(mockStatement.execute(anyString())).thenReturn(true);
        when(mockStatement.getResultSet()).thenReturn(mockResultSet);

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // 2. Configure the ScriptRunner to execute the entire script at once.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setStopOnError(true);

        // 3. Prepare a sample script.
        String sqlScript = "SELECT * FROM DUMMY_TABLE";
        Reader reader = new StringReader(sqlScript);
        String expectedCommand = sqlScript + System.lineSeparator();

        // Act & Assert
        try {
            scriptRunner.runScript(reader);
            fail("Should have thrown a RuntimeException due to the null column label.");
        } catch (RuntimeException e) {
            // Verify that the correct exception was thrown with a descriptive message.
            String expectedMessage = "Error executing: " + expectedCommand + ".  Cause: java.lang.NullPointerException";
            assertEquals(expectedMessage, e.getMessage());
            assertTrue("The cause of the exception should be a NullPointerException.", e.getCause() instanceof NullPointerException);
        }
    }
}
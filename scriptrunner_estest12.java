package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the ScriptRunner class.
 */
public class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner throws a RuntimeException when the underlying
     * JDBC Connection fails to create a Statement. This simulates a driver
     * or connection issue.
     */
    @Test
    public void runScriptShouldThrowRuntimeExceptionWhenConnectionFailsToCreateStatement() throws SQLException {
        // Arrange: Set up the test conditions
        // 1. Mock a Connection that will fail to create a statement.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(null);
        // ScriptRunner checks auto-commit status, so we need to mock this call.
        when(mockConnection.getAutoCommit()).thenReturn(true);

        // 2. Create a ScriptRunner with the mocked connection.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setFullLineDelimiter(true); // A semicolon on its own line is a delimiter.

        // 3. Define a simple script that contains only a delimiter.
        // This will trigger an attempt to execute an empty command.
        Reader scriptReader = new StringReader(";");

        // Act & Assert: Execute the script and verify the expected exception
        try {
            scriptRunner.runScript(scriptReader);
            fail("Expected a RuntimeException because statement creation returned null.");
        } catch (RuntimeException e) {
            // Verify the exception message to confirm the failure context.
            // The empty command "" results in "Error executing: .".
            String expectedMessage = "Error executing: .  Cause: java.lang.NullPointerException";
            assertEquals(expectedMessage, e.getMessage());

            // Verify the cause of the exception is a NullPointerException, which occurs
            // when ScriptRunner tries to use the null Statement object.
            assertNotNull("Exception cause should not be null.", e.getCause());
            assertTrue("Exception cause should be a NullPointerException.", e.getCause() instanceof NullPointerException);
        }
    }
}
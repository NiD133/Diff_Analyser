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
     * Verifies that ScriptRunner throws a descriptive RuntimeException when a SQL statement
     * in the script is not properly terminated by the default delimiter (a semicolon).
     */
    @Test
    public void shouldThrowExceptionWhenStatementIsMissingTerminator() throws Exception {
        // Arrange: Set up the test environment.
        
        // 1. Mock the database connection. We are unit-testing ScriptRunner's logic,
        //    not its interaction with a live database.
        Connection mockConnection = mock(Connection.class);

        // 2. The ScriptRunner checks the connection's auto-commit mode. Stub this call
        //    to return true, simulating a typical connection state.
        when(mockConnection.getAutoCommit()).thenReturn(true);

        // 3. Instantiate the class under test.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // 4. Define a simple SQL script that is intentionally missing the trailing semicolon.
        String scriptWithoutTerminator = "SELECT * FROM users";
        Reader scriptReader = new StringReader(scriptWithoutTerminator);

        // Act & Assert: Execute the script and verify the expected exception.
        try {
            scriptRunner.runScript(scriptReader);
            fail("Expected a RuntimeException because the SQL statement is missing a terminator.");
        } catch (RuntimeException e) {
            // Verify that the exception provides a clear error message about the failure.
            String expectedError = "Error executing: " + scriptWithoutTerminator;
            assertEquals(expectedError, e.getMessage());

            // Verify that the cause of the exception is an SQLException detailing the specific syntax error.
            Throwable cause = e.getCause();
            assertNotNull("Exception should have a cause.", cause);
            assertTrue("The cause should be an instance of SQLException.", cause instanceof SQLException);

            String expectedCauseMessage = "Line missing end-of-line terminator (;) => " + scriptWithoutTerminator;
            assertEquals(expectedCauseMessage, cause.getMessage());
        }
    }
}
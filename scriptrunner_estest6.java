package org.apache.ibatis.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Test suite for the ScriptRunner class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScriptRunnerTest {

    @Mock
    private Connection mockConnection;

    private ScriptRunner scriptRunner;

    @Before
    public void setUp() throws SQLException {
        // Configure the mock connection to behave as if auto-commit is enabled.
        // This is checked by the ScriptRunner before and after script execution.
        when(mockConnection.getAutoCommit()).thenReturn(true);
        scriptRunner = new ScriptRunner(mockConnection);

        // Suppress console output for a clean test run
        scriptRunner.setErrorLogWriter(null);
        scriptRunner.setLogWriter(null);
    }

    /**
     * Tests that ScriptRunner throws a RuntimeException when a script
     * contains a command that is not terminated by the required delimiter.
     */
    @Test
    public void shouldThrowExceptionForScriptWithMissingDelimiter() {
        // Arrange
        // A script containing a single command ('/') that is not terminated by the default delimiter (';').
        Reader scriptReader = new StringReader("/");

        // Act & Assert
        try {
            scriptRunner.runScript(scriptReader);
            fail("Expected a RuntimeException because the script is missing a delimiter.");
        } catch (RuntimeException e) {
            // The ScriptRunner should identify the unterminated line and report it in the exception message.
            String expectedMessage = "Line missing end-of-line terminator (;) => /";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
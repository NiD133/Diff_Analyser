package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link ScriptRunner} class.
 */
public class ScriptRunnerTest {

    /**
     * Verifies that ScriptRunner throws a RuntimeException when 'fullLineDelimiter' is enabled
     * and a script is provided that does not end with the required delimiter.
     * This ensures that incomplete scripts are correctly identified and rejected.
     */
    @Test
    public void shouldThrowExceptionForMissingTerminatorWhenFullLineDelimiterIsEnabled() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        // ScriptRunner checks the auto-commit status before running the script.
        when(mockConnection.getAutoCommit()).thenReturn(true);

        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setFullLineDelimiter(true);

        // A simple SQL script that is missing the end-of-line terminator (the default ';').
        Reader scriptReader = new StringReader("SELECT * FROM a_table");

        // Act & Assert
        try {
            scriptRunner.runScript(scriptReader);
            fail("Expected a RuntimeException to be thrown due to the missing line terminator.");
        } catch (RuntimeException e) {
            // Verify that the exception message clearly indicates the problem.
            String errorMessage = e.getMessage();
            assertThat(errorMessage, containsString("Line missing end-of-line terminator (;)"));
            assertThat(errorMessage, containsString("SELECT * FROM a_table"));
        }
    }
}
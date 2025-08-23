package org.apache.ibatis.jdbc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ScriptRunner's delimiter handling.
 */
// The class name has been simplified from 'ScriptRunnerTestTest12' to be more conventional.
class ScriptRunnerTest {

    // The original test class extended BaseDataTest and contained helper methods
    // and constants for database integration tests (e.g., runJPetStoreScripts).
    // These have been removed as they are not relevant to this specific unit test,
    // reducing clutter and improving focus. The unused method 'y(StringBuilder)' was also removed.

    @Test
    @DisplayName("should correctly execute statements when a multi-character delimiter is used")
    void shouldHandleMultiCharacterDelimiter() throws Exception {
        // Arrange
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        // This mock setup is required for the ScriptRunner to proceed without trying to print results.
        when(statement.getUpdateCount()).thenReturn(-1);

        ScriptRunner runner = new ScriptRunner(connection);

        // Using a text block for the SQL script makes it much more readable than a
        // manually concatenated string. Trailing whitespace from the original script is removed
        // as ScriptRunner trims each line before processing.
        String scriptWithCustomDelimiter = """
            -- @DELIMITER ||
            line 1;
            line 2;

            ||
            //  @DELIMITER  ;
            line 3;
            """;

        // Act
        runner.runScript(new StringReader(scriptWithCustomDelimiter));

        // Assert
        // The ScriptRunner should group commands based on the specified delimiter.

        // The first statement consists of all lines up to the custom '||' delimiter.
        // The blank line between "line 2;" and "||" is correctly interpreted as a newline.
        String expectedStatement1 = String.format("line 1;%nline 2;%n%n");

        // The second statement is the single line after the delimiter is reset to ';'.
        String expectedStatement2 = String.format("line 3%n");

        // Verify that the statements were executed as two separate commands.
        verify(statement).execute(expectedStatement1);
        verify(statement).execute(expectedStatement2);
    }
}
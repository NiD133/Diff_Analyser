package org.apache.ibatis.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test suite for the ScriptRunner class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScriptRunnerTest {

    @Mock
    private Connection mockConnection;

    /**
     * Tests that the ScriptRunner correctly identifies and ignores a script
     * containing only a single-line comment.
     * <p>
     * The test verifies that no SQL statements are created or executed when such a
     * script is processed, confirming that comments are handled as expected.
     */
    @Test
    public void shouldCorrectlyProcessScriptWithOnlySingleLineComment() throws SQLException {
        // Arrange
        // A script that consists of a single line, which is a standard SQL comment.
        String scriptWithOnlyComment = "// This is a comment and should be ignored.";
        StringReader scriptReader = new StringReader(scriptWithOnlyComment);

        // The ScriptRunner checks the auto-commit status of the connection.
        // We mock this behavior to return false for this test scenario.
        when(mockConnection.getAutoCommit()).thenReturn(false);

        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // Act
        // Execute the script containing only the comment.
        scriptRunner.runScript(scriptReader);

        // Assert
        // Verify that no SQL statement was created because the script only contained a comment.
        // This confirms that the comment was correctly identified and skipped.
        verify(mockConnection, never()).createStatement();
    }
}
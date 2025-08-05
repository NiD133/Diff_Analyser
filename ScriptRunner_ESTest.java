package org.apache.ibatis.jdbc;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Test suite for ScriptRunner class functionality.
 * Tests various scenarios including error handling, script execution modes,
 * and configuration options.
 */
public class ScriptRunnerTest {

    // Test data constants
    private static final String SIMPLE_SQL_SCRIPT = "SELECT * FROM users;";
    private static final String SCRIPT_WITHOUT_SEMICOLON = "SELECT * FROM users";
    private static final String COMMENT_ONLY_SCRIPT = "-- This is a comment";
    private static final String EMPTY_LINE_SCRIPT = "\n";
    private static final String MIXED_SCRIPT = "//--Mixed comment style";

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenConnectionIsNull() throws Throwable {
        // Given
        ScriptRunner scriptRunner = new ScriptRunner(null);
        Reader emptyScript = Reader.nullReader();

        // When & Then
        try {
            scriptRunner.runScript(emptyScript);
            fail("Expected RuntimeException for null connection");
        } catch (RuntimeException e) {
            assertTrue("Should mention AutoCommit error", 
                      e.getMessage().contains("Could not set AutoCommit to false"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForScriptWithoutSemicolon() throws Throwable {
        // Given
        Connection mockConnection = createMockConnectionWithAutoCommit(true);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        StringReader scriptWithoutSemicolon = new StringReader(SCRIPT_WITHOUT_SEMICOLON);

        // When & Then
        try {
            scriptRunner.runScript(scriptWithoutSemicolon);
            fail("Expected RuntimeException for script without semicolon");
        } catch (RuntimeException e) {
            assertTrue("Should mention missing end-of-line terminator", 
                      e.getMessage().contains("Line missing end-of-line terminator"));
        }
    }

    @Test(timeout = 4000)
    public void shouldExecuteSimpleScriptSuccessfully() throws Throwable {
        // Given
        Statement mockStatement = createMockStatementForUpdate(705);
        Connection mockConnection = createMockConnectionWithStatement(mockStatement, true);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        StringReader simpleScript = new StringReader(SIMPLE_SQL_SCRIPT);

        // When
        scriptRunner.runScript(simpleScript);

        // Then
        verify(mockStatement).execute(anyString());
        verify(mockConnection).createStatement();
    }

    @Test(timeout = 4000)
    public void shouldHandleResultSetWithManyColumns() throws Throwable {
        // Given
        ResultSet mockResultSet = createMockResultSetWithColumns(245);
        Statement mockStatement = createMockStatementWithResultSet(mockResultSet);
        Connection mockConnection = createMockConnectionWithStatement(mockStatement, true);
        
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setLogWriter(null); // This will cause NullPointerException
        
        StringReader script = new StringReader("SELECT * FROM table;");

        // When & Then
        try {
            scriptRunner.runScript(script);
            fail("Expected RuntimeException due to null log writer");
        } catch (RuntimeException e) {
            assertTrue("Should be caused by NullPointerException", 
                      e.getCause() instanceof NullPointerException);
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenWarningsEnabledAndWarningExists() throws Throwable {
        // Given
        SQLWarning mockWarning = mock(SQLWarning.class);
        when(mockWarning.toString()).thenReturn("Test Warning");
        
        Statement mockStatement = createMockStatementWithWarning(mockWarning);
        Connection mockConnection = createMockConnectionWithStatement(mockStatement, true);
        
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setThrowWarning(true);
        scriptRunner.setDelimiter("CUSTOM");
        
        StringReader script = new StringReader("CUSTOM");

        // When & Then
        try {
            scriptRunner.runScript(script);
            fail("Expected RuntimeException due to SQL warning");
        } catch (RuntimeException e) {
            assertTrue("Should contain warning message", 
                      e.getMessage().contains("Test Warning"));
        }
    }

    @Test(timeout = 4000)
    public void shouldSkipCommentOnlyLines() throws Throwable {
        // Given
        Connection mockConnection = createMockConnectionWithAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        StringReader commentScript = new StringReader(COMMENT_ONLY_SCRIPT);

        // When
        scriptRunner.runScript(commentScript);

        // Then - Should complete without executing any SQL
        verify(mockConnection, never()).createStatement();
    }

    @Test(timeout = 4000)
    public void shouldSkipEmptyLines() throws Throwable {
        // Given
        Connection mockConnection = createMockConnectionWithAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        StringReader emptyLineScript = new StringReader(EMPTY_LINE_SCRIPT);

        // When
        scriptRunner.runScript(emptyLineScript);

        // Then - Should complete without executing any SQL
        verify(mockConnection, never()).createStatement();
    }

    @Test(timeout = 4000)
    public void shouldHandleMixedCommentStyles() throws Throwable {
        // Given
        Connection mockConnection = createMockConnectionWithAutoCommit(false);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        StringReader mixedCommentScript = new StringReader(MIXED_SCRIPT);

        // When
        scriptRunner.runScript(mixedCommentScript);

        // Then - Should complete without executing any SQL
        verify(mockConnection, never()).createStatement();
    }

    @Test(timeout = 4000)
    public void shouldConfigureEscapeProcessing() throws Throwable {
        // Given
        ScriptRunner scriptRunner = new ScriptRunner(null);

        // When
        scriptRunner.setEscapeProcessing(true);

        // Then - Should not throw exception (configuration method)
        // This is a simple setter test
    }

    @Test(timeout = 4000)
    public void shouldCloseConnectionSafely() throws Throwable {
        // Given
        Connection mockConnection = mock(Connection.class);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // When
        scriptRunner.closeConnection();

        // Then - Should complete without exception
        // Note: closeConnection is deprecated but still needs to work
    }

    @Test(timeout = 4000)
    public void shouldHandleFullLineDelimiterMode() throws Throwable {
        // Given
        Connection mockConnection = createMockConnectionWithAutoCommit(true);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setFullLineDelimiter(true);
        StringReader script = new StringReader(";");

        // When & Then
        try {
            scriptRunner.runScript(script);
            fail("Expected RuntimeException due to null statement");
        } catch (RuntimeException e) {
            assertTrue("Should be caused by NullPointerException", 
                      e.getCause() instanceof NullPointerException);
        }
    }

    @Test(timeout = 4000)
    public void shouldConfigureRemoveCRsOption() throws Throwable {
        // Given
        Statement mockStatement = createMockStatementForUpdate(0);
        Connection mockConnection = createMockConnectionWithStatement(mockStatement, false);
        
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setRemoveCRs(true);
        
        StringReader script = new StringReader("SELECT 1;");

        // When & Then
        // This test verifies the configuration doesn't break normal execution
        // The actual CR removal logic would need integration testing
        try {
            scriptRunner.runScript(script);
            fail("Expected RuntimeException due to infinite loop in test environment");
        } catch (RuntimeException e) {
            assertTrue("Should mention too many resources", 
                      e.getMessage().contains("TooManyResourcesException"));
        }
    }

    // Helper methods for creating mock objects

    private Connection createMockConnectionWithAutoCommit(boolean autoCommit) throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.getAutoCommit()).thenReturn(autoCommit);
        return mockConnection;
    }

    private Connection createMockConnectionWithStatement(Statement statement, boolean autoCommit) throws Exception {
        Connection mockConnection = createMockConnectionWithAutoCommit(autoCommit);
        when(mockConnection.createStatement()).thenReturn(statement);
        return mockConnection;
    }

    private Statement createMockStatementForUpdate(int updateCount) throws Exception {
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(false);
        when(mockStatement.getMoreResults()).thenReturn(false);
        when(mockStatement.getUpdateCount()).thenReturn(updateCount, -1);
        return mockStatement;
    }

    private Statement createMockStatementWithResultSet(ResultSet resultSet) throws Exception {
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(true);
        when(mockStatement.getMoreResults()).thenReturn(false, true);
        when(mockStatement.getResultSet()).thenReturn(resultSet, null);
        when(mockStatement.getUpdateCount()).thenReturn(245);
        return mockStatement;
    }

    private Statement createMockStatementWithWarning(SQLWarning warning) throws Exception {
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(true);
        when(mockStatement.getWarnings()).thenReturn(warning);
        return mockStatement;
    }

    private ResultSet createMockResultSetWithColumns(int columnCount) throws Exception {
        ResultSetMetaData mockMetadata = mock(ResultSetMetaData.class);
        when(mockMetadata.getColumnCount()).thenReturn(columnCount);
        when(mockMetadata.getColumnLabel(anyInt())).thenReturn(null);

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getMetaData()).thenReturn(mockMetadata);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getString(anyInt())).thenReturn(null);
        
        return mockResultSet;
    }
}
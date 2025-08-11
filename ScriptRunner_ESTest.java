package org.apache.ibatis.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Understandable tests for the {@link ScriptRunner} class.
 */
@ExtendWith(MockitoExtension.class)
class ScriptRunnerTest {

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    private ScriptRunner scriptRunner;

    @BeforeEach
    void setUp() throws SQLException {
        // This common setup ensures that the ScriptRunner gets a mocked Statement
        // from its Connection, which is necessary for most tests.
        when(connection.createStatement()).thenReturn(statement);
        scriptRunner = new ScriptRunner(connection);
    }

    @Test
    @DisplayName("should execute a simple script with multiple statements")
    void shouldExecuteStatementsSeparately() throws Exception {
        // given
        String script = "CREATE TABLE users; \n" +
                        "INSERT INTO users (id, name) VALUES (1, 'John Doe');";
        Reader reader = new StringReader(script);

        // when
        scriptRunner.runScript(reader);

        // then
        verify(statement).execute("CREATE TABLE users");
        verify(statement).execute("INSERT INTO users (id, name) VALUES (1, 'John Doe')");
        verify(connection).commit(); // Should commit at the end of a successful script
    }

    @Test
    @DisplayName("should ignore single-line comments")
    void shouldIgnoreComments() {
        // given
        String script = "-- This is a comment\n" +
                        "SELECT * FROM users; // Another comment";
        Reader reader = new StringReader(script);

        // when
        scriptRunner.runScript(reader);

        // then
        verify(statement, times(1)).execute("SELECT * FROM users");
    }

    @Test
    @DisplayName("should ignore blank lines")
    void shouldIgnoreBlankLines() {
        // given
        String script = "\n\nSELECT * FROM users;\n\n";
        Reader reader = new StringReader(script);

        // when
        scriptRunner.runScript(reader);

        // then
        verify(statement, times(1)).execute("SELECT * FROM users");
    }

    @Test
    @DisplayName("should throw exception for a statement missing a delimiter")
    void shouldThrowExceptionForIncompleteStatement() {
        // given
        String script = "SELECT * FROM users"; // Missing semicolon
        Reader reader = new StringReader(script);

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> scriptRunner.runScript(reader));

        // then
        assertTrue(exception.getMessage().contains("Error executing: SELECT * FROM users"));
        assertTrue(exception.getCause().getMessage().contains("Line missing end-of-line terminator"));
    }

    @Test
    @DisplayName("should wrap SQLException in a RuntimeException on execution error")
    void shouldWrapSqlExceptionInRuntimeException() throws SQLException {
        // given
        String script = "BAD SQL;";
        Reader reader = new StringReader(script);
        SQLException sqlException = new SQLException("Syntax error");
        when(statement.execute("BAD SQL")).thenThrow(sqlException);

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> scriptRunner.runScript(reader));

        // then
        assertEquals("Error executing: BAD SQL.  Cause: java.sql.SQLException: Syntax error", exception.getMessage());
        assertEquals(sqlException, exception.getCause());
        verify(connection).rollback(); // Should roll back on error
    }

    @Test
    @DisplayName("should throw exception on warning if throwWarning is enabled")
    void shouldThrowExceptionOnWarningWhenConfigured() throws SQLException {
        // given
        scriptRunner.setThrowWarning(true);
        String script = "SELECT * FROM users;";
        Reader reader = new StringReader(script);

        SQLWarning warning = new SQLWarning("This is a warning");
        when(statement.getWarnings()).thenReturn(warning);

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> scriptRunner.runScript(reader));

        // then
        assertEquals("Error executing: SELECT * FROM users.  Cause: java.sql.SQLWarning: This is a warning", exception.getMessage());
        assertEquals(warning, exception.getCause());
    }

    @Test
    @DisplayName("should remove carriage returns if removeCRs is enabled")
    void shouldRemoveCarriageReturnsWhenConfigured() {
        // given
        scriptRunner.setRemoveCRs(true);
        String scriptWithCR = "SELECT * FROM users;\r\n";
        Reader reader = new StringReader(scriptWithCR);

        // when
        scriptRunner.runScript(reader);

        // then
        // The command passed to the statement should not contain the carriage return.
        verify(statement).execute("SELECT * FROM users");
    }

    @Test
    @DisplayName("should configure escape processing on the statement")
    void shouldSetEscapeProcessingOnStatementWhenConfigured() throws Exception {
        // given
        scriptRunner.setEscapeProcessing(false); // Default is true
        String script = "SELECT 1;";
        Reader reader = new StringReader(script);

        // when
        scriptRunner.runScript(reader);

        // then
        verify(statement).setEscapeProcessing(false);
        verify(statement).execute("SELECT 1");
    }

    @Test
    @DisplayName("should throw exception if connection is null")
    void shouldThrowExceptionWhenConnectionIsNull() {
        // given
        ScriptRunner runnerWithNullConnection = new ScriptRunner(null);
        Reader reader = new StringReader("SELECT 1;");

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> runnerWithNullConnection.runScript(reader));

        // then
        assertEquals("Could not set AutoCommit to false. Cause: java.lang.NullPointerException", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception if connection returns a null statement")
    void shouldThrowExceptionWhenConnectionReturnsNullStatement() throws SQLException {
        // given
        when(connection.createStatement()).thenReturn(null);
        Reader reader = new StringReader("SELECT 1;");

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> scriptRunner.runScript(reader));

        // then
        assertTrue(exception.getMessage().contains("Error executing: SELECT 1"));
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    @Test
    @DisplayName("closeConnection should delegate to the underlying connection")
    void closeConnectionShouldDelegateToConnection() throws SQLException {
        // when
        scriptRunner.closeConnection();

        // then
        verify(connection).close();
    }

    @Test
    @DisplayName("closeConnection should not fail if the connection is null")
    void closeConnectionShouldNotFailOnNullConnection() {
        // given
        ScriptRunner runnerWithNullConnection = new ScriptRunner(null);

        // when & then
        // The method should not throw an exception as it's caught internally.
        assertDoesNotThrow(runnerWithNullConnection::closeConnection);
    }
}
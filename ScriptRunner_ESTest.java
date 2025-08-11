package org.apache.ibatis.jdbc;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.junit.Test;

/**
 * Readable unit tests for ScriptRunner.
 *
 * These tests focus on common scenarios and use straightforward Mockito stubs
 * with descriptive names and comments to aid understanding and maintenance.
 */
public class ScriptRunnerReadableTest {

  /**
   * Helper to create a StringReader from a script string.
   */
  private Reader reader(String script) {
    return new StringReader(script);
  }

  @Test
  public void executesSingleStatement_lineByLine() throws Exception {
    // Given a connection with a statement that executes updates
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false); // false => update count path
    when(st.getUpdateCount()).thenReturn(1);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setLogWriter(null);       // silence logging
    runner.setErrorLogWriter(null);  // silence logging

    // When running a simple, properly terminated statement
    runner.runScript(reader("SELECT 1;"));

    // Then the statement is executed once with the expected command
    verify(st, times(1)).execute(argThat(cmd -> cmd.contains("SELECT 1")));
  }

  @Test
  public void ignoresCommentsAndBlankLines() throws Exception {
    Connection cn = mock(Connection.class);
    when(cn.getAutoCommit()).thenReturn(true);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    // Only comments and blanks => nothing to execute and no exception
    runner.runScript(reader(
        "-- leading comment\n" +
        "// another comment\n" +
        "   -- spaced comment\n" +
        "\n" +
        "\t\n"
    ));

    // No statement should have been created or executed
    verify(cn, never()).createStatement();
  }

  @Test
  public void throwsWhenWarningAndThrowWarningEnabled() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);

    // Simulate a successful execution that yields a warning
    when(st.execute(anyString())).thenReturn(true);
    when(st.getWarnings()).thenReturn(new SQLWarning("boom"));

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setThrowWarning(true);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    try {
      runner.runScript(reader("SELECT 1;"));
      fail("Expected RuntimeException when warnings are present and throwWarning=true");
    } catch (RuntimeException ex) {
      assertTrue(ex.getMessage() == null || ex.getMessage().contains("boom"));
    }
  }

  @Test
  public void supportsCustomDelimiterDirective() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false);
    when(st.getUpdateCount()).thenReturn(1);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    // The delimiter is changed by directive on a comment line.
    // Execution should happen when encountering the token "GO" on a line by itself.
    String script =
        "-- @DELIMITER GO\n" +
        "SELECT 1\n" +
        "GO\n";
    runner.runScript(reader(script));

    verify(st, times(1)).execute(argThat(cmd -> cmd.contains("SELECT 1")));
  }

  @Test
  public void sendFullScript_executesAllAtOnce() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false);
    when(st.getUpdateCount()).thenReturn(2);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setSendFullScript(true);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    String script =
        "SELECT 1;\n" +
        "SELECT 2;\n";
    runner.runScript(reader(script));

    // With sendFullScript=true, ScriptRunner should call execute once with the whole content.
    verify(st, times(1)).execute(argThat(cmd -> cmd.contains("SELECT 1") && cmd.contains("SELECT 2")));
  }

  @Test
  public void escapeProcessingFlagIsAppliedToStatement() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false);
    when(st.getUpdateCount()).thenReturn(1);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setEscapeProcessing(false); // ensure flag is propagated
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    runner.runScript(reader("SELECT 1;"));

    verify(st, atLeastOnce()).setEscapeProcessing(false);
  }

  @Test
  public void nullConnectionCausesMeaningfulFailure() {
    ScriptRunner runner = new ScriptRunner(null);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    try {
      runner.runScript(reader(""));
      fail("Expected RuntimeException when connection is null");
    } catch (RuntimeException ex) {
      // Message content may vary; asserting type is sufficient here for clarity.
      assertNotNull(ex);
    }
  }

  @Test
  public void nullStatementCausesMeaningfulFailure() throws Exception {
    Connection cn = mock(Connection.class);
    when(cn.getAutoCommit()).thenReturn(true);
    when(cn.createStatement()).thenReturn(null);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    try {
      runner.runScript(reader(";"));
      fail("Expected RuntimeException when createStatement() returns null");
    } catch (RuntimeException ex) {
      assertNotNull(ex);
    }
  }

  @Test
  public void removeCRs_stripsCarriageReturnsBeforeExecution() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false);
    when(st.getUpdateCount()).thenReturn(1);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setRemoveCRs(true);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    runner.runScript(reader("SELECT 1\r\n;"));

    verify(st).execute(argThat(cmd -> !cmd.contains("\r")));
  }

  @Test
  public void fullLineDelimiter_semicolonOnOwnLineIsAccepted() throws Exception {
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(false);
    when(st.getUpdateCount()).thenReturn(1);

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setFullLineDelimiter(true);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    // With fullLineDelimiter=true, a line with only ';' terminates the command.
    runner.runScript(reader("SELECT 1\n;\n"));

    verify(st, times(1)).execute(argThat(cmd -> cmd.contains("SELECT 1")));
  }

  @Test
  public void closeConnection_deprecatedDoesNotThrow() {
    Connection cn = mock(Connection.class);
    ScriptRunner runner = new ScriptRunner(cn);

    // The method is deprecated, but should be safe to call.
    runner.closeConnection();
  }

  @Test
  public void printsResultsWhenQueryReturnsResultSet() throws Exception {
    // This test ensures queries that produce a ResultSet do not throw and are "printed".
    // It avoids asserting the actual printed content for simplicity.
    Connection cn = mock(Connection.class);
    Statement st = mock(Statement.class);
    ResultSet rs = mock(ResultSet.class);
    ResultSetMetaData md = mock(ResultSetMetaData.class);

    when(cn.createStatement()).thenReturn(st);
    when(cn.getAutoCommit()).thenReturn(true);
    when(st.execute(anyString())).thenReturn(true); // true => has results
    when(st.getResultSet()).thenReturn(rs);
    when(rs.getMetaData()).thenReturn(md);
    when(md.getColumnCount()).thenReturn(1);
    when(md.getColumnLabel(1)).thenReturn("col");
    when(rs.next()).thenReturn(true, false);
    when(rs.getString(1)).thenReturn("value");

    ScriptRunner runner = new ScriptRunner(cn);
    runner.setLogWriter(null);      // Avoid noisy output in test
    runner.setErrorLogWriter(null);

    runner.runScript(reader("SELECT 'value';"));

    verify(st, times(1)).execute(anyString());
  }
}
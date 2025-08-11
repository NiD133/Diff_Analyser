package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ScriptRunnerTest extends BaseDataTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  // Common script resources
  private static final String RES_MISSING_EOL = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";
  private static final String RES_COMMENT_AFTER_EOL = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";
  private static final String RES_CHANGING_DELIM = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";
  private static final String RES_CHANGING_DELIM_MISSING = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";

  // ---------------------------------------------------------------------------
  // Happy path: executing JPetStore scripts
  // ---------------------------------------------------------------------------

  @Test
  @Disabled("Fails with HSQLDB 2.0 due to create index statements in the schema script")
  void runsFullScriptInOneGo() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    Connection conn = ds.getConnection();

    // Intentionally closing the connection after creating ScriptRunner to mimic
    // historical behavior being tested when 'sendFullScript' is enabled.
    ScriptRunner runner = quiet(new ScriptRunner(conn));
    runner.setSendFullScript(true);
    runner.setAutoCommit(true);
    runner.setStopOnError(false);
    conn.close();

    runJPetStoreScripts(runner);
    assertProductsTableExistsAndLoaded();
  }

  @Test
  void runsScriptsUsingProvidedConnection() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection()) {
      ScriptRunner runner = configuredRunner(conn, true, false, false);
      runJPetStoreScripts(runner);
    }
    assertProductsTableExistsAndLoaded();
  }

  @Test
  void runsScriptsUsingPropertiesBackedDataSource() throws Exception {
    Properties props = Resources.getResourceAsProperties(JPETSTORE_PROPERTIES);
    DataSource dataSource = new UnpooledDataSource(
        props.getProperty("driver"),
        props.getProperty("url"),
        props.getProperty("username"),
        props.getProperty("password"));

    ScriptRunner runner = configuredRunner(dataSource.getConnection(), true, false, false);
    runJPetStoreScripts(runner);
    assertProductsTableExistsAndLoaded();
  }

  // ---------------------------------------------------------------------------
  // Error handling and delimiter behavior
  // ---------------------------------------------------------------------------

  @Test
  void failsWithHelpfulMessageWhenEndOfLineTerminatorIsMissing() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(RES_MISSING_EOL)) {
      ScriptRunner runner = configuredRunner(conn, true, false, false);
      Exception ex = assertThrows(Exception.class, () -> runner.runScript(reader));
      // The message should guide users toward the missing EOL terminator problem.
      org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("end-of-line terminator"));
    }
  }

  @Test
  void ignoresCommentsThatFollowStatementDelimiter() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(RES_COMMENT_AFTER_EOL)) {
      ScriptRunner runner = configuredRunner(conn, false, true, false);
      // Ensure DB exists to run against
      runJPetStoreScripts(runner);
      runner.runScript(reader);
    }
  }

  @Test
  void failsWhenUsingInvalidOrNotCurrentDelimiter() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(RES_CHANGING_DELIM_MISSING)) {
      ScriptRunner runner = configuredRunner(conn, false, true, false);
      Exception ex = assertThrows(Exception.class, () -> runner.runScript(reader));
      org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("end-of-line terminator"));
    }
  }

  @Test
  void canChangeDelimiterWithinScript() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(RES_CHANGING_DELIM)) {
      ScriptRunner runner = configuredRunner(conn, false, true, false);
      // Ensure DB exists to run against
      runJPetStoreScripts(runner);
      runner.runScript(reader);
    }
  }

  // ---------------------------------------------------------------------------
  // Logging behavior
  // ---------------------------------------------------------------------------

  @Test
  void logsStatementsAndResultsWhenStreamingStatements() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection()) {
      ScriptRunner runner = configuredRunner(conn, true, false, false);
      StringWriter sw = new StringWriter();
      runner.setLogWriter(new PrintWriter(sw));

      Reader reader = new StringReader("select userid from account where userid = 'j2ee';");
      runner.runScript(reader);

      String expected =
          "select userid from account where userid = 'j2ee'" + LINE_SEPARATOR
              + LINE_SEPARATOR
              + "USERID\t" + LINE_SEPARATOR
              + "j2ee\t" + LINE_SEPARATOR;
      assertEquals(expected, sw.toString());
    }
  }

  @Test
  void logsStatementsAndResultsWhenSendingFullScript() throws Exception {
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection()) {
      ScriptRunner runner = configuredRunner(conn, true, false, true);
      StringWriter sw = new StringWriter();
      runner.setLogWriter(new PrintWriter(sw));

      Reader reader = new StringReader("select userid from account where userid = 'j2ee';");
      runner.runScript(reader);

      String expected =
          "select userid from account where userid = 'j2ee';" + LINE_SEPARATOR
              + LINE_SEPARATOR
              + "USERID\t" + LINE_SEPARATOR
              + "j2ee\t" + LINE_SEPARATOR;
      assertEquals(expected, sw.toString());
    }
  }

  // ---------------------------------------------------------------------------
  // Advanced: delimiter variations
  // ---------------------------------------------------------------------------

  @Test
  void acceptsVariousDelimiterDirectivesAndCases() throws Exception {
    Connection conn = mock(Connection.class);
    Statement stmt = mock(Statement.class);
    when(conn.createStatement()).thenReturn(stmt);
    when(stmt.getUpdateCount()).thenReturn(-1);

    ScriptRunner runner = new ScriptRunner(conn);

    String sql = """
        -- @DELIMITER |\s
        line 1;
        line 2;
        |
        //  @DELIMITER  ;
        line 3;\s
        -- //@deLimiTer $  blah
        line 4$
        // //@DELIMITER %
        line 5%
        """;
    Reader reader = new StringReader(sql);
    runner.runScript(reader);

    verify(stmt).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(stmt).execute("line 3" + LINE_SEPARATOR);
    verify(stmt).execute("line 4" + LINE_SEPARATOR);
    verify(stmt).execute("line 5" + LINE_SEPARATOR);
  }

  @Test
  void acceptsMultiCharacterDelimiter() throws Exception {
    Connection conn = mock(Connection.class);
    Statement stmt = mock(Statement.class);
    when(conn.createStatement()).thenReturn(stmt);
    when(stmt.getUpdateCount()).thenReturn(-1);

    ScriptRunner runner = new ScriptRunner(conn);

    String sql = """
        -- @DELIMITER ||\s
        line 1;
        line 2;
        ||
        //  @DELIMITER  ;
        line 3;\s
        """;
    Reader reader = new StringReader(sql);
    runner.runScript(reader);

    verify(stmt).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(stmt).execute("line 3" + LINE_SEPARATOR);
  }

  // ---------------------------------------------------------------------------
  // Miscellaneous helper behavior (kept for parity with existing test content)
  // ---------------------------------------------------------------------------

  @Test
  void returnsSameMutableInstanceFromHelper() {
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = appendAbc(sb);
    assertSame(sb, sb2);
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private ScriptRunner configuredRunner(Connection conn, boolean autoCommit, boolean stopOnError, boolean sendFullScript) {
    ScriptRunner runner = quiet(new ScriptRunner(conn));
    runner.setAutoCommit(autoCommit);
    runner.setStopOnError(stopOnError);
    runner.setSendFullScript(sendFullScript);
    return runner;
  }

  private ScriptRunner quiet(ScriptRunner runner) {
    // Avoid polluting test output. Null writers mean "no logging".
    runner.setErrorLogWriter(null);
    runner.setLogWriter(null);
    return runner;
  }

  private void runJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
    runScript(runner, JPETSTORE_DDL);
    runScript(runner, JPETSTORE_DATA);
  }

  private void assertProductsTableExistsAndLoaded() throws IOException, SQLException {
    PooledDataSource ds = createPooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection()) {
      SqlRunner executor = new SqlRunner(conn);
      List<Map<String, Object>> products = executor.selectAll("SELECT * FROM PRODUCT");
      assertEquals(16, products.size());
    } finally {
      ds.forceCloseAll();
    }
  }

  private StringBuilder appendAbc(StringBuilder sb) {
    sb.append("ABC");
    return sb;
  }
}
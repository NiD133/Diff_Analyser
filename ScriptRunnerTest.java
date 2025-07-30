package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
  private static final String JPETSTORE_PROPERTIES = "path/to/jpetstore/properties";
  private static final String JPETSTORE_DDL = "path/to/jpetstore/ddl";
  private static final String JPETSTORE_DATA = "path/to/jpetstore/data";

  // Test to verify running scripts by sending the full script at once
  @Test
  @Disabled("This fails with HSQLDB 2.0 due to the create index statements in the schema script")
  void testRunScriptsBySendingFullScriptAtOnce() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    Connection connection = dataSource.getConnection();
    ScriptRunner scriptRunner = new ScriptRunner(connection);

    // Configure ScriptRunner
    scriptRunner.setSendFullScript(true);
    scriptRunner.setAutoCommit(true);
    scriptRunner.setStopOnError(false);
    scriptRunner.setErrorLogWriter(null);
    scriptRunner.setLogWriter(null);

    connection.close();
    runJPetStoreScripts(scriptRunner);
    assertProductsTableExistsAndLoaded();
  }

  // Test to verify running scripts using a connection
  @Test
  void testRunScriptsUsingConnection() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(true);
      scriptRunner.setStopOnError(false);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setLogWriter(null);

      runJPetStoreScripts(scriptRunner);
    }
    assertProductsTableExistsAndLoaded();
  }

  // Test to verify running scripts using properties
  @Test
  void testRunScriptsUsingProperties() throws Exception {
    Properties properties = Resources.getResourceAsProperties(JPETSTORE_PROPERTIES);
    DataSource dataSource = new UnpooledDataSource(
        properties.getProperty("driver"),
        properties.getProperty("url"),
        properties.getProperty("username"),
        properties.getProperty("password")
    );
    ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());

    // Configure ScriptRunner
    scriptRunner.setAutoCommit(true);
    scriptRunner.setStopOnError(false);
    scriptRunner.setErrorLogWriter(null);
    scriptRunner.setLogWriter(null);

    runJPetStoreScripts(scriptRunner);
    assertProductsTableExistsAndLoaded();
  }

  // Test to verify warning if end-of-line terminator is not found
  @Test
  void testWarningIfEndOfLineTerminatorNotFound() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    String scriptResource = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";
    try (Connection connection = dataSource.getConnection();
         Reader reader = Resources.getResourceAsReader(scriptResource)) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(true);
      scriptRunner.setStopOnError(false);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setLogWriter(null);

      try {
        scriptRunner.runScript(reader);
        fail("Expected script runner to fail due to missing end of line terminator.");
      } catch (Exception e) {
        assertTrue(e.getMessage().contains("end-of-line terminator"));
      }
    }
  }

  // Test to verify that comments after statement delimiters do not cause runner failure
  @Test
  void testCommentAfterStatementDelimiter() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    String scriptResource = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";
    try (Connection connection = dataSource.getConnection();
         Reader reader = Resources.getResourceAsReader(scriptResource)) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(true);
      scriptRunner.setStopOnError(true);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setLogWriter(null);

      runJPetStoreScripts(scriptRunner);
      scriptRunner.runScript(reader);
    }
  }

  // Test to verify warning if not the current delimiter is used
  @Test
  void testWarningIfNotCurrentDelimiterUsed() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    String scriptResource = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";
    try (Connection connection = dataSource.getConnection();
         Reader reader = Resources.getResourceAsReader(scriptResource)) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(false);
      scriptRunner.setStopOnError(true);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setLogWriter(null);

      try {
        scriptRunner.runScript(reader);
        fail("Expected script runner to fail due to the usage of invalid delimiter.");
      } catch (Exception e) {
        assertTrue(e.getMessage().contains("end-of-line terminator"));
      }
    }
  }

  // Test to verify that changing delimiter does not cause runner failure
  @Test
  void testChangingDelimiter() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    String scriptResource = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";
    try (Connection connection = dataSource.getConnection();
         Reader reader = Resources.getResourceAsReader(scriptResource)) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(false);
      scriptRunner.setStopOnError(true);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setLogWriter(null);

      runJPetStoreScripts(scriptRunner);
      scriptRunner.runScript(reader);
    }
  }

  // Test to verify logging functionality
  @Test
  void testLogging() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(true);
      scriptRunner.setStopOnError(false);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setSendFullScript(false);

      StringWriter stringWriter = new StringWriter();
      PrintWriter logWriter = new PrintWriter(stringWriter);
      scriptRunner.setLogWriter(logWriter);

      Reader reader = new StringReader("select userid from account where userid = 'j2ee';");
      scriptRunner.runScript(reader);

      assertEquals("select userid from account where userid = 'j2ee'" + LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t"
          + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR, stringWriter.toString());
    }
  }

  // Test to verify logging functionality with full script
  @Test
  void testLoggingFullScript() throws Exception {
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      ScriptRunner scriptRunner = new ScriptRunner(connection);

      // Configure ScriptRunner
      scriptRunner.setAutoCommit(true);
      scriptRunner.setStopOnError(false);
      scriptRunner.setErrorLogWriter(null);
      scriptRunner.setSendFullScript(true);

      StringWriter stringWriter = new StringWriter();
      PrintWriter logWriter = new PrintWriter(stringWriter);
      scriptRunner.setLogWriter(logWriter);

      Reader reader = new StringReader("select userid from account where userid = 'j2ee';");
      scriptRunner.runScript(reader);

      assertEquals("select userid from account where userid = 'j2ee';" + LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t"
          + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR, stringWriter.toString());
    }
  }

  // Helper method to run JPetStore scripts
  private void runJPetStoreScripts(ScriptRunner scriptRunner) throws IOException, SQLException {
    runScript(scriptRunner, JPETSTORE_DDL);
    runScript(scriptRunner, JPETSTORE_DATA);
  }

  // Helper method to assert that the products table exists and is loaded
  private void assertProductsTableExistsAndLoaded() throws IOException, SQLException {
    PooledDataSource dataSource = createPooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      SqlRunner sqlRunner = new SqlRunner(connection);
      List<Map<String, Object>> products = sqlRunner.selectAll("SELECT * FROM PRODUCT");
      assertEquals(16, products.size());
    } finally {
      dataSource.forceCloseAll();
    }
  }

  // Test to verify acceptance of delimiter variations
  @Test
  void testAcceptDelimiterVariations() throws Exception {
    Connection mockConnection = mock(Connection.class);
    Statement mockStatement = mock(Statement.class);
    when(mockConnection.createStatement()).thenReturn(mockStatement);
    when(mockStatement.getUpdateCount()).thenReturn(-1);

    ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

    String sqlScript = """
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
    Reader reader = new StringReader(sqlScript);
    scriptRunner.runScript(reader);

    verify(mockStatement).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(mockStatement).execute("line 3" + LINE_SEPARATOR);
    verify(mockStatement).execute("line 4" + LINE_SEPARATOR);
    verify(mockStatement).execute("line 5" + LINE_SEPARATOR);
  }

  // Simple test to verify StringBuilder behavior
  @Test
  void testStringBuilderBehavior() {
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = appendABC(sb);
    assertSame(sb, sb2);
  }

  private StringBuilder appendABC(StringBuilder sb) {
    sb.append("ABC");
    return sb;
  }

  // Test to verify acceptance of multi-character delimiters
  @Test
  void testAcceptMultiCharDelimiter() throws Exception {
    Connection mockConnection = mock(Connection.class);
    Statement mockStatement = mock(Statement.class);
    when(mockConnection.createStatement()).thenReturn(mockStatement);
    when(mockStatement.getUpdateCount()).thenReturn(-1);

    ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

    String sqlScript = """
        -- @DELIMITER ||\s
        line 1;
        line 2;
        ||
        //  @DELIMITER  ;
        line 3;\s
        """;
    Reader reader = new StringReader(sqlScript);
    scriptRunner.runScript(reader);

    verify(mockStatement).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(mockStatement).execute("line 3" + LINE_SEPARATOR);
  }
}
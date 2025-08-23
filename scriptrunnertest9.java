package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for ScriptRunner's logging capabilities.
 */
class ScriptRunnerLoggingTest extends BaseDataTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @BeforeAll
  static void setup() throws Exception {
    // Create the database schema and populate it
    // This is necessary for the SELECT statement in the test to run successfully.
    runScript(createUnpooledDataSource(JPETSTORE_PROPERTIES), JPETSTORE_DDL);
    runScript(createUnpooledDataSource(JPETSTORE_PROPERTIES), JPETSTORE_DATA);
  }

  @Test
  void shouldLogFullScriptAndResultsWhenSendFullScriptIsTrue() throws Exception {
    // Arrange
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    StringWriter stringWriter = new StringWriter();
    PrintWriter logWriter = new PrintWriter(stringWriter);

    try (Connection conn = ds.getConnection()) {
      ScriptRunner runner = new ScriptRunner(conn);
      runner.setSendFullScript(true);
      runner.setAutoCommit(true);
      runner.setStopOnError(false);
      runner.setErrorLogWriter(null); // Suppress error logs for this test
      runner.setLogWriter(logWriter);

      String sqlScript = "select userid from account where userid = 'j2ee';";
      Reader reader = new StringReader(sqlScript);

      // Act
      runner.runScript(reader);

      // Assert
      // The log should contain the full script, a blank line, the column header, and the result row.
      String expectedLog = String.join(LINE_SEPARATOR,
          "select userid from account where userid = 'j2ee';",
          "",
          "USERID\t",
          "j2ee\t") + LINE_SEPARATOR;

      assertEquals(expectedLog, stringWriter.toString());
    }
  }
}
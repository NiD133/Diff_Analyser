package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;

/**
 * Tests for ScriptRunner's error handling capabilities.
 */
class ScriptRunnerErrorHandlingTest extends BaseDataTest {

  /**
   * A ScriptRunner should stop execution and throw an exception if a SQL statement
   * is not properly terminated by a delimiter.
   * This test verifies that behavior using a script with a missing final delimiter.
   */
  @Test
  void shouldThrowExceptionForStatementMissingTerminator() throws Exception {
    // Given
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    String scriptWithMissingDelimiter = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";

    try (Connection connection = dataSource.getConnection();
        Reader reader = Resources.getResourceAsReader(scriptWithMissingDelimiter)) {

      ScriptRunner runner = new ScriptRunner(connection);
      runner.setStopOnError(true);
      // Suppress logs for clean test output
      runner.setLogWriter(null);
      runner.setErrorLogWriter(null);

      // When & Then
      JdbcSqlException thrown = assertThrows(JdbcSqlException.class, () -> runner.runScript(reader),
          "ScriptRunner should fail when a statement is missing its delimiter.");

      assertTrue(thrown.getMessage().contains("missing end-of-line terminator"),
          "Exception message should indicate a missing delimiter.");
    }
  }
}
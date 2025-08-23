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
 * Tests for the ScriptRunner class, focusing on edge cases and error handling.
 */
class ScriptRunnerTest extends BaseDataTest {

  private static final String SCRIPT_WITH_MISSING_TERMINATOR = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";

  /**
   * Verifies that the ScriptRunner throws an exception if it processes a script
   * where the final SQL statement is not properly terminated by a delimiter.
   */
  @Test
  void shouldThrowExceptionWhenScriptIsMissingTerminator() throws Exception {
    // Arrange
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = ds.getConnection();
        Reader reader = Resources.getResourceAsReader(SCRIPT_WITH_MISSING_TERMINATOR)) {

      ScriptRunner runner = new ScriptRunner(connection);
      runner.setStopOnError(false);
      runner.setAutoCommit(true);
      runner.setLogWriter(null);        // Suppress console output for clean test logs
      runner.setErrorLogWriter(null);   // Suppress error output for clean test logs

      // Act & Assert
      // Expect an exception because the script file contains a statement without a final delimiter.
      JdbcSqlException thrown = assertThrows(JdbcSqlException.class, () -> runner.runScript(reader));

      assertTrue(thrown.getMessage().contains("Script missing end-of-line terminator"));
    }
  }
}
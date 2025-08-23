package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;

/**
 * Tests for edge cases in ScriptRunner, focusing on comment handling.
 */
class ScriptRunnerCommentHandlingTest extends BaseDataTest {

  private static final String SCRIPT_WITH_COMMENT_AFTER_DELIMITER = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";

  /**
   * A script with a comment immediately following a statement delimiter (e.g., "SELECT 1; -- comment")
   * should execute without errors.
   */
  @Test
  void shouldNotFailOnScriptWithCommentAfterDelimiter() throws Exception {
    // Arrange
    DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection conn = ds.getConnection()) {
      ScriptRunner runner = new ScriptRunner(conn);
      runner.setAutoCommit(true);
      runner.setStopOnError(true);
      runner.setErrorLogWriter(null); // Suppress error log for clean test output
      runner.setLogWriter(null);      // Suppress regular log for clean test output

      // Act & Assert
      assertDoesNotThrow(() -> {
        // First, run setup scripts to establish a baseline database state.
        runJPetStoreScripts(runner);
        // Then, run the specific script under test.
        try (Reader reader = Resources.getResourceAsReader(SCRIPT_WITH_COMMENT_AFTER_DELIMITER)) {
          runner.runScript(reader);
        }
      }, "ScriptRunner should not fail when a comment follows a statement delimiter.");
    }
  }

  /**
   * Helper method to run the standard JPetStore DDL and data scripts.
   */
  private void runJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
    runScript(runner, JPETSTORE_DDL);
    runScript(runner, JPETSTORE_DATA);
  }
}
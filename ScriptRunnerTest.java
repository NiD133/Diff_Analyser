/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ScriptRunner Test Suite")
class ScriptRunnerTest extends BaseDataTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  /**
   * Tests focused on the core functionality of running database scripts.
   */
  @Nested
  @DisplayName("Script Execution")
  class ScriptExecutionTests {

    @Test
    @Disabled("This fails with HSQLDB 2.0 due to the create index statements in the schema script")
    @DisplayName("should execute all scripts when sendFullScript is true")
    void shouldExecuteAllScriptsWhenSendFullScriptIsTrue() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      try (Connection conn = ds.getConnection()) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setSendFullScript(true);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);

        // Act
        runJPetStoreScripts(runner);
      }

      // Assert
      assertProductsTableExistsAndLoaded();
    }

    @Test
    @DisplayName("should execute scripts successfully using a direct connection")
    void shouldExecuteScriptsSuccessfullyUsingConnection() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      try (Connection conn = ds.getConnection()) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);

        // Act
        runJPetStoreScripts(runner);
      }

      // Assert
      assertProductsTableExistsAndLoaded();
    }

    @Test
    @DisplayName("should execute scripts successfully using data source properties")
    void shouldExecuteScriptsSuccessfullyUsingDataSourceProperties() throws Exception {
      // Arrange
      Properties props = Resources.getResourceAsProperties(JPETSTORE_PROPERTIES);
      DataSource dataSource = new UnpooledDataSource(props.getProperty("driver"), props.getProperty("url"),
          props.getProperty("username"), props.getProperty("password"));
      try (Connection conn = dataSource.getConnection()) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);

        // Act
        runJPetStoreScripts(runner);
      }

      // Assert
      assertProductsTableExistsAndLoaded();
    }
  }

  /**
   * Tests focused on how the ScriptRunner handles errors and invalid scripts.
   */
  @Nested
  @DisplayName("Error Handling")
  class ErrorHandlingTests {

    @Test
    @DisplayName("should throw exception for script missing the end-of-line terminator")
    void shouldThrowExceptionForScriptMissingEOLTerminator() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      String resource = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";
      try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(resource)) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setStopOnError(false); // This is key for the original test's intent

        // Act & Assert
        Exception e = assertThrows(RuntimeException.class, () -> runner.runScript(reader));
        assertTrue(e.getMessage().contains("end-of-line terminator"));
      }
    }

    @Test
    @DisplayName("should throw exception when changing to an undefined delimiter")
    void shouldThrowExceptionWhenChangingToUndefinedDelimiter() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      String resource = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";
      try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(resource)) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setStopOnError(true);

        // Act & Assert
        Exception e = assertThrows(RuntimeException.class, () -> runner.runScript(reader));
        assertTrue(e.getMessage().contains("end-of-line terminator"));
      }
    }
  }

  /**
   * Tests focused on handling custom delimiters and comments within scripts.
   */
  @Nested
  @DisplayName("Delimiter and Comment Handling")
  class DelimiterAndCommentHandlingTests {

    @Test
    @DisplayName("should not fail when a comment exists after a statement delimiter")
    void shouldNotFailWhenCommentExistsAfterDelimiter() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      String resource = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";
      try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(resource)) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setStopOnError(true);
        runJPetStoreScripts(runner);

        // Act & Assert (should not throw)
        runner.runScript(reader);
      }
    }

    @Test
    @DisplayName("should execute script correctly when the delimiter is changed")
    void shouldExecuteScriptWithChangingDelimiters() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      String resource = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";
      try (Connection conn = ds.getConnection(); Reader reader = Resources.getResourceAsReader(resource)) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setStopOnError(true);
        runJPetStoreScripts(runner);

        // Act & Assert (should not throw)
        runner.runScript(reader);
      }
    }
  }

  /**
   * Tests focused on the logging output of the ScriptRunner.
   */
  @Nested
  @DisplayName("Logging")
  class LoggingTests {
    private StringWriter sw;
    private PrintWriter logWriter;

    @BeforeEach
    void setup() {
      sw = new StringWriter();
      logWriter = new PrintWriter(sw);
    }

    @Test
    @DisplayName("should log executed statements and their results")
    void shouldLogExecutedStatementsAndResults() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      try (Connection conn = ds.getConnection()) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setLogWriter(logWriter);
        Reader reader = new StringReader("select userid from account where userid = 'j2ee';");

        // Act
        runner.runScript(reader);

        // Assert
        String expectedLog = "select userid from account where userid = 'j2ee'" + LINE_SEPARATOR + LINE_SEPARATOR
            + "USERID\t" + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR;
        assertEquals(expectedLog, sw.toString());
      }
    }

    @Test
    @DisplayName("should log the full script when sendFullScript is true")
    void shouldLogFullScriptWhenSendFullScriptIsTrue() throws Exception {
      // Arrange
      DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
      try (Connection conn = ds.getConnection()) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setSendFullScript(true);
        runner.setLogWriter(logWriter);
        Reader reader = new StringReader("select userid from account where userid = 'j2ee';");

        // Act
        runner.runScript(reader);

        // Assert
        String expectedLog = "select userid from account where userid = 'j2ee';" + LINE_SEPARATOR + LINE_SEPARATOR
            + "USERID\t" + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR;
        assertEquals(expectedLog, sw.toString());
      }
    }
  }

  /**
   * Tests for delimiter parsing logic, using mocks to isolate the behavior.
   */
  @Nested
  @DisplayName("Delimiter Parsing (with Mocks)")
  class DelimiterParsingTests {
    private ScriptRunner runner;
    private Statement stmt;

    @BeforeEach
    void setup() throws SQLException {
      Connection conn = mock(Connection.class);
      stmt = mock(Statement.class);
      when(conn.createStatement()).thenReturn(stmt);
      when(stmt.getUpdateCount()).thenReturn(-1);
      runner = new ScriptRunner(conn);
    }

    @Test
    @DisplayName("should correctly parse various delimiter formats")
    void shouldCorrectlyParseVariousDelimiterFormats() throws Exception {
      // Arrange
      String sql = """
          -- @DELIMITER |
          line 1;
          line 2;
          |
          //  @DELIMITER  ;
          line 3;
          -- //@deLimiTer $  blah
          line 4$
          // //@DELIMITER %
          line 5%
          """;

      // Act
      runner.runScript(new StringReader(sql));

      // Assert
      verify(stmt).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR);
      verify(stmt).execute("line 3" + LINE_SEPARATOR);
      verify(stmt).execute("line 4" + LINE_SEPARATOR);
      verify(stmt).execute("line 5" + LINE_SEPARATOR);
    }

    @Test
    @DisplayName("should correctly parse a multi-character delimiter")
    void shouldCorrectlyParseMultiCharacterDelimiter() throws Exception {
      // Arrange
      String sql = """
          -- @DELIMITER ||
          line 1;
          line 2;
          ||
          //  @DELIMITER  ;
          line 3;
          """;

      // Act
      runner.runScript(new StringReader(sql));

      // Assert
      verify(stmt).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR);
      verify(stmt).execute("line 3" + LINE_SEPARATOR);
    }
  }

  // -- Helper Methods --

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
}
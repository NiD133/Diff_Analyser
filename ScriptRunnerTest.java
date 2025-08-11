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

/**
 * Tests for ScriptRunner functionality including script execution modes,
 * delimiter handling, error handling, and logging capabilities.
 */
class ScriptRunnerTest extends BaseDataTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();
  private static final String EXPECTED_PRODUCT_COUNT = "16";
  private static final int EXPECTED_PRODUCTS_IN_DB = 16;

  // Test Resources
  private static final String SCRIPT_MISSING_EOL_TERMINATOR = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";
  private static final String SCRIPT_COMMENT_AFTER_TERMINATOR = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";
  private static final String SCRIPT_CHANGING_DELIMITER_MISSING = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";
  private static final String SCRIPT_CHANGING_DELIMITER = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";

  // =============================================================================
  // Script Execution Mode Tests
  // =============================================================================

  @Test
  @Disabled("This fails with HSQLDB 2.0 due to the create index statements in the schema script")
  void shouldExecuteScriptsInFullScriptMode() throws Exception {
    // Given: A ScriptRunner configured to send full script at once
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    Connection connection = dataSource.getConnection();
    ScriptRunner scriptRunner = createScriptRunnerWithFullScriptMode(connection);
    
    // When: Running JPetStore scripts
    connection.close();
    executeJPetStoreScripts(scriptRunner);
    
    // Then: Database should be properly initialized
    assertJPetStoreDataIsLoaded();
  }

  @Test
  void shouldExecuteScriptsUsingDatabaseConnection() throws Exception {
    // Given: A ScriptRunner with database connection
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      ScriptRunner scriptRunner = createStandardScriptRunner(connection);
      
      // When: Running JPetStore scripts
      executeJPetStoreScripts(scriptRunner);
    }
    
    // Then: Database should be properly initialized
    assertJPetStoreDataIsLoaded();
  }

  @Test
  void shouldExecuteScriptsUsingDatabaseProperties() throws Exception {
    // Given: A ScriptRunner created from database properties
    Properties databaseProperties = Resources.getResourceAsProperties(JPETSTORE_PROPERTIES);
    DataSource dataSource = createDataSourceFromProperties(databaseProperties);
    ScriptRunner scriptRunner = createStandardScriptRunner(dataSource.getConnection());
    
    // When: Running JPetStore scripts
    executeJPetStoreScripts(scriptRunner);
    
    // Then: Database should be properly initialized
    assertJPetStoreDataIsLoaded();
  }

  // =============================================================================
  // Error Handling Tests
  // =============================================================================

  @Test
  void shouldFailWhenScriptMissingEndOfLineTerminator() throws Exception {
    // Given: A script missing end-of-line terminator
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection(); 
         Reader scriptReader = Resources.getResourceAsReader(SCRIPT_MISSING_EOL_TERMINATOR)) {
      
      ScriptRunner scriptRunner = createStandardScriptRunner(connection);

      // When & Then: Running script should fail with appropriate error message
      Exception exception = assertScriptExecutionFails(scriptRunner, scriptReader);
      assertTrue(exception.getMessage().contains("end-of-line terminator"),
          "Error message should mention missing end-of-line terminator");
    }
  }

  @Test
  void shouldFailWhenUsingInvalidDelimiter() throws Exception {
    // Given: A script using invalid delimiter
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection(); 
         Reader scriptReader = Resources.getResourceAsReader(SCRIPT_CHANGING_DELIMITER_MISSING)) {
      
      ScriptRunner scriptRunner = createStrictScriptRunner(connection);

      // When & Then: Running script should fail with delimiter error
      Exception exception = assertScriptExecutionFails(scriptRunner, scriptRunner);
      assertTrue(exception.getMessage().contains("end-of-line terminator"),
          "Error message should mention delimiter issue");
    }
  }

  // =============================================================================
  // Delimiter Handling Tests
  // =============================================================================

  @Test
  void shouldHandleCommentsAfterStatementDelimiter() throws Exception {
    // Given: A script with comments after statement delimiters
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection(); 
         Reader scriptReader = Resources.getResourceAsReader(SCRIPT_COMMENT_AFTER_TERMINATOR)) {
      
      ScriptRunner scriptRunner = createStrictScriptRunner(connection);
      executeJPetStoreScripts(scriptRunner);
      
      // When: Running script with comments after delimiters
      scriptRunner.runScript(scriptReader);
      
      // Then: Script should execute successfully (no exception thrown)
    }
  }

  @Test
  void shouldHandleChangingDelimitersCorrectly() throws Exception {
    // Given: A script that changes delimiters during execution
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection(); 
         Reader scriptReader = Resources.getResourceAsReader(SCRIPT_CHANGING_DELIMITER)) {
      
      ScriptRunner scriptRunner = createStrictScriptRunner(connection);
      executeJPetStoreScripts(scriptRunner);
      
      // When: Running script with changing delimiters
      scriptRunner.runScript(scriptReader);
      
      // Then: Script should execute successfully (no exception thrown)
    }
  }

  @Test
  void shouldAcceptVariousDelimiterFormats() throws Exception {
    // Given: Mock database connection and statement
    Connection mockConnection = mock(Connection.class);
    Statement mockStatement = mock(Statement.class);
    when(mockConnection.createStatement()).thenReturn(mockStatement);
    when(mockStatement.getUpdateCount()).thenReturn(-1);
    
    ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

    // When: Running script with various delimiter formats
    String scriptWithVariousDelimiters = createScriptWithVariousDelimiters();
    Reader scriptReader = new StringReader(scriptWithVariousDelimiters);
    scriptRunner.runScript(scriptReader);

    // Then: Each statement should be executed with correct content
    verify(mockStatement).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(mockStatement).execute("line 3" + LINE_SEPARATOR);
    verify(mockStatement).execute("line 4" + LINE_SEPARATOR);
    verify(mockStatement).execute("line 5" + LINE_SEPARATOR);
  }

  @Test
  void shouldAcceptMultiCharacterDelimiters() throws Exception {
    // Given: Mock database connection and statement
    Connection mockConnection = mock(Connection.class);
    Statement mockStatement = mock(Statement.class);
    when(mockConnection.createStatement()).thenReturn(mockStatement);
    when(mockStatement.getUpdateCount()).thenReturn(-1);
    
    ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

    // When: Running script with multi-character delimiter
    String scriptWithMultiCharDelimiter = createScriptWithMultiCharDelimiter();
    Reader scriptReader = new StringReader(scriptWithMultiCharDelimiter);
    scriptRunner.runScript(scriptReader);

    // Then: Statements should be parsed correctly with multi-char delimiter
    verify(mockStatement).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
    verify(mockStatement).execute("line 3" + LINE_SEPARATOR);
  }

  // =============================================================================
  // Logging Tests
  // =============================================================================

  @Test
  void shouldLogStatementExecutionInLineByLineMode() throws Exception {
    // Given: ScriptRunner with logging enabled in line-by-line mode
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      StringWriter logOutput = new StringWriter();
      ScriptRunner scriptRunner = createScriptRunnerWithLogging(connection, logOutput, false);

      // When: Running a simple query
      String simpleQuery = "select userid from account where userid = 'j2ee';";
      Reader queryReader = new StringReader(simpleQuery);
      scriptRunner.runScript(queryReader);

      // Then: Log should contain statement without delimiter and results
      String expectedLog = buildExpectedLogOutput("select userid from account where userid = 'j2ee'", "j2ee");
      assertEquals(expectedLog, logOutput.toString());
    }
  }

  @Test
  void shouldLogStatementExecutionInFullScriptMode() throws Exception {
    // Given: ScriptRunner with logging enabled in full script mode
    DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      StringWriter logOutput = new StringWriter();
      ScriptRunner scriptRunner = createScriptRunnerWithLogging(connection, logOutput, true);

      // When: Running a simple query
      String simpleQuery = "select userid from account where userid = 'j2ee';";
      Reader queryReader = new StringReader(simpleQuery);
      scriptRunner.runScript(queryReader);

      // Then: Log should contain full statement with delimiter and results
      String expectedLog = buildExpectedLogOutput("select userid from account where userid = 'j2ee';", "j2ee");
      assertEquals(expectedLog, logOutput.toString());
    }
  }

  // =============================================================================
  // Utility Test (appears to be testing StringBuilder behavior)
  // =============================================================================

  @Test
  void shouldReturnSameStringBuilderInstanceAfterModification() {
    // Given: A StringBuilder instance
    StringBuilder originalBuilder = new StringBuilder();
    
    // When: Modifying it through a method
    StringBuilder returnedBuilder = appendToStringBuilder(originalBuilder);
    
    // Then: Same instance should be returned
    assertSame(originalBuilder, returnedBuilder);
  }

  // =============================================================================
  // Helper Methods
  // =============================================================================

  private ScriptRunner createStandardScriptRunner(Connection connection) {
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setAutoCommit(true);
    runner.setStopOnError(false);
    runner.setErrorLogWriter(null);
    runner.setLogWriter(null);
    return runner;
  }

  private ScriptRunner createScriptRunnerWithFullScriptMode(Connection connection) {
    ScriptRunner runner = createStandardScriptRunner(connection);
    runner.setSendFullScript(true);
    return runner;
  }

  private ScriptRunner createStrictScriptRunner(Connection connection) {
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setAutoCommit(false);
    runner.setStopOnError(true);
    runner.setErrorLogWriter(null);
    runner.setLogWriter(null);
    return runner;
  }

  private ScriptRunner createScriptRunnerWithLogging(Connection connection, StringWriter logOutput, boolean fullScript) {
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setAutoCommit(true);
    runner.setStopOnError(false);
    runner.setErrorLogWriter(null);
    runner.setSendFullScript(fullScript);
    runner.setLogWriter(new PrintWriter(logOutput));
    return runner;
  }

  private DataSource createDataSourceFromProperties(Properties properties) {
    return new UnpooledDataSource(
        properties.getProperty("driver"), 
        properties.getProperty("url"),
        properties.getProperty("username"), 
        properties.getProperty("password")
    );
  }

  private void executeJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
    runScript(runner, JPETSTORE_DDL);
    runScript(runner, JPETSTORE_DATA);
  }

  private void assertJPetStoreDataIsLoaded() throws IOException, SQLException {
    PooledDataSource dataSource = createPooledDataSource(JPETSTORE_PROPERTIES);
    try (Connection connection = dataSource.getConnection()) {
      SqlRunner sqlRunner = new SqlRunner(connection);
      List<Map<String, Object>> products = sqlRunner.selectAll("SELECT * FROM PRODUCT");
      assertEquals(EXPECTED_PRODUCTS_IN_DB, products.size(), 
          "Expected " + EXPECTED_PRODUCTS_IN_DB + " products to be loaded in database");
    } finally {
      dataSource.forceCloseAll();
    }
  }

  private Exception assertScriptExecutionFails(ScriptRunner runner, Reader scriptReader) {
    try {
      runner.runScript(scriptReader);
      fail("Expected script runner to fail but it succeeded");
      return null; // Never reached
    } catch (Exception e) {
      return e;
    }
  }

  private String createScriptWithVariousDelimiters() {
    return """
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
  }

  private String createScriptWithMultiCharDelimiter() {
    return """
        -- @DELIMITER ||\s
        line 1;
        line 2;
        ||
        //  @DELIMITER  ;
        line 3;\s
        """;
  }

  private String buildExpectedLogOutput(String statement, String result) {
    return statement + LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t" + LINE_SEPARATOR + result + "\t" + LINE_SEPARATOR;
  }

  private StringBuilder appendToStringBuilder(StringBuilder sb) {
    sb.append("ABC");
    return sb;
  }
}
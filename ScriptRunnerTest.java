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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ScriptRunnerTest extends BaseDataTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Test
    @Disabled("This fails with HSQLDB 2.0 due to the create index statements in the schema script")
    void shouldRunScriptsBySendingFullScriptAtOnce() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection()) {
            ScriptRunner runner = createScriptRunner(conn);
            runner.setSendFullScript(true);
            runJPetStoreScripts(runner);
        }
        assertProductsTableExistsAndLoaded();
    }

    @Test
    void shouldRunScriptsUsingConnection() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection()) {
            ScriptRunner runner = createScriptRunner(conn);
            runJPetStoreScripts(runner);
        }
        assertProductsTableExistsAndLoaded();
    }

    @Test
    void shouldRunScriptsUsingProperties() throws Exception {
        Properties props = Resources.getResourceAsProperties(JPETSTORE_PROPERTIES);
        DataSource dataSource = new UnpooledDataSource(
            props.getProperty("driver"),
            props.getProperty("url"),
            props.getProperty("username"),
            props.getProperty("password"));
        try (Connection conn = dataSource.getConnection()) {
            ScriptRunner runner = createScriptRunner(conn);
            runJPetStoreScripts(runner);
        }
        assertProductsTableExistsAndLoaded();
    }

    @Test
    void shouldReturnWarningIfEndOfLineTerminatorNotFound() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        String resource = "org/apache/ibatis/jdbc/ScriptMissingEOLTerminator.sql";
        try (Connection conn = ds.getConnection();
             Reader reader = Resources.getResourceAsReader(resource)) {
            ScriptRunner runner = createScriptRunner(conn);
            
            Exception e = assertThrows(Exception.class, () -> runner.runScript(reader));
            assertTrue(e.getMessage().contains("end-of-line terminator"));
        }
    }

    @Test
    void commentAfterStatementDelimiterShouldNotCauseRunnerFail() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        String resource = "org/apache/ibatis/jdbc/ScriptCommentAfterEOLTerminator.sql";
        try (Connection conn = ds.getConnection();
             Reader reader = Resources.getResourceAsReader(resource)) {
            ScriptRunner runner = createScriptRunner(conn);
            runJPetStoreScripts(runner);
            // Should not throw exception when comment exists after delimiter
            runner.runScript(reader);
        }
    }

    @Test
    void shouldReturnWarningIfNotTheCurrentDelimiterUsed() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        String resource = "org/apache/ibatis/jdbc/ScriptChangingDelimiterMissingDelimiter.sql";
        try (Connection conn = ds.getConnection();
             Reader reader = Resources.getResourceAsReader(resource)) {
            ScriptRunner runner = createScriptRunner(conn);
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            
            Exception e = assertThrows(Exception.class, () -> runner.runScript(reader));
            assertTrue(e.getMessage().contains("end-of-line terminator"));
        }
    }

    @Test
    void changingDelimiterShouldNotCauseRunnerFail() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        String resource = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";
        try (Connection conn = ds.getConnection();
             Reader reader = Resources.getResourceAsReader(resource)) {
            ScriptRunner runner = createScriptRunner(conn);
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            runJPetStoreScripts(runner);
            // Should handle delimiter changes without errors
            runner.runScript(reader);
        }
    }

    @Test
    void logging() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection();
             StringWriter sw = new StringWriter();
             PrintWriter logWriter = new PrintWriter(sw)) {
            ScriptRunner runner = createScriptRunner(conn);
            runner.setSendFullScript(false);
            runner.setLogWriter(logWriter);

            try (Reader reader = new StringReader("select userid from account where userid = 'j2ee';")) {
                runner.runScript(reader);
            }

            String expected = "select userid from account where userid = 'j2ee'" + 
                LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t" + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR;
            assertEquals(expected, sw.toString());
        }
    }

    @Test
    void loggingFullScript() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection();
             StringWriter sw = new StringWriter();
             PrintWriter logWriter = new PrintWriter(sw)) {
            ScriptRunner runner = createScriptRunner(conn);
            runner.setSendFullScript(true);
            runner.setLogWriter(logWriter);

            try (Reader reader = new StringReader("select userid from account where userid = 'j2ee';")) {
                runner.runScript(reader);
            }

            String expected = "select userid from account where userid = 'j2ee';" + 
                LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t" + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR;
            assertEquals(expected, sw.toString());
        }
    }

    @Test
    void shouldAcceptDelimiterVariations() throws Exception {
        try (MockConnection conn = new MockConnection()) {
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
            runner.runScript(new StringReader(sql));

            // Verify expected commands were executed
            conn.verifyStatementExecuted("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
            conn.verifyStatementExecuted("line 3" + LINE_SEPARATOR);
            conn.verifyStatementExecuted("line 4" + LINE_SEPARATOR);
            conn.verifyStatementExecuted("line 5" + LINE_SEPARATOR);
        }
    }

    @Test
    void appendShouldReturnSameStringBuilder() {
        StringBuilder original = new StringBuilder();
        StringBuilder result = appendABC(original);
        assertSame(original, result);
    }

    @Test
    void shouldAcceptMultiCharDelimiter() throws Exception {
        try (MockConnection conn = new MockConnection()) {
            ScriptRunner runner = new ScriptRunner(conn);
            String sql = """
                -- @DELIMITER ||\s
                line 1;
                line 2;
                ||
                //  @DELIMITER  ;
                line 3;\s
                """;
            runner.runScript(new StringReader(sql));

            // Verify expected commands were executed
            conn.verifyStatementExecuted("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
            conn.verifyStatementExecuted("line 3" + LINE_SEPARATOR);
        }
    }

    private ScriptRunner createScriptRunner(Connection conn) {
        ScriptRunner runner = new ScriptRunner(conn);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setErrorLogWriter(null);
        runner.setLogWriter(null);
        return runner;
    }

    private void runJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
        runScript(runner, JPETSTORE_DDL);
        runScript(runner, JPETSTORE_DATA);
    }

    private void assertProductsTableExistsAndLoaded() throws IOException, SQLException {
        try (PooledDataSource ds = createPooledDataSource(JPETSTORE_PROPERTIES);
             Connection conn = ds.getConnection()) {
            SqlRunner executor = new SqlRunner(conn);
            List<Map<String, Object>> products = executor.selectAll("SELECT * FROM PRODUCT");
            assertEquals(16, products.size());
        }
    }

    private StringBuilder appendABC(StringBuilder sb) {
        sb.append("ABC");
        return sb;
    }

    /**
     * Helper class to mock JDBC connections and verify statement execution
     */
    private static class MockConnection implements Connection, AutoCloseable {
        private final Statement stmt = mock(Statement.class);
        
        public MockConnection() {
            when(stmt.getUpdateCount()).thenReturn(-1);
        }
        
        @Override
        public Statement createStatement() {
            return stmt;
        }
        
        public void verifyStatementExecuted(String expectedSql) throws SQLException {
            verify(stmt).execute(expectedSql);
        }
        
        // Implement other Connection methods as no-op
        @Override public void close() {}
        @Override public boolean isClosed() { return false; }
        @Override public void setAutoCommit(boolean autoCommit) {}
        @Override public boolean getAutoCommit() { return false; }
        @Override public void commit() {}
        // ... other Connection method implementations ...
    }
}
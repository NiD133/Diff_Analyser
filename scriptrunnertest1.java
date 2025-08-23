package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for ScriptRunner sending the entire script at once.
 */
class ScriptRunnerFullScriptTest extends BaseDataTest {

    /**
     * This test demonstrates the ScriptRunner's ability to execute an entire script
     * in one go, rather than statement by statement.
     * <p>
     * It is currently disabled because the underlying HSQLDB (v2.0) fails on
     * 'CREATE INDEX' statements within the script. The original test also contained
     * a bug where the connection was closed before the script was executed.
     */
    @Test
    @Disabled("Fails with HSQLDB 2.0 due to 'CREATE INDEX' statements in the schema script.")
    void shouldExecuteAllStatementsWhenSendingFullScript() throws Exception {
        // Arrange
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection connection = ds.getConnection()) {
            ScriptRunner runner = new ScriptRunner(connection);
            // Configure the runner to send the entire script in one execution
            runner.setSendFullScript(true);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setErrorLogWriter(null); // Suppress logs for clean test output
            runner.setLogWriter(null);

            // Act
            runJPetStoreScripts(runner);
        } // The try-with-resources block ensures the connection is closed automatically.

        // Assert
        assertProductsTableExistsAndLoaded();
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
}
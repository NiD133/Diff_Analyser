package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ScriptRunner} handling of SQL scripts that change the statement delimiter.
 */
class ScriptRunnerChangingDelimiterTest extends BaseDataTest {

    /**
     * Sets up the JPetStore database by running the DDL and data-loading scripts.
     */
    private void runJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
        runScript(runner, JPETSTORE_DDL);
        runScript(runner, JPETSTORE_DATA);
    }

    /**
     * Verifies that the database setup is correct by checking the number of rows in the PRODUCT table.
     */
    private void assertProductsTableExistsAndLoaded() throws IOException, SQLException {
        // Use a new data source and connection for verification to ensure the test's transaction was handled correctly.
        PooledDataSource dataSource = createPooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection connection = dataSource.getConnection()) {
            SqlRunner executor = new SqlRunner(connection);
            List<Map<String, Object>> products = executor.selectAll("SELECT * FROM PRODUCT");
            assertEquals(16, products.size(), "Expected 16 products in the PRODUCT table.");
        } finally {
            dataSource.forceCloseAll();
        }
    }

    /**
     * A script that changes its delimiter mid-stream should be executed correctly without failing.
     * This ensures the ScriptRunner can parse and handle delimiter change commands.
     */
    @Test
    void shouldRunScriptSuccessfullyWhenDelimiterIsChanged() throws Exception {
        // Arrange
        DataSource dataSource = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        String scriptWithChangingDelimiter = "org/apache/ibatis/jdbc/ScriptChangingDelimiter.sql";

        try (Connection connection = dataSource.getConnection();
             Reader reader = Resources.getResourceAsReader(scriptWithChangingDelimiter)) {

            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            // Suppress logs for clean test output
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);

            // Act
            // 1. Set up the initial database schema and data.
            runJPetStoreScripts(runner);
            // 2. Run the script that contains a delimiter change.
            runner.runScript(reader);
        }

        // Assert
        // Verify that the database is in the expected state after all scripts have run.
        // This confirms the script was executed correctly and didn't corrupt the data.
        assertProductsTableExistsAndLoaded();
    }
}
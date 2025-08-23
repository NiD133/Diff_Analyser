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
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the ScriptRunner class, focusing on its core script execution capabilities.
 * This class extends BaseDataTest, which provides helper methods for database setup and script loading.
 */
public class ScriptRunnerTest {

    /**
     * Helper method to execute the JPetStore schema creation (DDL) and data loading (DML) scripts.
     *
     * @param runner The ScriptRunner instance to use.
     */
    private void runJPetStoreScripts(ScriptRunner runner) throws IOException, SQLException {
        // The script file paths (e.g., JPETSTORE_DDL) are constants defined in the BaseDataTest superclass.
        runScript(runner, JPETSTORE_DDL);
        runScript(runner, JPETSTORE_DATA);
    }

    /**
     * Helper method to verify that the JPetStore database was correctly initialized.
     * It checks if the 'PRODUCT' table exists and contains the expected number of rows.
     */
    private void assertProductsTableExistsAndLoaded() throws IOException, SQLException {
        // Use a new data source and connection to ensure the state is read from the database
        // after the script runner has finished its work.
        PooledDataSource ds = createPooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection()) {
            SqlRunner executor = new SqlRunner(conn);
            List<Map<String, Object>> products = executor.selectAll("SELECT * FROM PRODUCT");
            assertEquals(16, products.size(), "The PRODUCT table should contain 16 rows after data script execution.");
        } finally {
            // Ensure all connections in the pool are closed to avoid resource leaks in tests.
            ds.forceCloseAll();
        }
    }

    /**
     * Verifies that the ScriptRunner can successfully execute a series of SQL scripts
     * (DDL and DML) using a provided database connection.
     */
    @Test
    void shouldExecuteScriptsSuccessfullyWithGivenConnection() throws Exception {
        // Arrange: Set up the database connection and configure the ScriptRunner.
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection()) {
            ScriptRunner runner = new ScriptRunner(conn);
            // Configure runner to continue on error and suppress log output for a clean test run.
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);

            // Act: Execute the DDL and data scripts.
            runJPetStoreScripts(runner);
        }

        // Assert: Verify that the database state reflects the script execution.
        assertProductsTableExistsAndLoaded();
    }
}
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

public class ScriptRunnerTestTest9 extends BaseDataTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

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

    private StringBuilder y(StringBuilder sb) {
        sb.append("ABC");
        return sb;
    }

    @Test
    void loggingFullScipt() throws Exception {
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        try (Connection conn = ds.getConnection()) {
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setErrorLogWriter(null);
            runner.setSendFullScript(true);
            StringWriter sw = new StringWriter();
            PrintWriter logWriter = new PrintWriter(sw);
            runner.setLogWriter(logWriter);
            Reader reader = new StringReader("select userid from account where userid = 'j2ee';");
            runner.runScript(reader);
            assertEquals("select userid from account where userid = 'j2ee';" + LINE_SEPARATOR + LINE_SEPARATOR + "USERID\t" + LINE_SEPARATOR + "j2ee\t" + LINE_SEPARATOR, sw.toString());
        }
    }
}

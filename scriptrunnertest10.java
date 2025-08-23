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

public class ScriptRunnerTestTest10 extends BaseDataTest {

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
    void shouldAcceptDelimiterVariations() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.getUpdateCount()).thenReturn(-1);
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
        Reader reader = new StringReader(sql);
        runner.runScript(reader);
        verify(stmt).execute("line 1;" + LINE_SEPARATOR + "line 2;" + LINE_SEPARATOR + LINE_SEPARATOR);
        verify(stmt).execute("line 3" + LINE_SEPARATOR);
        verify(stmt).execute("line 4" + LINE_SEPARATOR);
        verify(stmt).execute("line 5" + LINE_SEPARATOR);
    }
}

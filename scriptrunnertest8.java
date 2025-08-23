package org.apache.ibatis.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.ibatis.BaseDataTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for the logging capabilities of the ScriptRunner.
 */
public class ScriptRunnerTestTest8 extends BaseDataTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * This test verifies that when a log writer is configured, the ScriptRunner
     * correctly logs the executed SQL statement and its results.
     */
    @Test
    void shouldLogExecutedStatementAndItsResult() throws Exception {
        // Arrange
        DataSource ds = createUnpooledDataSource(JPETSTORE_PROPERTIES);
        StringWriter logOutput = new StringWriter();
        String sqlScript = "select userid from account where userid = 'j2ee';";

        try (Connection conn = ds.getConnection()) {
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(new PrintWriter(logOutput));
            // Configure runner to execute single statements and show results
            runner.setSendFullScript(false);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setErrorLogWriter(null); // Disable error log for this test

            // Act
            try (Reader scriptReader = new StringReader(sqlScript)) {
                runner.runScript(scriptReader);
            }

            // Assert
            // The expected output format is:
            // <SQL command>
            //
            // <Column Header>\t
            // <Row Value>\t
            //
            String expectedLog = """
                select userid from account where userid = 'j2ee'

                USERID\t
                j2ee\t
                """.replace("\n", LINE_SEPARATOR);

            assertEquals(expectedLog, logOutput.toString());
        }
    }
}
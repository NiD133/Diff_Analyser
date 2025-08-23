package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link ScriptRunner}.
 */
public class ScriptRunnerTest {

    /**
     * Tests that ScriptRunner correctly processes a statement that yields multiple update counts.
     * <p>
     * Some JDBC drivers can return multiple update counts from a single execution for batched
     * or complex statements. This test simulates that scenario and ensures ScriptRunner
     * iterates through all results correctly without throwing an error.
     */
    @Test
    public void shouldCorrectlyProcessMultipleUpdateCountsFromSingleStatement() throws Exception {
        // Arrange
        // 1. Mock the JDBC Statement to simulate a driver returning multiple update counts.
        Statement mockStatement = mock(Statement.class);

        // A call to execute() returns false, indicating an update count or no results.
        when(mockStatement.execute(anyString())).thenReturn(false);

        // The driver reports more results are available (first call), then no more (second call).
        when(mockStatement.getMoreResults()).thenReturn(false, false);

        // The driver returns two update counts (e.g., 705 and 150 rows affected),
        // followed by -1 to signal the end of the results.
        when(mockStatement.getUpdateCount()).thenReturn(705, 150, -1);

        // 2. Mock the JDBC Connection to return our mocked Statement.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.getAutoCommit()).thenReturn(true);

        // 3. Instantiate the ScriptRunner with the mocked connection.
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        // Suppress console output to keep test logs clean.
        scriptRunner.setLogWriter(null);
        scriptRunner.setErrorLogWriter(null);

        // 4. Define a simple SQL script. The content is not critical, only that it's a single statement.
        Reader scriptReader = new StringReader("UPDATE some_table SET a_column = 'a_value';");

        // Act
        scriptRunner.runScript(scriptReader);

        // Assert
        // Verify that the ScriptRunner's internal loop correctly interacted with the statement
        // to process all the update counts.
        verify(mockStatement).execute("UPDATE some_table SET a_column = 'a_value'");
        verify(mockStatement, times(2)).getMoreResults();
        verify(mockStatement, times(3)).getUpdateCount();
        verify(mockStatement).close(); // Ensure the statement is always closed.
    }
}
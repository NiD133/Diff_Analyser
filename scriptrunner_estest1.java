package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScriptRunner_ESTestTest1 extends ScriptRunner_ESTest_scaffolding {

    /**
     * Tests that runScript correctly handles a scenario that could cause an infinite loop.
     * <p>
     * This situation can occur with a misbehaving JDBC driver's Statement that
     * continuously returns a non-negative update count (e.g., 0) from getUpdateCount()
     * and 'false' from getMoreResults(). This combination causes an infinite loop in
     * ScriptRunner's result processing logic.
     * <p>
     * The test expects a RuntimeException, which is thrown by ScriptRunner when it
     * fails to execute the script. In the context of the original test generation tool (EvoSuite),
     * this exception was caused by a resource limit being exceeded, which successfully stopped the loop.
     */
    @Test(timeout = 4000)
    public void shouldThrowRuntimeExceptionWhenStatementCausesInfiniteLoopInResultProcessing() throws Throwable {
        // Arrange: Set up mocks to simulate a JDBC driver causing an infinite loop.
        // The loop condition in ScriptRunner's printResults method is effectively:
        // while (statement.getUpdateCount() != -1 || statement.getMoreResults()) { ... }
        // By always returning 0 for getUpdateCount(), the condition is always true.

        // Mocks for ResultSet processing, needed because statement.execute() returns true.
        ResultSetMetaData mockResultSetMetaData = mock(ResultSetMetaData.class);
        when(mockResultSetMetaData.getColumnCount()).thenReturn(0); // Use a simple, valid value.

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        when(mockResultSet.next()).thenReturn(false); // Simulate no rows in the result set.

        // Mock a Statement that triggers the infinite loop.
        Statement mockStatement = mock(Statement.class);
        when(mockStatement.execute(anyString())).thenReturn(true); // Indicates a result set is available.
        when(mockStatement.getResultSet()).thenReturn(mockResultSet);

        // These two mock configurations are the key to causing the infinite loop.
        when(mockStatement.getUpdateCount()).thenReturn(0); // Always return a non-negative value.
        when(mockStatement.getMoreResults()).thenReturn(false); // And never report more results.

        // Mock the Connection to return our misbehaving Statement.
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.getAutoCommit()).thenReturn(false);

        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);
        scriptRunner.setSendFullScript(true); // Ensure the execution path that processes results is taken.

        // An empty reader is sufficient to trigger the execution.
        Reader emptyScriptReader = Reader.nullReader();

        // Act & Assert
        try {
            scriptRunner.runScript(emptyScriptReader);
            fail("Expected a RuntimeException due to the infinite loop in result processing.");
        } catch (RuntimeException e) {
            // Verify that the expected exception is thrown from the ScriptRunner.
            // The underlying cause is the infinite loop, which is interrupted by the test timeout
            // or, in the original test's case, a test generation tool's resource monitor.
            assertTrue("Exception message should indicate an error during script execution.",
                e.getMessage().startsWith("Error executing: ."));
            assertNotNull("The RuntimeException should wrap the underlying cause.", e.getCause());
        }
    }
}
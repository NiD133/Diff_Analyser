package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test suite for the ScriptRunner class.
 */
public class ScriptRunnerTest {

    /**
     * Verifies that the deprecated closeConnection() method correctly calls close()
     * on the underlying SQL Connection. This ensures backward compatibility.
     */
    @Test
    public void shouldCloseConnectionWhenCloseConnectionIsCalled() throws SQLException {
        // Arrange: Create a mock Connection and a ScriptRunner instance.
        Connection mockConnection = mock(Connection.class);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // Act: Call the method under test.
        scriptRunner.closeConnection();

        // Assert: Verify that the close() method of the mock Connection was called exactly once.
        verify(mockConnection).close();
    }
}
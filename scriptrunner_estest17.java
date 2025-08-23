package org.apache.ibatis.jdbc;

import org.junit.Test;

import java.sql.Connection;

import static org.mockito.Mockito.mock;

/**
 * Test suite for the ScriptRunner class, focusing on its configuration setters.
 */
public class ScriptRunnerTest {

    /**
     * Verifies that the setEscapeProcessing method can be called without error.
     * <p>
     * This test ensures the setter for the 'escapeProcessing' flag functions correctly.
     * As the ScriptRunner class does not provide a corresponding getter for this property,
     * the test's success is defined by the absence of any exceptions during the method call.
     */
    @Test
    public void shouldSetEscapeProcessingFlag() {
        // Arrange: Create a ScriptRunner instance with a mock Connection.
        // Using a mock is better practice than passing null, even if the connection is not used by this setter.
        Connection mockConnection = mock(Connection.class);
        ScriptRunner scriptRunner = new ScriptRunner(mockConnection);

        // Act: Call the setter for the escapeProcessing property.
        scriptRunner.setEscapeProcessing(true);

        // Assert: The test passes if the 'Act' phase completes without throwing an exception.
        // No explicit assertion is needed here.
    }
}
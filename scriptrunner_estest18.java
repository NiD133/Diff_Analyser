package org.apache.ibatis.jdbc;

import org.junit.Test;
import java.sql.Connection;

/**
 * Test suite for the {@link ScriptRunner} class.
 * 
 * Note: The original test class name "ScriptRunner_ESTestTest18" suggests it was
 * auto-generated. A more conventional name would be ScriptRunnerTest.
 */
public class ScriptRunner_ESTestTest18 {

    /**
     * Verifies that calling closeConnection() on a ScriptRunner initialized
     * with a null connection does not throw a NullPointerException.
     * <p>
     * The method should handle the null connection gracefully.
     */
    @Test
    public void closeConnectionShouldNotThrowExceptionWhenConnectionIsNull() {
        // Arrange: Create a ScriptRunner with a null database connection.
        ScriptRunner scriptRunner = new ScriptRunner((Connection) null);

        // Act: Attempt to close the (null) connection.
        // The test's success is confirmed by the absence of an exception.
        scriptRunner.closeConnection();

        // Assert: No exception is thrown (implicit assertion).
    }
}
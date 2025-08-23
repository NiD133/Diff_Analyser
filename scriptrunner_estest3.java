package org.apache.ibatis.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Reader;
import java.sql.Connection;
import org.junit.Test;

/**
 * Contains tests for the {@link ScriptRunner} class.
 * This class focuses on improving the understandability of an auto-generated test.
 */
public class ScriptRunnerTest {

    /**
     * Verifies that runScript() throws a RuntimeException when the ScriptRunner
     * is initialized with a null Connection.
     * <p>
     * The method is expected to fail early because it first tries to configure
     * the connection's auto-commit property, which will result in a
     * NullPointerException.
     */
    @Test
    public void runScriptShouldThrowRuntimeExceptionWhenConnectionIsNull() {
        // Arrange: Create a ScriptRunner with a null database connection.
        // The reader can be any valid reader; its content is irrelevant
        // as the exception occurs before the script is read.
        ScriptRunner scriptRunner = new ScriptRunner((Connection) null);
        Reader emptyReader = Reader.nullReader();

        // Act & Assert
        try {
            scriptRunner.runScript(emptyReader);
            fail("Expected a RuntimeException to be thrown due to the null connection.");
        } catch (RuntimeException e) {
            // Verify that the exception is the one we expect.
            // The ScriptRunner should wrap the underlying NullPointerException.
            final String expectedMessage = "Could not set AutoCommit to false. Cause: java.lang.NullPointerException";
            assertEquals(expectedMessage, e.getMessage());

            // Verify the cause of the exception is indeed a NullPointerException.
            Throwable cause = e.getCause();
            assertNotNull("Exception cause should not be null", cause);
            assertTrue("Exception cause should be a NullPointerException", cause instanceof NullPointerException);
        }
    }
}
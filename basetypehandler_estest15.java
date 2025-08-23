package org.apache.ibatis.type;

import org.apache.ibatis.type.ClobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Tests for the BaseTypeHandler class, focusing on its exception handling capabilities.
 */
public class BaseTypeHandlerTest {

    /**
     * Verifies that setParameter wraps any underlying exception in a TypeException.
     *
     * This test ensures that when a non-null parameter is provided but the PreparedStatement is null,
     * the call to the underlying setNonNullParameter method will fail with a NullPointerException.
     * The BaseTypeHandler should catch this and wrap it in a more informative TypeException,
     * providing context about the failed operation.
     */
    @Test
    public void shouldThrowTypeExceptionWhenPreparedStatementIsNullOnSetParameter() {
        // Arrange
        // Use a concrete implementation to test the abstract BaseTypeHandler
        BaseTypeHandler<String> handler = new ClobTypeHandler();
        int parameterIndex = 1;
        String parameterValue = "test-value";
        JdbcType jdbcType = JdbcType.CLOB;

        // Act & Assert
        try {
            // The PreparedStatement is intentionally null to trigger an internal NullPointerException
            handler.setParameter(null, parameterIndex, parameterValue, jdbcType);
            fail("Should have thrown a TypeException because the PreparedStatement is null.");
        } catch (TypeException e) {
            // Verify that the correct, informative exception was thrown
            assertEquals("The cause of the TypeException should be a NullPointerException.",
                NullPointerException.class, e.getCause().getClass());

            String expectedMessage = "Error setting non null for parameter #1 with JdbcType CLOB";
            assertTrue("Exception message should contain details about the error.",
                e.getMessage().contains(expectedMessage));
        } catch (SQLException e) {
            // This catch block is for safety, but a TypeException is expected.
            fail("A SQLException was thrown, but a TypeException was expected. Details: " + e.getMessage());
        }
    }
}
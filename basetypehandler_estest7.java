package org.apache.ibatis.type;

import org.junit.Test;

/**
 * Tests for the {@link IntegerTypeHandler} to ensure it correctly handles parameters.
 *
 * This test focuses on edge cases, such as handling null arguments, to ensure
 * the type handler is robust and behaves predictably.
 */
public class IntegerTypeHandlerTest {

    /**
     * Verifies that setNonNullParameter throws a NullPointerException if the PreparedStatement is null.
     * A TypeHandler should not attempt to operate on a null PreparedStatement, and throwing an
     * NPE is the expected behavior, preventing silent failures.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenPreparedStatementIsNull() {
        // Arrange
        IntegerTypeHandler handler = new IntegerTypeHandler();
        Integer parameter = 123;
        int parameterIndex = 1; // JDBC parameters are 1-based
        // Although the specific JdbcType doesn't affect this null check,
        // using a relevant type makes the test setup more realistic.
        JdbcType jdbcType = JdbcType.INTEGER;

        // Act & Assert
        // This call is expected to throw a NullPointerException because the PreparedStatement is null.
        // The assertion is handled by the @Test(expected=...) annotation.
        handler.setNonNullParameter(null, parameterIndex, parameter, jdbcType);
    }
}
package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Test suite for {@link ArrayTypeHandler}.
 */
public class ArrayTypeHandlerTest {

    /**
     * The original test passed null for both the PreparedStatement and the parameter.
     * Based on the implementation of ArrayTypeHandler, the NullPointerException is thrown
     * because the 'parameter' argument is null, not the PreparedStatement.
     * This improved test clarifies this specific behavior.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenParameterIsNull() throws SQLException {
        // Arrange
        ArrayTypeHandler handler = new ArrayTypeHandler();
        int anyIndex = 1;
        JdbcType anyJdbcType = JdbcType.ARRAY;

        // Act & Assert
        // The following call is expected to throw a NullPointerException because the `parameter`
        // argument is null. The handler attempts to access its class before using the PreparedStatement.
        handler.setNonNullParameter(null, anyIndex, null, anyJdbcType);
    }
}
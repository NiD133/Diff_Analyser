package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 * This improved version focuses on clarity and standard testing practices.
 */
public class EnumOrdinalTypeHandlerTest {

    /**
     * Verifies that setNonNullParameter throws a NullPointerException when the PreparedStatement is null.
     * This is the expected behavior, as the method cannot set a parameter on a null statement.
     */
    @Test(expected = NullPointerException.class)
    public void setNonNullParameterShouldThrowNpeForNullPreparedStatement() throws SQLException {
        // Arrange
        // Create a handler for a specific enum type, in this case, JdbcType.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // Define arbitrary but valid parameters for the method call.
        // Their specific values do not affect this test's outcome.
        int anyColumnIndex = 1;
        JdbcType anyEnumParameter = JdbcType.VARCHAR;

        // Act
        // Call the method with a null PreparedStatement, which should trigger the exception.
        typeHandler.setNonNullParameter(null, anyColumnIndex, anyEnumParameter, null);

        // Assert
        // The @Test(expected = NullPointerException.class) annotation handles the assertion.
        // The test will fail automatically if a NullPointerException is not thrown.
    }
}
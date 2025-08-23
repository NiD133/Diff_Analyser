package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test focuses on the behavior of EnumOrdinalTypeHandler when handling
 * invalid data from a ResultSet.
 */
public class EnumOrdinalTypeHandlerTest { // Renamed for clarity

    /**
     * Verifies that getNullableResult throws an IllegalArgumentException when the
     * ResultSet provides an ordinal value that is out of bounds for the target enum.
     * An enum's ordinal is a zero-based index, so a negative value like -1 is always invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForInvalidOrdinal() throws SQLException {
        // Arrange
        // 1. Create a handler for a specific enum type (JdbcType).
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // 2. Mock a ResultSet to simulate a database result.
        ResultSet mockResultSet = mock(ResultSet.class);

        // 3. Configure the mock to return an invalid ordinal (-1) for any column.
        when(mockResultSet.getInt(anyString())).thenReturn(-1);

        // 4. Ensure the database value is not treated as SQL NULL.
        // This forces the handler to attempt conversion instead of returning null.
        when(mockResultSet.wasNull()).thenReturn(false);

        // Act
        // Attempt to retrieve the enum value. This should trigger the exception because
        // the ordinal -1 does not correspond to any enum constant.
        handler.getNullableResult(mockResultSet, "anyColumnName");

        // Assert
        // The test passes if an IllegalArgumentException is thrown, as declared by the
        // @Test(expected = ...) annotation.
    }
}
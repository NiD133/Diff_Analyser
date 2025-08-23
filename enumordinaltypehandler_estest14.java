package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 */
public class EnumOrdinalTypeHandlerTest {

    /**
     * This test verifies that the handler can correctly retrieve an enum constant
     * from a ResultSet based on its ordinal value (an integer).
     */
    @Test
    public void shouldReturnEnumConstantForGivenOrdinalFromResultSet() throws SQLException {
        // Arrange
        // JdbcType.ARRAY is the first constant in the enum, so its ordinal is 0.
        int arrayOrdinal = 0;
        JdbcType expectedEnum = JdbcType.ARRAY;
        String columnName = "enum_column";

        // Mock the ResultSet to simulate database behavior
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(columnName)).thenReturn(arrayOrdinal);
        when(mockResultSet.wasNull()).thenReturn(false);

        // Create the handler for the JdbcType enum
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // Act
        JdbcType actualEnum = typeHandler.getNullableResult(mockResultSet, columnName);

        // Assert
        assertEquals(expectedEnum, actualEnum);
    }
}
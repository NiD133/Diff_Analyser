package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 */
// The original class name is kept for context, but in a real-world scenario,
// it would be renamed to something like "EnumOrdinalTypeHandlerTest".
public class EnumOrdinalTypeHandler_ESTestTest10 {

    /**
     * Verifies that the handler correctly retrieves an enum constant by its ordinal value
     * when reading a non-null integer from a ResultSet by column index.
     */
    @Test
    public void shouldReturnEnumConstantForGivenOrdinalFromResultSetByIndex() throws SQLException {
        // Arrange
        // 1. Define the expected enum constant. JdbcType.ARRAY has an ordinal value of 0.
        JdbcType expectedEnum = JdbcType.ARRAY;
        int columnIndex = 1;

        // 2. Mock the ResultSet to simulate database behavior.
        //    When getInt() is called for our column, it should return the enum's ordinal.
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(columnIndex)).thenReturn(expectedEnum.ordinal());
        when(mockResultSet.wasNull()).thenReturn(false); // Simulate a non-null value.

        // 3. Create an instance of the type handler for the JdbcType enum.
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // Act
        // Call the method under test to get the result from the mocked ResultSet.
        JdbcType actualEnum = handler.getNullableResult(mockResultSet, columnIndex);

        // Assert
        // Verify that the handler returned the correct enum constant.
        assertEquals("The handler should map the ordinal back to the correct enum constant.", expectedEnum, actualEnum);
    }
}
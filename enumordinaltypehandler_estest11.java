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
public class EnumOrdinalTypeHandlerTest {

    @Test
    public void shouldReturnEnumConstantWhenOrdinalIsFoundAndNotNull() throws SQLException {
        // Arrange
        // 1. Create the type handler for the JdbcType enum.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // 2. Mock the ResultSet to simulate a database response.
        ResultSet mockResultSet = mock(ResultSet.class);
        int columnIndex = 1;
        int ordinalFromDatabase = 0; // The ordinal for JdbcType.ARRAY is 0.
        
        // 3. Configure the mock: when getInt() is called, return the ordinal,
        //    and confirm the value was not SQL NULL.
        when(mockResultSet.getInt(columnIndex)).thenReturn(ordinalFromDatabase);
        when(mockResultSet.wasNull()).thenReturn(false);

        // Act
        // Call the method under test to get the enum constant.
        JdbcType result = typeHandler.getNullableResult(mockResultSet, columnIndex);

        // Assert
        // Verify that the handler correctly mapped the ordinal (0) to the expected enum (JdbcType.ARRAY).
        assertEquals(JdbcType.ARRAY, result);
    }
}
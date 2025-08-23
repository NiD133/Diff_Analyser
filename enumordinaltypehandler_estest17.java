package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EnumOrdinalTypeHandler}.
 */
public class EnumOrdinalTypeHandlerTest {

    /**
     * Verifies that getNullableResult correctly converts an integer ordinal from a
     * ResultSet into the corresponding enum constant when accessed by column name.
     */
    @Test
    public void shouldReturnEnumConstantForGivenOrdinalFromResultSetByName() throws SQLException {
        // Arrange
        // 1. Define the target enum and its expected ordinal value.
        //    In the JdbcType enum, BIT has an ordinal value of 1.
        final JdbcType expectedEnum = JdbcType.BIT;
        final int enumOrdinal = expectedEnum.ordinal();
        final String columnName = "ENUM_COLUMN";

        // 2. Create a mock ResultSet that returns the ordinal when getInt is called.
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(columnName)).thenReturn(enumOrdinal);

        // 3. Instantiate the handler for the JdbcType enum.
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // Act
        // Call the method under test to convert the ordinal from the ResultSet to an enum.
        JdbcType actualEnum = handler.getNullableResult(mockResultSet, columnName);

        // Assert
        // Verify that the handler returned the correct enum constant.
        assertEquals(expectedEnum, actualEnum);
    }
}
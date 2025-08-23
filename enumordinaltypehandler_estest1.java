package org.apache.ibatis.type;

import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 */
class EnumOrdinalTypeHandlerTest {

    /**
     * This test verifies that getNullableResult throws an IllegalArgumentException
     * when the database returns an integer that does not correspond to a valid
     * enum ordinal (e.g., a negative number or an out-of-bounds index).
     */
    @Test
    void getNullableResultShouldThrowExceptionWhenResultSetReturnsInvalidOrdinal() throws SQLException {
        // Arrange
        // 1. Create the handler for a specific enum type, JdbcType.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // 2. Mock a ResultSet to simulate a database query result.
        ResultSet mockResultSet = mock(ResultSet.class);

        // 3. Configure the mock to return an invalid ordinal value.
        //    Enum ordinals are 0-based array indices, so any negative number is invalid.
        final int invalidOrdinal = -1824;
        when(mockResultSet.getInt(anyInt())).thenReturn(invalidOrdinal);

        // 4. The handler first checks if the value was SQL NULL. We must mock this
        //    to return false to proceed to the ordinal conversion logic.
        when(mockResultSet.wasNull()).thenReturn(false);


        // Act & Assert
        // 5. Execute the method and assert that it throws the expected exception.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            // The column index (e.g., 1) is arbitrary because getInt() is mocked with anyInt().
            typeHandler.getNullableResult(mockResultSet, 1);
        });

        // 6. Verify that the exception message is clear and informative.
        String expectedMessage = "Cannot convert " + invalidOrdinal + " to " + JdbcType.class.getSimpleName() + " by ordinal value.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
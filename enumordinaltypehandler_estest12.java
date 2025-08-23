package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 */
// The class name is improved to follow standard conventions.
// Original: EnumOrdinalTypeHandler_ESTestTest12
public class EnumOrdinalTypeHandlerTest {

    /**
     * Verifies that getNullableResult throws an IllegalArgumentException
     * when the integer value from the database does not correspond to a valid enum ordinal.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionForInvalidOrdinal() throws SQLException {
        // Arrange
        // 1. Define an ordinal that is guaranteed to be out of bounds for the JdbcType enum.
        // This is more robust than using a "magic number" like 538.
        int invalidOrdinal = JdbcType.values().length;
        int anyColumnIndex = 1;

        // 2. Mock a ResultSet to return this invalid ordinal.
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(anyColumnIndex)).thenReturn(invalidOrdinal);
        when(mockResultSet.wasNull()).thenReturn(false); // Ensure we don't get a null result

        // 3. Create the handler instance for the JdbcType enum.
        EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);

        // Act & Assert
        try {
            typeHandler.getNullableResult(mockResultSet, anyColumnIndex);
            fail("Should have thrown an IllegalArgumentException for an out-of-bounds ordinal.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is informative and correct.
            String expectedMessage = "Cannot convert " + invalidOrdinal + " to JdbcType by ordinal value.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 * Note: The original test class name 'EnumOrdinalTypeHandler_ESTestTest4' and its scaffolding
 * are preserved as per the request. In a typical project, this class would be named 'EnumOrdinalTypeHandlerTest'.
 */
public class EnumOrdinalTypeHandler_ESTestTest4 extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    /**
     * Verifies that getNullableResult throws an IllegalArgumentException when the database
     * returns an ordinal value that is out of bounds for the target enum.
     */
    @Test
    public void shouldThrowExceptionWhenGettingResultWithInvalidOrdinal() throws SQLException {
        // Arrange
        final int invalidOrdinal = -874;
        final Class<JdbcType> enumClass = JdbcType.class;
        final EnumOrdinalTypeHandler<JdbcType> typeHandler = new EnumOrdinalTypeHandler<>(enumClass);

        ResultSet mockResultSet = mock(ResultSet.class);
        // Configure the mock to simulate the database returning an ordinal that does not exist.
        when(mockResultSet.getInt(anyString())).thenReturn(invalidOrdinal);

        // Act & Assert
        try {
            typeHandler.getNullableResult(mockResultSet, "anyColumnName");
            fail("Should have thrown an IllegalArgumentException for an invalid ordinal.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is clear and informative.
            String expectedMessage = "Cannot convert " + invalidOrdinal + " to " + enumClass.getSimpleName() + " by ordinal value.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
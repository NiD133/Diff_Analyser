package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 */
// The class name now clearly indicates which class is under test.
public class EnumOrdinalTypeHandlerTest {

    /**
     * Verifies that getNullableResult throws an IllegalArgumentException when the database
     * returns an ordinal value that is not a valid for the target enum.
     * Enum ordinals are always non-negative, so any negative number is invalid.
     */
    // The test method name describes the behavior being tested (the "what" and "why").
    @Test
    public void shouldThrowExceptionWhenCallableStatementReturnsInvalidOrdinal() throws SQLException {
        // Arrange: Set up the test objects and mock behavior.

        // An invalid ordinal for any enum (ordinals are always >= 0).
        // Using a named constant makes the code self-documenting.
        final int INVALID_ORDINAL = -983;
        final Class<JdbcType> enumType = JdbcType.class;

        // The handler under test, configured for the JdbcType enum.
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(enumType);

        // Mock a CallableStatement to simulate a database result.
        CallableStatement mockCallableStatement = mock(CallableStatement.class);
        // Configure the mock to return an invalid ordinal when getInt() is called.
        when(mockCallableStatement.getInt(anyInt())).thenReturn(INVALID_ORDINAL);

        // Act & Assert: Execute the method and verify the outcome.
        try {
            // The column index (1) is arbitrary as our mock will return the same value for any index.
            handler.getNullableResult(mockCallableStatement, 1);
            fail("Should have thrown an IllegalArgumentException for an invalid ordinal.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct and informative.
            String expectedMessage = "Cannot convert " + INVALID_ORDINAL + " to " + enumType.getSimpleName() + " by ordinal value.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
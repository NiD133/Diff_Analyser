package org.apache.ibatis.type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Test suite for {@link EnumOrdinalTypeHandler}.
 * This class focuses on verifying the handler's behavior when retrieving enum values.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnumOrdinalTypeHandlerTest {

    @Mock
    private CallableStatement mockCallableStatement;

    private EnumOrdinalTypeHandler<JdbcType> typeHandler;

    @Before
    public void setUp() {
        // Initialize the handler for a specific enum (JdbcType) before each test.
        typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
    }

    /**
     * Verifies that getNullableResult returns null when the underlying CallableStatement
     * indicates the retrieved value was a SQL NULL.
     */
    @Test
    public void shouldReturnNullWhenCallableStatementValueWasNull() throws SQLException {
        // Arrange: Configure the mock CallableStatement to simulate a NULL value.
        // According to the JDBC specification, when a numeric value is NULL, getInt()
        // may return 0, and wasNull() must be checked, which will return true.
        int anyColumnIndex = 1;
        when(mockCallableStatement.getInt(anyColumnIndex)).thenReturn(0);
        when(mockCallableStatement.wasNull()).thenReturn(true);

        // Act: Call the method under test to retrieve the result.
        JdbcType result = typeHandler.getNullableResult(mockCallableStatement, anyColumnIndex);

        // Assert: The result should be null because wasNull() returned true,
        // indicating a SQL NULL value.
        assertNull("The type handler should return null for a SQL NULL value.", result);
    }
}
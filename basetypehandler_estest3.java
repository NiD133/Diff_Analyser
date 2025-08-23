package org.apache.ibatis.type;

import org.junit.Test;
import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for BaseTypeHandler.
 * This class tests the generic behavior of BaseTypeHandler using a concrete implementation.
 */
public class BaseTypeHandlerTest {

    /**
     * Verifies that getNullableResult returns null when the underlying CallableStatement
     * returns a null object, correctly handling database NULL values.
     */
    @Test
    public void getNullableResultShouldReturnNullWhenCallableStatementReturnsNull() throws SQLException {
        // Arrange
        // Use ObjectTypeHandler as a concrete implementation to test the abstract BaseTypeHandler
        ObjectTypeHandler handler = new ObjectTypeHandler();
        CallableStatement mockStatement = mock(CallableStatement.class);
        int anyColumnIndex = 1;

        // Configure the mock to return null, simulating a NULL value from a stored procedure
        when(mockStatement.getObject(anyInt())).thenReturn(null);

        // Act
        Object result = handler.getNullableResult(mockStatement, anyColumnIndex);

        // Assert
        assertNull("The result should be null when the database value is null.", result);
    }
}
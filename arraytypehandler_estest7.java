package org.apache.ibatis.type;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.junit.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 */
public class ArrayTypeHandlerTest {

    @Test
    public void shouldReturnNullForNullSqlArrayFromCallableStatement() throws SQLException {
        // Arrange
        ArrayTypeHandler typeHandler = new ArrayTypeHandler();
        CallableStatement mockedStatement = mock(CallableStatement.class);

        // Configure the mock to simulate the JDBC driver returning a null SQL Array.
        // This represents a NULL value in the database column.
        when(mockedStatement.getArray(anyInt())).thenReturn(null);

        // Act
        // Call the method under test with an arbitrary column index (e.g., 1).
        // The specific index doesn't matter due to the anyInt() matcher.
        Object result = typeHandler.getNullableResult(mockedStatement, 1);

        // Assert
        // The handler should correctly return null when the database value is null.
        assertNull("The result should be null when the database array is null.", result);
    }
}
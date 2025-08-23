package org.apache.ibatis.type;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.YearMonth;
import org.junit.Test;

/**
 * Unit tests for the BaseTypeHandler class, focusing on its interaction with JDBC.
 * A concrete implementation (YearMonthTypeHandler) is used to test the base class behavior.
 */
public class BaseTypeHandlerTest {

    private final YearMonthTypeHandler typeHandler = new YearMonthTypeHandler();

    /**
     * Verifies that getResult(CallableStatement, int) correctly returns null
     * when the underlying database value is SQL NULL.
     */
    @Test
    public void shouldReturnNullWhenGettingResultFromCallableStatementAndValueIsNull() throws SQLException {
        // Arrange: Create a mock CallableStatement that simulates retrieving a NULL value.
        CallableStatement mockCallableStatement = mock(CallableStatement.class);
        when(mockCallableStatement.getString(anyInt())).thenReturn(null);

        // Act: Call the method under test.
        YearMonth result = typeHandler.getResult(mockCallableStatement, 1);

        // Assert: Verify that the result is null, as expected.
        assertNull("The type handler should return a null object for a SQL NULL value.", result);
    }
}
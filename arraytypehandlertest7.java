package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Tests for {@link ArrayTypeHandler} focusing on result retrieval.
 * This class was renamed from ArrayTypeHandlerTestTest7 for clarity.
 */
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> ARRAY_TYPE_HANDLER = new ArrayTypeHandler();

    @Mock
    private Array mockSqlArray;

    /**
     * This test verifies that the handler correctly:
     * 1. Extracts a Java array from a SQL ARRAY retrieved from a ResultSet by column index.
     * 2. Calls the `free()` method on the SQL ARRAY to release its resources.
     */
    @Override
    @Test
    void shouldGetArrayFromResultSetByIndexAndFreeResource() throws SQLException {
        // Arrange
        String[] expectedArray = { "a", "b" };
        when(rs.getArray(1)).thenReturn(mockSqlArray);
        when(mockSqlArray.getArray()).thenReturn(expectedArray);

        // Act
        Object actualResult = ARRAY_TYPE_HANDLER.getResult(rs, 1);

        // Assert
        // Verify the returned object is the expected Java array
        assertArrayEquals(expectedArray, (Object[]) actualResult, "The handler should return the correct Java array.");

        // Verify that the handler correctly releases the SQL Array resource after use
        verify(mockSqlArray).free();
    }
}
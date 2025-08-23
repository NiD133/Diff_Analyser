package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Test class for {@link ArrayTypeHandler}.
 * This class focuses on how the handler retrieves results from a ResultSet.
 */
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();
    private static final String COLUMN_NAME = "column";

    @Mock
    private Array mockSqlArray;

    @Override
    @Test
    void getResultFromResultSetByName_shouldReturnArrayAndFreeSqlArrayResource() throws Exception {
        // Arrange
        String[] expectedArray = { "a", "b" };
        when(rs.getArray(COLUMN_NAME)).thenReturn(mockSqlArray);
        when(mockSqlArray.getArray()).thenReturn(expectedArray);

        // Act
        Object actualArray = TYPE_HANDLER.getResult(rs, COLUMN_NAME);

        // Assert
        // 1. The handler should return the correct array content.
        assertEquals(expectedArray, actualArray);

        // 2. The handler must free the SQL array to prevent resource leaks.
        verify(mockSqlArray).free();
    }
}
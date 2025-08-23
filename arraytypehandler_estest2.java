package org.apache.ibatis.type;

import org.junit.Test;
import java.sql.Array;
import java.sql.SQLException;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ArrayTypeHandler}.
 */
public class ArrayTypeHandlerTest {

    private final ArrayTypeHandler typeHandler = new ArrayTypeHandler();

    @Test
    public void shouldReturnUnderlyingArrayFromSqlArray() throws SQLException {
        // Arrange
        // The method under test should simply return the object from the SQL Array,
        // regardless of its type. A simple String array is used for clarity.
        Object expectedArray = new String[]{"value1", "value2"};

        // Create a mock of the java.sql.Array interface.
        Array mockSqlArray = mock(Array.class);
        when(mockSqlArray.getArray()).thenReturn(expectedArray);

        // Act
        // Call the method that extracts the underlying Java array.
        Object actualArray = typeHandler.extractArray(mockSqlArray);

        // Assert
        // Verify that the method returned the exact object instance from the mock.
        assertSame("The extracted object should be the same instance as the one from the mock.", expectedArray, actualArray);
    }
}
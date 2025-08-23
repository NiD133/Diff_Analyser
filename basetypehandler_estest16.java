package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the StringTypeHandler.
 * This focuses on testing the specific implementation of the BaseTypeHandler.
 */
public class StringTypeHandlerTest {

    /**
     * Tests that the handler can correctly retrieve a string value
     * from a CallableStatement by its column index.
     */
    @Test
    public void shouldGetStringResultFromCallableStatementByIndex() throws SQLException {
        // Arrange
        final String EXPECTED_STRING = "Hello World";
        final int COLUMN_INDEX = 1;

        // Create the handler to be tested
        StringTypeHandler handler = new StringTypeHandler();

        // Mock the database dependency (CallableStatement)
        CallableStatement mockCallableStatement = mock(CallableStatement.class);
        when(mockCallableStatement.getString(COLUMN_INDEX)).thenReturn(EXPECTED_STRING);

        // Act
        // Call the method under test to get the result
        String actualString = handler.getResult(mockCallableStatement, COLUMN_INDEX);

        // Assert
        // Verify that the handler returned the expected string from the mock statement
        assertEquals(EXPECTED_STRING, actualString);
    }
}
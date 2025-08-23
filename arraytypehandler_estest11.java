package org.apache.ibatis.type;

import org.junit.Test;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains tests for the ArrayTypeHandler.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class ArrayTypeHandler_ESTestTest11 { // The class name is kept for consistency with the original test suite structure.

    /**
     * Verifies that getNullableResult correctly extracts an object from a SQL Array
     * when retrieved from a CallableStatement by column index.
     */
    @Test
    public void shouldReturnArrayContentWhenGettingResultFromCallableStatement() throws SQLException {
        // Arrange
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        final int anyColumnIndex = 1;

        // 1. Define the expected data to be returned by the SQL Array.
        // Using a standard Timestamp is more readable than building it from an Instant.
        Timestamp expectedTimestamp = Timestamp.valueOf("2024-01-15 10:30:00.123456789");

        // 2. Mock the java.sql.Array to return the expected Timestamp.
        // The type handler should call array.getArray() to extract the content.
        Array mockSqlArray = mock(Array.class);
        when(mockSqlArray.getArray()).thenReturn(expectedTimestamp);

        // 3. Mock the CallableStatement to return the mock SQL Array for our column index.
        CallableStatement mockCallableStatement = mock(CallableStatement.class);
        when(mockCallableStatement.getArray(anyColumnIndex)).thenReturn(mockSqlArray);

        // Act
        // The getNullableResult method should orchestrate the calls to get the final object.
        Object actualResult = arrayTypeHandler.getNullableResult(mockCallableStatement, anyColumnIndex);

        // Assert
        // Verify that the returned object is of the correct type and value.
        assertTrue("The result should be an instance of Timestamp.", actualResult instanceof Timestamp);
        assertEquals("The returned timestamp should match the expected one.", expectedTimestamp, actualResult);
    }
}
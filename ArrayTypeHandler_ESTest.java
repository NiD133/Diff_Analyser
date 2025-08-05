package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Test suite for ArrayTypeHandler functionality.
 * Tests array extraction, parameter setting, result retrieval, and type name resolution.
 */
public class ArrayTypeHandlerTest {

    private static final int SAMPLE_PARAMETER_INDEX = 1;
    private static final String SAMPLE_COLUMN_NAME = "test_column";
    
    // ========== Array Extraction Tests ==========
    
    @Test
    public void extractArray_WithNullArray_ShouldReturnNull() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        
        // When
        Object result = handler.extractArray(null);
        
        // Then
        assertNull("Extracting from null array should return null", result);
    }

    @Test
    public void extractArray_WithValidArray_ShouldReturnArrayContents() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        LocalDate testDate = LocalDate.of(1, Month.JANUARY, 1);
        Date expectedDate = Date.valueOf(testDate);
        
        Array mockArray = mock(Array.class);
        when(mockArray.getArray()).thenReturn(expectedDate);
        
        // When
        Object result = handler.extractArray(mockArray);
        
        // Then
        assertSame("Should return the array contents", expectedDate, result);
    }

    // ========== Parameter Setting Tests ==========
    
    @Test(expected = RuntimeException.class)
    public void setNonNullParameter_WithUnsupportedType_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        Time unsupportedParameter = new Time(226);
        JdbcType jdbcType = JdbcType.BINARY;
        
        // When & Then
        handler.setNonNullParameter(mockStatement, SAMPLE_PARAMETER_INDEX, unsupportedParameter, jdbcType);
        // Should throw RuntimeException with message about unsupported type
    }

    @Test(expected = NullPointerException.class)
    public void setNonNullParameter_WithNullStatement_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        JdbcType jdbcType = JdbcType.TIMESTAMP;
        
        // When & Then
        handler.setNonNullParameter(null, SAMPLE_PARAMETER_INDEX, null, jdbcType);
        // Should throw NullPointerException
    }

    // ========== Type Name Resolution Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void resolveTypeName_WithNullClass_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        
        // When & Then
        handler.resolveTypeName(null);
        // Should throw NullPointerException
    }

    @Test
    public void resolveTypeName_WithDateClass_ShouldReturnTimestampType() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        Class<java.util.Date> dateClass = java.util.Date.class;
        
        // When
        String typeName = handler.resolveTypeName(dateClass);
        
        // Then
        assertEquals("Date class should map to TIMESTAMP type", "TIMESTAMP", typeName);
    }

    // ========== Result Retrieval Tests - CallableStatement ==========
    
    @Test(expected = NullPointerException.class)
    public void getNullableResult_CallableStatement_WithNullStatement_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        
        // When & Then
        handler.getNullableResult((CallableStatement) null, SAMPLE_PARAMETER_INDEX);
        // Should throw NullPointerException
    }

    @Test
    public void getNullableResult_CallableStatement_WithNullArray_ShouldReturnNull() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        CallableStatement mockStatement = mock(CallableStatement.class);
        when(mockStatement.getArray(SAMPLE_PARAMETER_INDEX)).thenReturn(null);
        
        // When
        Object result = handler.getNullableResult(mockStatement, SAMPLE_PARAMETER_INDEX);
        
        // Then
        assertNull("Null array should return null result", result);
    }

    @Test
    public void getNullableResult_CallableStatement_WithValidArray_ShouldReturnExtractedValue() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        Instant testInstant = Instant.ofEpochSecond(-1314L, 0L);
        Timestamp expectedTimestamp = Timestamp.from(testInstant);
        
        Array mockArray = mock(Array.class);
        when(mockArray.getArray()).thenReturn(expectedTimestamp);
        
        CallableStatement mockStatement = mock(CallableStatement.class);
        when(mockStatement.getArray(-1314)).thenReturn(mockArray);
        
        // When
        Timestamp result = (Timestamp) handler.getNullableResult(mockStatement, -1314);
        
        // Then
        assertNotNull("Should return a timestamp", result);
        assertEquals("Timestamp should have correct nanoseconds", 0, result.getNanos());
    }

    // ========== Result Retrieval Tests - ResultSet ==========
    
    @Test(expected = NullPointerException.class)
    public void getNullableResult_ResultSet_WithNullResultSetByName_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        
        // When & Then
        handler.getNullableResult((ResultSet) null, SAMPLE_COLUMN_NAME);
        // Should throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResult_ResultSet_WithNullResultSetByIndex_ShouldThrowException() throws Exception {
        // Given
        ArrayTypeHandler handler = new ArrayTypeHandler();
        
        // When & Then
        handler.getNullableResult((ResultSet) null, 0);
        // Should throw NullPointerException
    }
}
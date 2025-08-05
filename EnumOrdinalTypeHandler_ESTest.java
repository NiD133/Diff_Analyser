package org.apache.ibatis.type;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test suite for EnumOrdinalTypeHandler functionality.
 * Tests the conversion between enum ordinal values and enum constants.
 */
public class EnumOrdinalTypeHandlerTest {

    private EnumOrdinalTypeHandler<JdbcType> typeHandler;
    private ResultSet mockResultSet;
    private CallableStatement mockCallableStatement;
    private PreparedStatement mockPreparedStatement;

    @Before
    public void setUp() {
        typeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        mockResultSet = mock(ResultSet.class);
        mockCallableStatement = mock(CallableStatement.class);
        mockPreparedStatement = mock(PreparedStatement.class);
    }

    // Constructor Tests
    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNullType_ShouldThrowException() {
        new EnumOrdinalTypeHandler<JdbcType>(null);
    }

    @Test
    public void constructor_WithValidEnumType_ShouldCreateHandler() {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        assertNotNull(handler);
    }

    // setNonNullParameter Tests
    @Test
    public void setNonNullParameter_WithValidEnum_ShouldSetOrdinalValue() throws SQLException {
        JdbcType enumValue = JdbcType.VARCHAR;
        int parameterIndex = 1;
        
        typeHandler.setNonNullParameter(mockPreparedStatement, parameterIndex, enumValue, JdbcType.INTEGER);
        
        verify(mockPreparedStatement).setInt(parameterIndex, enumValue.ordinal());
    }

    @Test(expected = NullPointerException.class)
    public void setNonNullParameter_WithNullStatement_ShouldThrowException() throws SQLException {
        typeHandler.setNonNullParameter(null, 1, JdbcType.VARCHAR, JdbcType.INTEGER);
    }

    // getNullableResult from ResultSet by column index Tests
    @Test
    public void getNullableResult_ByColumnIndex_WithValidOrdinal_ShouldReturnEnum() throws SQLException {
        int columnIndex = 1;
        int ordinalValue = 0; // JdbcType.ARRAY has ordinal 0
        when(mockResultSet.getInt(columnIndex)).thenReturn(ordinalValue);
        when(mockResultSet.wasNull()).thenReturn(false);

        JdbcType result = typeHandler.getNullableResult(mockResultSet, columnIndex);

        assertEquals(JdbcType.ARRAY, result);
    }

    @Test
    public void getNullableResult_ByColumnIndex_WithNullValue_ShouldReturnNull() throws SQLException {
        int columnIndex = 1;
        when(mockResultSet.getInt(columnIndex)).thenReturn(0);
        when(mockResultSet.wasNull()).thenReturn(true);

        JdbcType result = typeHandler.getNullableResult(mockResultSet, columnIndex);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullableResult_ByColumnIndex_WithInvalidOrdinal_ShouldThrowException() throws SQLException {
        int columnIndex = 1;
        int invalidOrdinal = -1824; // Invalid ordinal value
        when(mockResultSet.getInt(columnIndex)).thenReturn(invalidOrdinal);
        when(mockResultSet.wasNull()).thenReturn(false);

        typeHandler.getNullableResult(mockResultSet, columnIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullableResult_ByColumnIndex_WithOrdinalTooLarge_ShouldThrowException() throws SQLException {
        int columnIndex = 1;
        int tooLargeOrdinal = 538; // Larger than available enum constants
        when(mockResultSet.getInt(columnIndex)).thenReturn(tooLargeOrdinal);
        when(mockResultSet.wasNull()).thenReturn(false);

        typeHandler.getNullableResult(mockResultSet, columnIndex);
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResult_ByColumnIndex_WithNullResultSet_ShouldThrowException() throws SQLException {
        typeHandler.getNullableResult((ResultSet) null, 1);
    }

    // getNullableResult from ResultSet by column name Tests
    @Test
    public void getNullableResult_ByColumnName_WithValidOrdinal_ShouldReturnEnum() throws SQLException {
        String columnName = "jdbc_type";
        int ordinalValue = 1; // JdbcType.BIT has ordinal 1
        when(mockResultSet.getInt(columnName)).thenReturn(ordinalValue);
        when(mockResultSet.wasNull()).thenReturn(false);

        JdbcType result = typeHandler.getNullableResult(mockResultSet, columnName);

        assertEquals(JdbcType.BIT, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullableResult_ByColumnName_WithInvalidOrdinal_ShouldThrowException() throws SQLException {
        String columnName = "jdbc_type";
        int invalidOrdinal = -874; // Invalid ordinal value
        when(mockResultSet.getInt(columnName)).thenReturn(invalidOrdinal);
        when(mockResultSet.wasNull()).thenReturn(false);

        typeHandler.getNullableResult(mockResultSet, columnName);
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResult_ByColumnName_WithNullResultSet_ShouldThrowException() throws SQLException {
        typeHandler.getNullableResult((ResultSet) null, "column_name");
    }

    // getNullableResult from CallableStatement Tests
    @Test
    public void getNullableResult_FromCallableStatement_WithValidOrdinal_ShouldReturnEnum() throws SQLException {
        int parameterIndex = 1;
        int ordinalValue = 15; // Valid ordinal for some JdbcType
        when(mockCallableStatement.getInt(parameterIndex)).thenReturn(ordinalValue);
        when(mockCallableStatement.wasNull()).thenReturn(false);

        JdbcType result = typeHandler.getNullableResult(mockCallableStatement, parameterIndex);

        assertNotNull(result);
        assertEquals(ordinalValue, result.ordinal());
    }

    @Test
    public void getNullableResult_FromCallableStatement_WithNullValue_ShouldReturnNull() throws SQLException {
        int parameterIndex = 1;
        when(mockCallableStatement.getInt(parameterIndex)).thenReturn(0);
        when(mockCallableStatement.wasNull()).thenReturn(true);

        JdbcType result = typeHandler.getNullableResult(mockCallableStatement, parameterIndex);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullableResult_FromCallableStatement_WithInvalidOrdinal_ShouldThrowException() throws SQLException {
        int parameterIndex = 1;
        int invalidOrdinal = -1824; // Invalid ordinal value
        when(mockCallableStatement.getInt(parameterIndex)).thenReturn(invalidOrdinal);
        when(mockCallableStatement.wasNull()).thenReturn(false);

        typeHandler.getNullableResult(mockCallableStatement, parameterIndex);
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResult_FromCallableStatement_WithNullStatement_ShouldThrowException() throws SQLException {
        typeHandler.getNullableResult((CallableStatement) null, 1);
    }

    // Integration test
    @Test
    public void integrationTest_SetAndGetParameter_ShouldMaintainConsistency() throws SQLException {
        // Set a parameter
        JdbcType originalEnum = JdbcType.VARCHAR;
        typeHandler.setNonNullParameter(mockPreparedStatement, 1, originalEnum, JdbcType.INTEGER);
        
        // Verify the ordinal was set correctly
        verify(mockPreparedStatement).setInt(1, originalEnum.ordinal());
        
        // Simulate getting the same value back
        when(mockCallableStatement.getInt(1)).thenReturn(originalEnum.ordinal());
        when(mockCallableStatement.wasNull()).thenReturn(false);
        
        JdbcType retrievedEnum = typeHandler.getNullableResult(mockCallableStatement, 1);
        
        assertEquals(originalEnum, retrievedEnum);
    }
}
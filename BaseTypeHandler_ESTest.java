/*
 * Improved test suite for BaseTypeHandler and its concrete implementations
 * Focus: Testing parameter setting, result retrieval, and error handling scenarios
 */

package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Month;
import java.time.YearMonth;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.ClobTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MonthTypeHandler;
import org.apache.ibatis.type.ObjectTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.YearMonthTypeHandler;

/**
 * Test suite for BaseTypeHandler and its concrete implementations.
 * Tests cover parameter setting, result retrieval, null handling, and error scenarios.
 */
public class BaseTypeHandler_ImprovedTest {

    // ============ Parameter Setting Tests ============

    @Test(timeout = 4000)
    public void shouldSetStringParameterWithNumericJdbcType() throws Throwable {
        // Given: A StringTypeHandler and a prepared statement
        StringTypeHandler stringHandler = new StringTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        JdbcType numericType = JdbcType.NUMERIC;

        // When: Setting a string parameter with NUMERIC JDBC type
        stringHandler.setParameter(preparedStatement, 3693, "PARTIAL", numericType);

        // Then: No exception should be thrown (parameter setting succeeded)
        // Note: The actual parameter setting behavior is tested in the concrete type handler
    }

    // ============ Result Retrieval Tests ============

    @Test(timeout = 4000)
    public void shouldReturnNullWhenYearMonthResultIsNull() throws Throwable {
        // Given: A YearMonthTypeHandler and a callable statement returning null
        YearMonthTypeHandler yearMonthHandler = new YearMonthTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        when(callableStatement.getString(0)).thenReturn(null);

        // When: Getting result from callable statement
        YearMonth result = yearMonthHandler.getResult(callableStatement, 0);

        // Then: Result should be null
        assertNull("YearMonth result should be null when database returns null", result);
    }

    @Test(timeout = 4000)
    public void shouldReturnNullWhenObjectResultIsNull() throws Throwable {
        // Given: An ObjectTypeHandler and a callable statement returning null
        ObjectTypeHandler objectHandler = new ObjectTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        when(callableStatement.getObject(1779)).thenReturn(null);

        // When: Getting nullable result from callable statement
        Object result = objectHandler.getNullableResult(callableStatement, 1779);

        // Then: Result should be null
        assertNull("Object result should be null when database returns null", result);
    }

    @Test(timeout = 4000)
    public void shouldRetrieveStringValueFromCallableStatement() throws Throwable {
        // Given: A StringTypeHandler and a callable statement returning a string
        StringTypeHandler stringHandler = new StringTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        String expectedValue = " Q9@^x<<xBG*k";
        when(callableStatement.getString(0)).thenReturn(expectedValue);

        // When: Getting result from callable statement
        String result = stringHandler.getResult(callableStatement, 0);

        // Then: Should return the expected string value
        assertEquals("String result should match the value from callable statement",
                    expectedValue, result);
    }

    // ============ Enum Handling Tests ============

    @Test(timeout = 4000)
    public void shouldHandleEnumOrdinalParameterSetting() throws Throwable {
        // Given: An EnumOrdinalTypeHandler for JdbcType and mock statements
        Class<JdbcType> jdbcTypeClass = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> enumHandler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        CallableStatement callableStatement = mock(CallableStatement.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Given: Callable statement returns ordinal 0 (first enum value) and wasNull returns false
        when(callableStatement.getInt(807)).thenReturn(0);
        when(callableStatement.wasNull()).thenReturn(false);

        // When: Getting enum result and then setting it as parameter
        JdbcType retrievedEnum = enumHandler.getNullableResult(callableStatement, 807);
        enumHandler.setNonNullParameter(preparedStatement, 0, retrievedEnum, retrievedEnum);

        // Then: Should complete without exceptions
        // The enum value should be properly retrieved and set
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForInvalidEnumOrdinal() throws Throwable {
        // Given: An EnumOrdinalTypeHandler for JdbcType
        Class<JdbcType> jdbcTypeClass = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> enumHandler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        CallableStatement callableStatement = mock(CallableStatement.class);
        when(callableStatement.getInt(-983)).thenReturn(-983);

        // When/Then: Getting result with invalid ordinal should throw IllegalArgumentException
        try {
            enumHandler.getNullableResult(callableStatement, -983);
            fail("Expected IllegalArgumentException for invalid enum ordinal");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should mention the invalid ordinal value",
                      e.getMessage().contains("Cannot convert -983 to JdbcType"));
        }
    }

    // ============ Null Parameter Handling Tests ============

    @Test(timeout = 4000)
    public void shouldSetNullParameterWithSpecifiedJdbcType() throws Throwable {
        // Given: A ClobTypeHandler and a prepared statement
        ClobTypeHandler clobHandler = new ClobTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        JdbcType jdbcType = JdbcType.DATALINK;

        // When: Setting null parameter with specified JDBC type
        clobHandler.setParameter(preparedStatement, -1, null, jdbcType);

        // Then: Should complete without exception (null handling with JDBC type works)
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenJdbcTypeIsNullForNullParameter() throws Throwable {
        // Given: A ClobTypeHandler and a prepared statement
        ClobTypeHandler clobHandler = new ClobTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // When/Then: Setting null parameter without JDBC type should throw RuntimeException
        try {
            clobHandler.setParameter(preparedStatement, -3, null, null);
            fail("Expected RuntimeException when JdbcType is null for null parameter");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention JDBC type requirement for nullable parameters",
                      e.getMessage().contains("JDBC requires that the JdbcType must be specified"));
        }
    }

    // ============ Error Handling Tests ============

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenPreparedStatementIsNullForParameterSetting() throws Throwable {
        // Given: A ClobTypeHandler with null PreparedStatement
        ClobTypeHandler clobHandler = ClobTypeHandler.INSTANCE;
        JdbcType jdbcType = JdbcType.TIME_WITH_TIMEZONE;

        // When/Then: Setting parameter on null PreparedStatement should throw NullPointerException
        try {
            clobHandler.setParameter(null, -3018, null, jdbcType);
            fail("Expected NullPointerException when PreparedStatement is null");
        } catch (NullPointerException e) {
            // Expected behavior - BaseTypeHandler should handle null PreparedStatement
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenResultSetIsNullForResultRetrieval() throws Throwable {
        // Given: An ObjectTypeHandler with null ResultSet
        ObjectTypeHandler objectHandler = new ObjectTypeHandler();

        // When/Then: Getting result from null ResultSet should throw NullPointerException
        try {
            objectHandler.getNullableResult((ResultSet) null, "columnName");
            fail("Expected NullPointerException when ResultSet is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenCallableStatementIsNullForResultRetrieval() throws Throwable {
        // Given: A ClobTypeHandler with null CallableStatement
        ClobTypeHandler clobHandler = ClobTypeHandler.INSTANCE;

        // When/Then: Getting result from null CallableStatement should throw RuntimeException
        try {
            clobHandler.getResult((CallableStatement) null, 4491);
            fail("Expected RuntimeException when CallableStatement is null");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention column retrieval error",
                      e.getMessage().contains("Error attempting to get column #4491"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForIncompatibleArrayTypeParameter() throws Throwable {
        // Given: An ArrayTypeHandler and incompatible parameter type
        MonthTypeHandler monthHandler = new MonthTypeHandler();
        ArrayTypeHandler arrayHandler = new ArrayTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        JdbcType jdbcType = JdbcType.DOUBLE;

        // When/Then: Setting incompatible type should throw RuntimeException
        try {
            arrayHandler.setNonNullParameter(preparedStatement, -349, monthHandler, jdbcType);
            fail("Expected RuntimeException for incompatible array parameter type");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention ArrayType Handler requirements",
                      e.getMessage().contains("ArrayType Handler requires SQL array or java array parameter"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenPreparedStatementIsNullForNonNullParameter() throws Throwable {
        // Given: An IntegerTypeHandler with null PreparedStatement
        IntegerTypeHandler integerHandler = new IntegerTypeHandler();
        Integer value = new Integer(0);
        JdbcType jdbcType = JdbcType.DATALINK;

        // When/Then: Setting non-null parameter on null PreparedStatement should throw NullPointerException
        try {
            integerHandler.setNonNullParameter(null, 0, value, jdbcType);
            fail("Expected NullPointerException when PreparedStatement is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenNonNullParameterFailsToSet() throws Throwable {
        // Given: A ClobTypeHandler with null PreparedStatement and non-null value
        ClobTypeHandler clobHandler = ClobTypeHandler.INSTANCE;
        JdbcType jdbcType = JdbcType.STRUCT;

        // When/Then: Setting non-null parameter should throw RuntimeException when PreparedStatement is null
        try {
            clobHandler.setParameter(null, -420, "testValue", jdbcType);
            fail("Expected RuntimeException when setting non-null parameter fails");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention parameter setting error",
                      e.getMessage().contains("Error setting non null for parameter #-420"));
        }
    }

    // ============ Configuration Tests ============

    @Test(timeout = 4000)
    public void shouldAllowSettingNullConfiguration() throws Throwable {
        // Given: A ClobTypeHandler
        ClobTypeHandler clobHandler = new ClobTypeHandler();

        // When: Setting null configuration
        clobHandler.setConfiguration(null);

        // Then: Should complete without exception (deprecated method allows null)
    }

    // ============ Additional Result Set Error Tests ============

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenResultSetIsNullForColumnIndexRetrieval() throws Throwable {
        // Given: An IntegerTypeHandler with null ResultSet
        IntegerTypeHandler integerHandler = new IntegerTypeHandler();

        // When/Then: Getting result by column index from null ResultSet should throw NullPointerException
        try {
            integerHandler.getNullableResult((ResultSet) null, 21);
            fail("Expected NullPointerException when ResultSet is null for column index retrieval");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenResultSetIsNullForColumnNameRetrieval() throws Throwable {
        // Given: An ObjectTypeHandler instance with null ResultSet
        ObjectTypeHandler objectHandler = ObjectTypeHandler.INSTANCE;

        // When/Then: Getting result by column name from null ResultSet should throw RuntimeException
        try {
            objectHandler.getResult((ResultSet) null, "testColumn");
            fail("Expected RuntimeException when ResultSet is null for column name retrieval");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention column retrieval error",
                      e.getMessage().contains("Error attempting to get column 'testColumn'"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenResultSetIsNullForIntegerColumnRetrieval() throws Throwable {
        // Given: An IntegerTypeHandler instance with null ResultSet
        IntegerTypeHandler integerHandler = IntegerTypeHandler.INSTANCE;

        // When/Then: Getting result by column index from null ResultSet should throw RuntimeException
        try {
            integerHandler.getResult((ResultSet) null, 2558);
            fail("Expected RuntimeException when ResultSet is null for integer column retrieval");
        } catch (RuntimeException e) {
            assertTrue("Exception should mention column retrieval error",
                      e.getMessage().contains("Error attempting to get column #2558"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenCallableStatementIsNullForArrayRetrieval() throws Throwable {
        // Given: An ArrayTypeHandler with null CallableStatement
        ArrayTypeHandler arrayHandler = new ArrayTypeHandler();

        // When/Then: Getting result from null CallableStatement should throw NullPointerException
        try {
            arrayHandler.getNullableResult((CallableStatement) null, 0);
            fail("Expected NullPointerException when CallableStatement is null for array retrieval");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }
}
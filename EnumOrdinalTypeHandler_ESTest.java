package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class EnumOrdinalTypeHandler_ESTest extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    private static final Class<JdbcType> JDBC_TYPE_CLASS = JdbcType.class;

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetIndex_InvalidOrdinalThrows() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(-1824).when(mockResultSet).getInt(anyInt());

        try {
            handler.getNullableResult(mockResultSet, 0);
            fail("Expected IllegalArgumentException for invalid ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert -1824 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_CallableStatement_ValidOrdinalThenSetParameter() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        CallableStatement mockCallableStmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(15).when(mockCallableStmt).getInt(anyInt());

        JdbcType result = handler.getNullableResult(mockCallableStmt, 0);
        assertNotNull(result);

        PreparedStatement mockPreparedStmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        handler.setNonNullParameter(mockPreparedStmt, 1996, result, result);
        verify(mockPreparedStmt).setInt(1996, result.ordinal());
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetColumnName_NullResultSetThrowsNPE() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);

        try {
            handler.getNullableResult((ResultSet) null, "");
            fail("Expected NullPointerException for null ResultSet");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetColumnName_InvalidOrdinalThrows() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(-874).when(mockResultSet).getInt(anyString());

        try {
            handler.getNullableResult(mockResultSet, "");
            fail("Expected IllegalArgumentException for invalid ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert -874 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetIndex_NullResultSetThrowsNPE() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);

        try {
            handler.getNullableResult((ResultSet) null, -2905);
            fail("Expected NullPointerException for null ResultSet");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_CallableStatementIndex_NullStatementThrowsNPE() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);

        try {
            handler.getNullableResult((CallableStatement) null, -3556);
            fail("Expected NullPointerException for null CallableStatement");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_CallableStatementIndex_InvalidOrdinalThrows() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        CallableStatement mockCallableStmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(-1824).when(mockCallableStmt).getInt(anyInt());

        try {
            handler.getNullableResult(mockCallableStmt, -1824);
            fail("Expected IllegalArgumentException for invalid ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert -1824 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_CallableStatementIndex_ZeroAndWasNullReturnsNull() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        CallableStatement mockCallableStmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockCallableStmt).getInt(anyInt());
        doReturn(true).when(mockCallableStmt).wasNull();

        JdbcType result = handler.getNullableResult(mockCallableStmt, 0);
        assertNull("Expected null when database value was NULL", result);
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_CallableStatementIndex_ValidOrdinalReturnsEnum() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        CallableStatement mockCallableStmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockCallableStmt).getInt(anyInt());
        doReturn(false).when(mockCallableStmt).wasNull();

        JdbcType result = handler.getNullableResult(mockCallableStmt, -1);
        assertEquals(JdbcType.ARRAY, result);
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetIndex_ValidOrdinalReturnsEnum() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockResultSet).getInt(anyInt());
        doReturn(false).when(mockResultSet).wasNull();

        JdbcType result = handler.getNullableResult(mockResultSet, 2778);
        assertEquals(JdbcType.ARRAY, result);
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetIndex_ZeroAndWasNullReturnsEnum() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockResultSet).getInt(anyInt());
        doReturn(false).when(mockResultSet).wasNull();

        JdbcType result = handler.getNullableResult(mockResultSet, -1);
        assertEquals(JdbcType.ARRAY, result);
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetIndex_OutOfRangeOrdinalThrows() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(538).when(mockResultSet).getInt(anyInt());

        try {
            handler.getNullableResult(mockResultSet, 538);
            fail("Expected IllegalArgumentException for out-of-range ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert 538 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetColumnName_NegativeOrdinalReturnsNull() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(-1).when(mockResultSet).getInt(anyString());
        doReturn(false).when(mockResultSet).wasNull();

        JdbcType result = handler.getNullableResult(mockResultSet, (String) null);
        assertNull("Expected null for invalid ordinal", result);
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetColumnName_ValidOrdinalReturnsEnum() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockResultSet).getInt(anyString());
        doReturn(false).when(mockResultSet).wasNull();

        JdbcType result = handler.getNullableResult(mockResultSet, "+%+\"nk/Mp#S]");
        assertEquals(JdbcType.ARRAY, result);
    }

    @Test(timeout = 4000)
    public void testConstructor_NullTypeArgumentThrowsException() throws Throwable {
        try {
            new EnumOrdinalTypeHandler<JdbcType>(null);
            fail("Expected IllegalArgumentException for null type argument");
        } catch (IllegalArgumentException e) {
            assertEquals("Type argument cannot be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSetNonNullParameter_NullPreparedStatementThrowsNPE() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        JdbcType value = JdbcType.LONGVARBINARY;

        try {
            handler.setNonNullParameter(null, -2025, value, value);
            fail("Expected NullPointerException for null PreparedStatement");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResult_ResultSetColumnName_OrdinalOneReturnsCorrectEnum() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(JDBC_TYPE_CLASS);
        ResultSet mockResultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn(1).when(mockResultSet).getInt(anyString());

        JdbcType result = handler.getNullableResult(mockResultSet, "Nv<W]59B0_y%8#{]L");
        assertEquals(JdbcType.BIT, result);
    }
}
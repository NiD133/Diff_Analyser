package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EnumOrdinalTypeHandlerTest {

    private final Class<JdbcType> jdbcTypeClass = JdbcType.class;

    @Test(timeout = 4000)
    public void testGetNullableResultWithInvalidOrdinalFromResultSet() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(-1824).when(resultSet).getInt(anyInt());

        try {
            handler.getNullableResult(resultSet, 0);
            fail("Expected IllegalArgumentException for invalid ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert -1824 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSetNonNullParameterWithValidJdbcType() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(15).when(callableStatement).getInt(anyInt());

        JdbcType jdbcType = handler.getNullableResult(callableStatement, 0);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        handler.setNonNullParameter(preparedStatement, 1996, jdbcType, jdbcType);
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithNullResultSet() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);

        try {
            handler.getNullableResult((ResultSet) null, "");
            fail("Expected NullPointerException for null ResultSet");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithInvalidOrdinalFromCallableStatement() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(-1824).when(callableStatement).getInt(anyInt());

        try {
            handler.getNullableResult(callableStatement, -1824);
            fail("Expected IllegalArgumentException for invalid ordinal");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert -1824 to JdbcType by ordinal value.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithNullCallableStatement() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);

        try {
            handler.getNullableResult((CallableStatement) null, -3556);
            fail("Expected NullPointerException for null CallableStatement");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithValidOrdinalFromCallableStatement() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(0).when(callableStatement).getInt(anyInt());
        doReturn(false).when(callableStatement).wasNull();

        JdbcType jdbcType = handler.getNullableResult(callableStatement, -1);
        assertEquals(JdbcType.ARRAY, jdbcType);
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithValidOrdinalFromResultSet() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(0).when(resultSet).getInt(anyInt());
        doReturn(false).when(resultSet).wasNull();

        JdbcType jdbcType = handler.getNullableResult(resultSet, 2778);
        assertEquals(JdbcType.ARRAY, jdbcType);
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullType() throws Throwable {
        try {
            new EnumOrdinalTypeHandler<JdbcType>(null);
            fail("Expected IllegalArgumentException for null type");
        } catch (IllegalArgumentException e) {
            assertEquals("Type argument cannot be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSetNonNullParameterWithNullPreparedStatement() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        JdbcType jdbcType = JdbcType.LONGVARBINARY;

        try {
            handler.setNonNullParameter(null, -2025, jdbcType, jdbcType);
            fail("Expected NullPointerException for null PreparedStatement");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetNullableResultWithValidOrdinalFromResultSetByName() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(jdbcTypeClass);
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt(anyString());

        JdbcType jdbcType = handler.getNullableResult(resultSet, "columnName");
        assertEquals(JdbcType.BIT, jdbcType);
    }
}
package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.YearMonth;
import org.apache.ibatis.session.Configuration;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BaseTypeHandlerTest extends BaseTypeHandler_ESTest_scaffolding {

    private static final int PARAM_INDEX = 3693;
    private static final int CALLABLE_INDEX = 0;
    private static final int RESULT_SET_INDEX = 1779;
    private static final int INVALID_INDEX = -3018;
    private static final String TEST_STRING = "PARTIAL";
    private static final String MOCK_STRING = " Q9@^x<<xBG*k";
    private static final String COLUMN_NAME = "Whv13V)Nc";

    @Test(timeout = 4000)
    public void testStringTypeHandlerSetParameter() throws Throwable {
        StringTypeHandler stringTypeHandler = new StringTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        JdbcType jdbcType = JdbcType.NUMERIC;

        stringTypeHandler.setParameter(preparedStatement, PARAM_INDEX, TEST_STRING, jdbcType);
    }

    @Test(timeout = 4000)
    public void testYearMonthTypeHandlerGetResultWithNull() throws Throwable {
        YearMonthTypeHandler yearMonthTypeHandler = new YearMonthTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(null).when(callableStatement).getString(anyInt());

        YearMonth result = yearMonthTypeHandler.getResult(callableStatement, CALLABLE_INDEX);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testObjectTypeHandlerGetNullableResultWithNull() throws Throwable {
        ObjectTypeHandler objectTypeHandler = new ObjectTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(null).when(callableStatement).getObject(anyInt());

        Object result = objectTypeHandler.getNullableResult(callableStatement, RESULT_SET_INDEX);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testEnumOrdinalTypeHandlerSetAndGet() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(0).when(callableStatement).getInt(anyInt());
        doReturn(false).when(callableStatement).wasNull();

        JdbcType jdbcType = enumOrdinalTypeHandler.getNullableResult(callableStatement, 807);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        enumOrdinalTypeHandler.setNonNullParameter(preparedStatement, 0, jdbcType, jdbcType);
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerSetParameterWithNullPreparedStatement() {
        ClobTypeHandler clobTypeHandler = ClobTypeHandler.INSTANCE;
        JdbcType jdbcType = JdbcType.TIME_WITH_TIMEZONE;

        try {
            clobTypeHandler.setParameter(null, INVALID_INDEX, null, jdbcType);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testArrayTypeHandlerSetNonNullParameterWithInvalidType() {
        MonthTypeHandler monthTypeHandler = new MonthTypeHandler();
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        JdbcType jdbcType = JdbcType.DOUBLE;

        try {
            arrayTypeHandler.setNonNullParameter(preparedStatement, -349, monthTypeHandler, jdbcType);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testIntegerTypeHandlerSetNonNullParameterWithNullPreparedStatement() {
        IntegerTypeHandler integerTypeHandler = new IntegerTypeHandler();
        Integer parameter = 0;
        JdbcType jdbcType = JdbcType.DATALINK;

        try {
            integerTypeHandler.setNonNullParameter(null, 0, parameter, jdbcType);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.IntegerTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerGetResultWithNullCallableStatement() {
        ClobTypeHandler clobTypeHandler = ClobTypeHandler.INSTANCE;

        try {
            clobTypeHandler.getResult(null, 4491);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testObjectTypeHandlerGetNullableResultWithNullResultSet() {
        ObjectTypeHandler objectTypeHandler = new ObjectTypeHandler();

        try {
            objectTypeHandler.getNullableResult(null, COLUMN_NAME);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ObjectTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testIntegerTypeHandlerGetNullableResultWithNullResultSet() {
        IntegerTypeHandler integerTypeHandler = new IntegerTypeHandler();

        try {
            integerTypeHandler.getNullableResult(null, 21);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.IntegerTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testArrayTypeHandlerGetNullableResultWithNullCallableStatement() {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        try {
            arrayTypeHandler.getNullableResult(null, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnumOrdinalTypeHandlerGetNullableResultWithInvalidOrdinal() {
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler = new EnumOrdinalTypeHandler<>(JdbcType.class);
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(-983).when(callableStatement).getInt(anyInt());

        try {
            enumOrdinalTypeHandler.getNullableResult(callableStatement, -983);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.ibatis.type.EnumOrdinalTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerSetParameterWithNullJdbcType() {
        ClobTypeHandler clobTypeHandler = new ClobTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        try {
            clobTypeHandler.setParameter(preparedStatement, -3, null, null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerSetParameterWithValidInputs() throws Throwable {
        ClobTypeHandler clobTypeHandler = new ClobTypeHandler();
        JdbcType jdbcType = JdbcType.DATALINK;
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        clobTypeHandler.setParameter(preparedStatement, -1, null, jdbcType);
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerSetParameterWithInvalidJdbcType() {
        ClobTypeHandler clobTypeHandler = ClobTypeHandler.INSTANCE;
        JdbcType jdbcType = JdbcType.STRUCT;

        try {
            clobTypeHandler.setParameter(null, -420, "tA pe~s<#.", jdbcType);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testStringTypeHandlerGetResult() throws Throwable {
        StringTypeHandler stringTypeHandler = new StringTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class);
        doReturn(MOCK_STRING).when(callableStatement).getString(anyInt());

        String result = stringTypeHandler.getResult(callableStatement, CALLABLE_INDEX);
        assertEquals(MOCK_STRING, result);
    }

    @Test(timeout = 4000)
    public void testObjectTypeHandlerGetResultWithNullResultSet() {
        ObjectTypeHandler objectTypeHandler = ObjectTypeHandler.INSTANCE;

        try {
            objectTypeHandler.getResult(null, "(?y^aVym-^?qnK=B\"");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testClobTypeHandlerSetConfiguration() {
        ClobTypeHandler clobTypeHandler = new ClobTypeHandler();
        clobTypeHandler.setConfiguration(null);
    }

    @Test(timeout = 4000)
    public void testIntegerTypeHandlerGetResultWithNullResultSet() {
        IntegerTypeHandler integerTypeHandler = IntegerTypeHandler.INSTANCE;

        try {
            integerTypeHandler.getResult(null, 2558);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }
}
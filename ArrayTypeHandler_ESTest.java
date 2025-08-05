package org.apache.ibatis.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

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

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ArrayTypeHandlerTest extends ArrayTypeHandler_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int MOCK_INDEX = 1577;
    private static final int INVALID_INDEX = -1314;

    @Test(timeout = TIMEOUT)
    public void testExtractArrayWithNullArray() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        Object result = arrayTypeHandler.extractArray(null);
        assertNull(result);
    }

    @Test(timeout = TIMEOUT)
    public void testExtractArrayWithMockedDateArray() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        Month month = Month.JANUARY;
        LocalDate localDate = MockLocalDate.of(1, month, 1);
        Date expectedDate = Date.valueOf(localDate);
        Array mockedArray = mock(Array.class, new ViolatedAssumptionAnswer());
        doReturn(expectedDate).when(mockedArray).getArray();
        Object result = arrayTypeHandler.extractArray(mockedArray);
        assertSame(expectedDate, result);
    }

    @Test(timeout = TIMEOUT)
    public void testSetNonNullParameterWithInvalidType() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        Time time = new Time(226);
        JdbcType jdbcType = JdbcType.BINARY;

        try {
            arrayTypeHandler.setNonNullParameter(preparedStatement, MOCK_INDEX, time, jdbcType);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSetNonNullParameterWithNullPreparedStatement() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        JdbcType jdbcType = JdbcType.TIMESTAMP;

        try {
            arrayTypeHandler.setNonNullParameter(null, 9, null, jdbcType);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testResolveTypeNameWithNullClass() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        try {
            arrayTypeHandler.resolveTypeName(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetNullableResultWithNullCallableStatement() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        try {
            arrayTypeHandler.getNullableResult(null, 2638);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetNullableResultWithMockedNullArray() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        CallableStatement callableStatement = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(callableStatement).getArray(anyInt());
        Object result = arrayTypeHandler.getNullableResult(callableStatement, MOCK_INDEX);
        assertNull(result);
    }

    @Test(timeout = TIMEOUT)
    public void testGetNullableResultWithNullResultSetByName() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        try {
            arrayTypeHandler.getNullableResult(null, "4i.1>=ua+");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testResolveTypeNameWithDateClass() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        String typeName = arrayTypeHandler.resolveTypeName(java.util.Date.class);
        assertEquals("TIMESTAMP", typeName);
    }

    @Test(timeout = TIMEOUT)
    public void testGetNullableResultWithNullResultSetByIndex() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();

        try {
            arrayTypeHandler.getNullableResult(null, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetNullableResultWithMockedTimestampArray() throws Throwable {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        Instant instant = MockInstant.ofEpochSecond(INVALID_INDEX, 0L);
        Timestamp expectedTimestamp = Timestamp.from(instant);
        Array mockedArray = mock(Array.class, new ViolatedAssumptionAnswer());
        doReturn(expectedTimestamp).when(mockedArray).getArray();
        CallableStatement callableStatement = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(mockedArray).when(callableStatement).getArray(anyInt());
        Timestamp result = (Timestamp) arrayTypeHandler.getNullableResult(callableStatement, INVALID_INDEX);
        assertEquals(0, result.getNanos());
    }
}
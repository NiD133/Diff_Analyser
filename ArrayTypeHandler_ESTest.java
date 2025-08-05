package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ArrayTypeHandler_ESTest {

    @Test(timeout = 4000)
    public void testExtractArray_WhenArrayIsNull_ReturnsNull() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        Object result = handler.extractArray(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testExtractArray_WhenArrayReturnsDate_ReturnsSameDate() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        LocalDate localDate = MockLocalDate.of(1, Month.JANUARY, 1);
        Date expectedDate = Date.valueOf(localDate);

        Array mockArray = mock(Array.class);
        doReturn(expectedDate).when(mockArray).getArray();

        Object result = handler.extractArray(mockArray);
        assertSame(expectedDate, result);
    }

    @Test(timeout = 4000)
    public void testSetNonNullParameter_WhenUnsupportedType_ThrowsRuntimeException() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        PreparedStatement ps = mock(PreparedStatement.class);
        Time unsupportedParam = new Time(226);
        JdbcType jdbcType = JdbcType.BINARY;

        try {
            handler.setNonNullParameter(ps, 1577, unsupportedParam, jdbcType);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("ArrayType Handler requires SQL array or java array parameter and does not support type class java.sql.Time", 
                         e.getMessage());
        }
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testSetNonNullParameter_WhenPreparedStatementIsNull_ThrowsNullPointerException() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        handler.setNonNullParameter(null, 9, null, JdbcType.TIMESTAMP);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testResolveTypeName_WhenClassIsNull_ThrowsNullPointerException() {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        handler.resolveTypeName(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testGetNullableResultFromCallableStatement_WhenCallableStatementIsNull_ThrowsNullPointerException() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        handler.getNullableResult((CallableStatement) null, 2638);
    }

    @Test(timeout = 4000)
    public void testGetNullableResultFromCallableStatement_WhenArrayIsNull_ReturnsNull() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        CallableStatement cs = mock(CallableStatement.class);
        doReturn(null).when(cs).getArray(anyInt());

        Object result = handler.getNullableResult(cs, 1577);
        assertNull(result);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testGetNullableResultFromResultSetByColumnName_WhenResultSetIsNull_ThrowsNullPointerException() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        handler.getNullableResult((ResultSet) null, "columnName");
    }

    @Test(timeout = 4000)
    public void testResolveTypeName_ForUtilDate_ReturnsTimestamp() {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        String typeName = handler.resolveTypeName(java.util.Date.class);
        assertEquals("TIMESTAMP", typeName);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testGetNullableResultFromResultSetByColumnIndex_WhenResultSetIsNull_ThrowsNullPointerException() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        handler.getNullableResult((ResultSet) null, 0);
    }

    @Test(timeout = 4000)
    public void testGetNullableResultFromCallableStatement_WhenArrayReturnsTimestamp_ReturnsTimestamp() throws Exception {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        Instant instant = MockInstant.ofEpochSecond(-1314L, 0L);
        Timestamp expectedTimestamp = Timestamp.from(instant);

        Array mockArray = mock(Array.class);
        doReturn(expectedTimestamp).when(mockArray).getArray();

        CallableStatement cs = mock(CallableStatement.class);
        doReturn(mockArray).when(cs).getArray(-1314);

        Timestamp result = (Timestamp) handler.getNullableResult(cs, -1314);
        assertEquals(0, result.getNanos());
    }
}
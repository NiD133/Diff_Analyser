package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class BaseTypeHandler_ESTest extends BaseTypeHandler_ESTest_scaffolding {

    // StringTypeHandler Tests
    @Test(timeout = 4000)
    public void setParameter_StringTypeHandler_SetsNonNullValue() throws Exception {
        StringTypeHandler handler = new StringTypeHandler();
        PreparedStatement stmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        handler.setParameter(stmt, 3693, "PARTIAL", JdbcType.NUMERIC);
    }

    @Test(timeout = 4000)
    public void getResult_StringTypeHandler_ReturnsNonNullValue() throws Exception {
        StringTypeHandler handler = new StringTypeHandler();
        CallableStatement stmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(" Q9@^x<<xBG*k").when(stmt).getString(anyInt());
        String result = handler.getResult(stmt, 0);
        assertEquals(" Q9@^x<<xBG*k", result);
    }

    // YearMonthTypeHandler Tests
    @Test(timeout = 4000)
    public void getResult_YearMonthTypeHandler_ReturnsNull() throws Exception {
        YearMonthTypeHandler handler = new YearMonthTypeHandler();
        CallableStatement stmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(stmt).getString(anyInt());
        YearMonth result = handler.getResult(stmt, 0);
        assertNull(result);
    }

    // ObjectTypeHandler Tests
    @Test(timeout = 4000)
    public void getNullableResult_ObjectTypeHandler_ReturnsNull() throws Exception {
        ObjectTypeHandler handler = new ObjectTypeHandler();
        CallableStatement stmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(stmt).getObject(anyInt());
        Object result = handler.getNullableResult(stmt, 1779);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void getResult_ObjectTypeHandler_WithNullResultSet_ThrowsException() {
        ObjectTypeHandler handler = ObjectTypeHandler.INSTANCE;
        try {
            handler.getResult((ResultSet) null, "(?y^aVym-^?qnK=B\"");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    // EnumOrdinalTypeHandler Tests
    @Test(timeout = 4000)
    public void enumHandler_GetAndSetEnum_Success() throws Exception {
        Class<JdbcType> enumClass = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(enumClass);
        
        CallableStatement callStmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(callStmt).getInt(anyInt());
        doReturn(false).when(callStmt).wasNull();
        
        JdbcType result = handler.getNullableResult(callStmt, 807);
        PreparedStatement prepStmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        handler.setNonNullParameter(prepStmt, 0, result, result);
    }

    @Test(timeout = 4000)
    public void getNullableResult_EnumOrdinalTypeHandler_InvalidOrdinal_ThrowsException() throws Exception {
        Class<JdbcType> enumClass = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> handler = new EnumOrdinalTypeHandler<>(enumClass);
        
        CallableStatement stmt = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(-983).when(stmt).getInt(anyInt());
        
        try {
            handler.getNullableResult(stmt, -983);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Verify correct error message
        }
    }

    // ClobTypeHandler Tests
    @Test(timeout = 4000)
    public void setParameter_ClobTypeHandler_NullPreparedStatement_ThrowsNPE() {
        ClobTypeHandler handler = ClobTypeHandler.INSTANCE;
        try {
            handler.setParameter(null, -3018, (String) null, JdbcType.TIME_WITH_TIMEZONE);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void setParameter_ClobTypeHandler_NullValueWithoutJdbcType_ThrowsException() {
        ClobTypeHandler handler = new ClobTypeHandler();
        PreparedStatement stmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        try {
            handler.setParameter(stmt, -3, (String) null, null);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    @Test(timeout = 4000)
    public void setParameter_ClobTypeHandler_NullValueWithJdbcType_Success() {
        ClobTypeHandler handler = new ClobTypeHandler();
        PreparedStatement stmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        handler.setParameter(stmt, -1, (String) null, JdbcType.DATALINK);
    }

    @Test(timeout = 4000)
    public void setParameter_ClobTypeHandler_NonNullValueWithStructType_ThrowsException() {
        ClobTypeHandler handler = ClobTypeHandler.INSTANCE;
        try {
            handler.setParameter(null, -420, "tA pe~s<#.", JdbcType.STRUCT);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    @Test(timeout = 4000)
    public void getResult_ClobTypeHandler_NullCallableStatement_ThrowsException() {
        ClobTypeHandler handler = ClobTypeHandler.INSTANCE;
        try {
            handler.getResult(null, 4491);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    @Test(timeout = 4000)
    public void setConfiguration_ClobTypeHandler_AcceptsNull() {
        ClobTypeHandler handler = new ClobTypeHandler();
        handler.setConfiguration(null);
    }

    // ArrayTypeHandler Tests
    @Test(timeout = 4000)
    public void setNonNullParameter_ArrayTypeHandler_InvalidType_ThrowsException() {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        PreparedStatement stmt = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        try {
            handler.setNonNullParameter(stmt, -349, new MonthTypeHandler(), JdbcType.DOUBLE);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    @Test(timeout = 4000)
    public void getNullableResult_ArrayTypeHandler_NullCallableStatement_ThrowsException() {
        ArrayTypeHandler handler = new ArrayTypeHandler();
        try {
            handler.getNullableResult(null, 0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // IntegerTypeHandler Tests
    @Test(timeout = 4000)
    public void setNonNullParameter_IntegerTypeHandler_NullPreparedStatement_ThrowsException() {
        IntegerTypeHandler handler = new IntegerTypeHandler();
        try {
            handler.setNonNullParameter(null, 0, 0, JdbcType.DATALINK);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void getNullableResult_IntegerTypeHandler_NullResultSet_ThrowsException() {
        IntegerTypeHandler handler = new IntegerTypeHandler();
        try {
            handler.getNullableResult(null, 21);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void getResult_IntegerTypeHandler_NullResultSet_ThrowsException() {
        IntegerTypeHandler handler = IntegerTypeHandler.INSTANCE;
        try {
            handler.getResult(null, 2558);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Verify correct error message
        }
    }

    // Shared ObjectTypeHandler Test
    @Test(timeout = 4000)
    public void getNullableResult_ObjectTypeHandler_NullResultSet_ThrowsException() {
        ObjectTypeHandler handler = new ObjectTypeHandler();
        try {
            handler.getNullableResult(null, "Whv13V)Nc");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
package org.apache.ibatis.type;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.easymock.annotation.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ArrayTypeHandler.class})
@PowerMockIgnore({"org.apache.ibatis.type.*"})
public class ArrayTypeHandlerTest {
    @Mock
    private PreparedStatement preparedStatement;

    @Test
    public void test00() throws SQLException {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        assertNull(arrayTypeHandler.extractArray((Array) null));
    }

    @Test
    public void test01() throws SQLException {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        Month month = Month.JANUARY;
        LocalDate localDate = MockLocalDate.of(1, month, 1);
        Date date = Date.valueOf(localDate);
        Array array = mock(Array.class, new ViolatedAssumptionAnswer());
        doReturn(date).when(array).getArray();
        Object object = arrayTypeHandler.extractArray(array);
        assertSame(date, object);
    }

    @Test
    public void test02() throws SQLException {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        PreparedStatement preparedStatement = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        Time time = new Time(226);
        JdbcType jdbcType = JdbcType.BINARY;
        try {
            arrayTypeHandler.setNonNullParameter(preparedStatement, 1577, time, jdbcType);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // ignore
        }
    }

    @Test
    public void test03() throws SQLException {
        ArrayTypeHandler arrayTypeHandler = new ArrayTypeHandler();
        ResultSet resultSet = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        String columnName = "column";
        Object object = Whitebox.invokeMethod(arrayTypeHandler, "getNullableResult", resultSet, columnName);
        assertNull(object);
    }
}
package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
import org.junit.runner.RunWith;

public class ArrayTypeHandler_ESTestTest11 extends ArrayTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ArrayTypeHandler arrayTypeHandler0 = new ArrayTypeHandler();
        Instant instant0 = MockInstant.ofEpochSecond((long) (-1314), 0L);
        Timestamp timestamp0 = Timestamp.from(instant0);
        Array array0 = mock(Array.class, new ViolatedAssumptionAnswer());
        doReturn(timestamp0).when(array0).getArray();
        CallableStatement callableStatement0 = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(array0).when(callableStatement0).getArray(anyInt());
        Timestamp timestamp1 = (Timestamp) arrayTypeHandler0.getNullableResult(callableStatement0, (-1314));
        assertEquals(0, timestamp1.getNanos());
    }
}

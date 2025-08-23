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

public class ArrayTypeHandler_ESTestTest2 extends ArrayTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ArrayTypeHandler arrayTypeHandler0 = new ArrayTypeHandler();
        Month month0 = Month.JANUARY;
        LocalDate localDate0 = MockLocalDate.of(1, month0, 1);
        Date date0 = Date.valueOf(localDate0);
        Array array0 = mock(Array.class, new ViolatedAssumptionAnswer());
        doReturn(date0).when(array0).getArray();
        Object object0 = arrayTypeHandler0.extractArray(array0);
        assertSame(date0, object0);
    }
}

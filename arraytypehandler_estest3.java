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

public class ArrayTypeHandler_ESTestTest3 extends ArrayTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ArrayTypeHandler arrayTypeHandler0 = new ArrayTypeHandler();
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        Time time0 = new Time(226);
        JdbcType jdbcType0 = JdbcType.BINARY;
        // Undeclared exception!
        try {
            arrayTypeHandler0.setNonNullParameter(preparedStatement0, 1577, time0, jdbcType0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ArrayType Handler requires SQL array or java array parameter and does not support type class java.sql.Time
            //
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }
}

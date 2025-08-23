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
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class BaseTypeHandler_ESTestTest6 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        MonthTypeHandler monthTypeHandler0 = new MonthTypeHandler();
        ArrayTypeHandler arrayTypeHandler0 = new ArrayTypeHandler();
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        JdbcType jdbcType0 = JdbcType.DOUBLE;
        // Undeclared exception!
        try {
            arrayTypeHandler0.setNonNullParameter(preparedStatement0, (-349), monthTypeHandler0, jdbcType0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ArrayType Handler requires SQL array or java array parameter and does not support type class org.apache.ibatis.type.MonthTypeHandler
            //
            verifyException("org.apache.ibatis.type.ArrayTypeHandler", e);
        }
    }
}

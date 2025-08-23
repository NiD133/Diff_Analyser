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

public class BaseTypeHandler_ESTestTest13 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        ClobTypeHandler clobTypeHandler0 = new ClobTypeHandler();
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        // Undeclared exception!
        try {
            clobTypeHandler0.setParameter(preparedStatement0, (-3), (String) null, (JdbcType) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // JDBC requires that the JdbcType must be specified for all nullable parameters.
            //
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }
}

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

public class BaseTypeHandler_ESTestTest8 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        ClobTypeHandler clobTypeHandler0 = ClobTypeHandler.INSTANCE;
        // Undeclared exception!
        try {
            clobTypeHandler0.getResult((CallableStatement) null, 4491);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error attempting to get column #4491 from callable statement.  Cause: java.lang.NullPointerException
            //
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }
}

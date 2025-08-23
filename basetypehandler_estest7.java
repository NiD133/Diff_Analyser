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

public class BaseTypeHandler_ESTestTest7 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        IntegerTypeHandler integerTypeHandler0 = new IntegerTypeHandler();
        Integer integer0 = new Integer(0);
        JdbcType jdbcType0 = JdbcType.DATALINK;
        // Undeclared exception!
        try {
            integerTypeHandler0.setNonNullParameter((PreparedStatement) null, 0, integer0, jdbcType0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.type.IntegerTypeHandler", e);
        }
    }
}

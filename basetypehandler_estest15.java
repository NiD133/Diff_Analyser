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

public class BaseTypeHandler_ESTestTest15 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        ClobTypeHandler clobTypeHandler0 = ClobTypeHandler.INSTANCE;
        JdbcType jdbcType0 = JdbcType.STRUCT;
        // Undeclared exception!
        try {
            clobTypeHandler0.setParameter((PreparedStatement) null, (-420), "tA pe~s<#.", jdbcType0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error setting non null for parameter #-420 with JdbcType STRUCT . Try setting a different JdbcType for this parameter or a different configuration property. Cause: java.lang.NullPointerException
            //
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }
}

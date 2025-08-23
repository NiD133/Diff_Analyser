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

public class BaseTypeHandler_ESTestTest17 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ObjectTypeHandler objectTypeHandler0 = ObjectTypeHandler.INSTANCE;
        // Undeclared exception!
        try {
            objectTypeHandler0.getResult((ResultSet) null, "(?y^aVym-^?qnK=B\"");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error attempting to get column '(?y^aVym-^?qnK=B\"' from result set.  Cause: java.lang.NullPointerException
            //
            verifyException("org.apache.ibatis.type.BaseTypeHandler", e);
        }
    }
}

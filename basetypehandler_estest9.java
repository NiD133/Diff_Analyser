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

public class BaseTypeHandler_ESTestTest9 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ObjectTypeHandler objectTypeHandler0 = new ObjectTypeHandler();
        // Undeclared exception!
        try {
            objectTypeHandler0.getNullableResult((ResultSet) null, "Whv13V)Nc");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.type.ObjectTypeHandler", e);
        }
    }
}

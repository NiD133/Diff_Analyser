package org.apache.ibatis.type;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class EnumOrdinalTypeHandler_ESTestTest15 extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler0 = null;
        try {
            enumOrdinalTypeHandler0 = new EnumOrdinalTypeHandler<JdbcType>((Class<JdbcType>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Type argument cannot be null
            //
            verifyException("org.apache.ibatis.type.EnumOrdinalTypeHandler", e);
        }
    }
}

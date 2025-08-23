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

public class BaseTypeHandler_ESTestTest12 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Class<JdbcType> class0 = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler0 = new EnumOrdinalTypeHandler<JdbcType>(class0);
        CallableStatement callableStatement0 = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn((-983)).when(callableStatement0).getInt(anyInt());
        // Undeclared exception!
        try {
            enumOrdinalTypeHandler0.getNullableResult(callableStatement0, (-983));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot convert -983 to JdbcType by ordinal value.
            //
            verifyException("org.apache.ibatis.type.EnumOrdinalTypeHandler", e);
        }
    }
}

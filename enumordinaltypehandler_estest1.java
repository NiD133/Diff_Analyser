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

public class EnumOrdinalTypeHandler_ESTestTest1 extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Class<JdbcType> class0 = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler0 = new EnumOrdinalTypeHandler<JdbcType>(class0);
        ResultSet resultSet0 = mock(ResultSet.class, new ViolatedAssumptionAnswer());
        doReturn((-1824)).when(resultSet0).getInt(anyInt());
        // Undeclared exception!
        try {
            enumOrdinalTypeHandler0.getNullableResult(resultSet0, 0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot convert -1824 to JdbcType by ordinal value.
            //
            verifyException("org.apache.ibatis.type.EnumOrdinalTypeHandler", e);
        }
    }
}

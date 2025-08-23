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

public class EnumOrdinalTypeHandler_ESTestTest2 extends EnumOrdinalTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Class<JdbcType> class0 = JdbcType.class;
        EnumOrdinalTypeHandler<JdbcType> enumOrdinalTypeHandler0 = new EnumOrdinalTypeHandler<JdbcType>(class0);
        CallableStatement callableStatement0 = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(15).when(callableStatement0).getInt(anyInt());
        JdbcType jdbcType0 = enumOrdinalTypeHandler0.getNullableResult(callableStatement0, 0);
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        enumOrdinalTypeHandler0.setNonNullParameter(preparedStatement0, 1996, jdbcType0, jdbcType0);
    }
}

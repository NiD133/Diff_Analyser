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

public class BaseTypeHandler_ESTestTest1 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        StringTypeHandler stringTypeHandler0 = new StringTypeHandler();
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        JdbcType jdbcType0 = JdbcType.NUMERIC;
        stringTypeHandler0.setParameter(preparedStatement0, 3693, "PARTIAL", jdbcType0);
    }
}

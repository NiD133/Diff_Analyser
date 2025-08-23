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

public class BaseTypeHandler_ESTestTest14 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        ClobTypeHandler clobTypeHandler0 = new ClobTypeHandler();
        JdbcType jdbcType0 = JdbcType.DATALINK;
        PreparedStatement preparedStatement0 = mock(PreparedStatement.class, new ViolatedAssumptionAnswer());
        clobTypeHandler0.setParameter(preparedStatement0, (-1), (String) null, jdbcType0);
    }
}

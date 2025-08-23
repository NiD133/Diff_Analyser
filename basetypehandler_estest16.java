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

public class BaseTypeHandler_ESTestTest16 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        StringTypeHandler stringTypeHandler0 = new StringTypeHandler();
        CallableStatement callableStatement0 = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn(" Q9@^x<<xBG*k").when(callableStatement0).getString(anyInt());
        String string0 = stringTypeHandler0.getResult(callableStatement0, 0);
        assertEquals(" Q9@^x<<xBG*k", string0);
    }
}

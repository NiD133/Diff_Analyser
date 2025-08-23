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

public class BaseTypeHandler_ESTestTest3 extends BaseTypeHandler_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ObjectTypeHandler objectTypeHandler0 = new ObjectTypeHandler();
        CallableStatement callableStatement0 = mock(CallableStatement.class, new ViolatedAssumptionAnswer());
        doReturn((Object) null).when(callableStatement0).getObject(anyInt());
        Object object0 = objectTypeHandler0.getNullableResult(callableStatement0, 1779);
        assertNull(object0);
    }
}

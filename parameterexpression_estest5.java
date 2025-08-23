package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ParameterExpression_ESTestTest5 extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        ParameterExpression parameterExpression0 = null;
        try {
            parameterExpression0 = new ParameterExpression((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }
}

package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ParameterExpression_ESTestTest12 extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        ParameterExpression parameterExpression0 = null;
        try {
            parameterExpression0 = new ParameterExpression("(;)$");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Parsing error in {(;)$} in position 3
            //
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }
}

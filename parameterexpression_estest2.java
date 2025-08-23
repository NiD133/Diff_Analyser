package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ParameterExpression_ESTestTest2 extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ParameterExpression parameterExpression0 = null;
        try {
            parameterExpression0 = new ParameterExpression("(V5BD)wo7C47");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Parsing error in {(V5BD)wo7C47} in position 6
            //
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }
}

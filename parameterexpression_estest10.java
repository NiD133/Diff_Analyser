package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ParameterExpression_ESTestTest10 extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        ParameterExpression parameterExpression0 = null;
        try {
            parameterExpression0 = new ParameterExpression("( :s>(R5C/J>,K1");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
        }
    }
}

package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ParameterExpression_ESTestTest11 extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ParameterExpression parameterExpression0 = new ParameterExpression("(Vx5:BD)");
        assertEquals(1, parameterExpression0.size());
    }
}

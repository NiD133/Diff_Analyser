package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest17 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.empty();
        boolean boolean0 = jacksonInject_Value0.equals(jacksonInject_Value0);
        assertTrue(boolean0);
    }
}

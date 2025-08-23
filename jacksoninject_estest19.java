package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest19 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.empty();
        JacksonInject.Value jacksonInject_Value1 = JacksonInject.Value.forId(jacksonInject_Value0);
        boolean boolean0 = jacksonInject_Value1.hasId();
        assertTrue(boolean0);
    }
}
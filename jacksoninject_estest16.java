package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest16 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.empty();
        Boolean boolean0 = Boolean.FALSE;
        JacksonInject.Value jacksonInject_Value1 = JacksonInject.Value.construct((Object) jacksonInject_Value0, boolean0, boolean0);
        boolean boolean1 = jacksonInject_Value1.equals(jacksonInject_Value0);
        assertFalse(boolean1);
        assertTrue(jacksonInject_Value1.hasId());
    }
}

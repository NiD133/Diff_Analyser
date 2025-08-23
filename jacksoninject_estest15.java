package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest15 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Object object0 = new Object();
        Boolean boolean0 = new Boolean("");
        JacksonInject.Value jacksonInject_Value0 = new JacksonInject.Value(object0, boolean0, boolean0);
        JacksonInject.Value jacksonInject_Value1 = JacksonInject.Value.construct((Object) "", boolean0, boolean0);
        boolean boolean1 = jacksonInject_Value1.equals(jacksonInject_Value0);
        assertFalse(boolean1);
        assertFalse(jacksonInject_Value1.hasId());
        assertFalse(jacksonInject_Value0.equals((Object) jacksonInject_Value1));
    }
}
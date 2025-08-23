package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest1 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Object object0 = new Object();
        Boolean boolean0 = new Boolean("");
        Boolean boolean1 = Boolean.valueOf(true);
        JacksonInject.Value jacksonInject_Value0 = new JacksonInject.Value(object0, boolean0, boolean1);
        Boolean boolean2 = Boolean.FALSE;
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withOptional(boolean2);
        boolean boolean3 = jacksonInject_Value0.equals(jacksonInject_Value1);
        assertFalse(jacksonInject_Value1.equals((Object) jacksonInject_Value0));
        assertFalse(jacksonInject_Value0.getUseInput());
        assertTrue(jacksonInject_Value1.hasId());
        assertFalse(jacksonInject_Value1.getUseInput());
        assertFalse(boolean3);
    }
}

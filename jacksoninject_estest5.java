package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest5 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Object object0 = new Object();
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.forId(object0);
        Boolean boolean0 = Boolean.FALSE;
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withOptional(boolean0);
        Boolean boolean1 = jacksonInject_Value1.getUseInput();
        assertTrue(jacksonInject_Value1.hasId());
        assertFalse(jacksonInject_Value1.equals((Object) jacksonInject_Value0));
        assertNull(boolean1);
        assertNotSame(jacksonInject_Value1, jacksonInject_Value0);
    }
}
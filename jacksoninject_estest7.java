package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest7 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Object object0 = new Object();
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.forId(object0);
        Boolean boolean0 = Boolean.FALSE;
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withOptional(boolean0);
        JacksonInject.Value jacksonInject_Value2 = jacksonInject_Value1.withUseInput(boolean0);
        assertNotSame(jacksonInject_Value2, jacksonInject_Value1);
        assertFalse(jacksonInject_Value2.equals((Object) jacksonInject_Value1));
        assertFalse(jacksonInject_Value1.equals((Object) jacksonInject_Value0));
        assertNotSame(jacksonInject_Value1, jacksonInject_Value0);
        assertTrue(jacksonInject_Value2.hasId());
    }
}

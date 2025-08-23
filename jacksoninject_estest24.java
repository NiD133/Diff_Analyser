package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest24 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Object object0 = new Object();
        Boolean boolean0 = new Boolean("");
        JacksonInject.Value jacksonInject_Value0 = new JacksonInject.Value(object0, boolean0, boolean0);
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withUseInput(boolean0);
        assertSame(jacksonInject_Value1, jacksonInject_Value0);
        assertTrue(jacksonInject_Value1.hasId());
    }
}

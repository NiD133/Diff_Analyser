package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest4 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Object object0 = new Object();
        Boolean boolean0 = new Boolean(false);
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.construct(object0, boolean0, (Boolean) null);
        Boolean boolean1 = jacksonInject_Value0.getOptional();
        assertTrue(jacksonInject_Value0.hasId());
        assertNull(boolean1);
    }
}

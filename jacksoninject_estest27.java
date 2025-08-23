package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest27 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Object object0 = new Object();
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.forId(object0);
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withId(object0);
        assertSame(jacksonInject_Value1, jacksonInject_Value0);
    }
}

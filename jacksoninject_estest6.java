package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest6 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Boolean boolean0 = new Boolean("gJ%*=b`O<@AxM)o");
        Boolean boolean1 = Boolean.FALSE;
        JacksonInject.Value jacksonInject_Value0 = new JacksonInject.Value((Object) null, boolean0, boolean1);
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withOptional((Boolean) null);
        assertFalse(jacksonInject_Value1.equals((Object) jacksonInject_Value0));
        assertNotSame(jacksonInject_Value1, jacksonInject_Value0);
    }
}

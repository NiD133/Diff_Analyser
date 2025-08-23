package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest11 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Boolean boolean0 = Boolean.TRUE;
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.construct((Object) null, (Boolean) null, boolean0);
        assertFalse(jacksonInject_Value0.hasId());
    }
}

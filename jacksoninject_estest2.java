package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest2 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        JacksonInject.Value jacksonInject_Value0 = JacksonInject.Value.forId((Object) null);
        Boolean boolean0 = Boolean.valueOf(false);
        JacksonInject.Value jacksonInject_Value1 = jacksonInject_Value0.withOptional(boolean0);
        String string0 = jacksonInject_Value1.toString();
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=false)", string0);
    }
}

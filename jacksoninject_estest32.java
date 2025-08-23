package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JacksonInject_ESTestTest32 extends JacksonInject_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        JacksonInject jacksonInject0 = mock(JacksonInject.class, CALLS_REAL_METHODS);
        doReturn((OptBoolean) null).when(jacksonInject0).useInput();
        doReturn((String) null).when(jacksonInject0).value();
        // Undeclared exception!
        try {
            JacksonInject.Value.from(jacksonInject0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.annotation.JacksonInject$Value", e);
        }
    }
}

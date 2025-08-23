package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest14 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        boolean boolean0 = JsonTypeInfo.Value.isEnabled((JsonTypeInfo.Value) null);
        assertFalse(boolean0);
    }
}

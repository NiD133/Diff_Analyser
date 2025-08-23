package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest28 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.empty();
        jsonSetter_Value0.nonDefaultContentNulls();
    }
}

package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest8 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Nulls nulls0 = Nulls.DEFAULT;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.construct(nulls0, nulls0);
        jsonSetter_Value0.nonDefaultValueNulls();
    }
}

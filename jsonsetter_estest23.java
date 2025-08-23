package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest23 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.from((JsonSetter) null);
        assertEquals(Nulls.DEFAULT, jsonSetter_Value0.getValueNulls());
    }
}

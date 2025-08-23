package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest29 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Nulls nulls0 = Nulls.SET;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.construct(nulls0, nulls0);
        Nulls nulls1 = jsonSetter_Value0.getContentNulls();
        assertEquals(Nulls.SET, nulls1);
        assertEquals(Nulls.SET, jsonSetter_Value0.getValueNulls());
    }
}

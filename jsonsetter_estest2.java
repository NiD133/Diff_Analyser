package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest2 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Nulls nulls0 = Nulls.DEFAULT;
        Nulls nulls1 = Nulls.FAIL;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forContentNulls(nulls1);
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.forValueNulls(nulls0, nulls1);
        boolean boolean0 = jsonSetter_Value1.equals(jsonSetter_Value0);
        assertEquals(Nulls.FAIL, jsonSetter_Value1.getContentNulls());
        assertEquals(Nulls.DEFAULT, jsonSetter_Value1.getValueNulls());
        assertTrue(boolean0);
    }
}

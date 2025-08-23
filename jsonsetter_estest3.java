package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest3 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Nulls nulls0 = Nulls.SET;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0);
        Nulls nulls1 = Nulls.SKIP;
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.forValueNulls(nulls1);
        boolean boolean0 = jsonSetter_Value0.equals(jsonSetter_Value1);
        assertEquals(Nulls.DEFAULT, jsonSetter_Value1.getContentNulls());
        assertFalse(boolean0);
    }
}

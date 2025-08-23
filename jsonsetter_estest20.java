package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest20 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Nulls nulls0 = Nulls.FAIL;
        JsonSetter.Value jsonSetter_Value0 = new JsonSetter.Value(nulls0, nulls0);
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.forValueNulls(nulls0);
        JsonSetter.Value jsonSetter_Value2 = JsonSetter.Value.merge(jsonSetter_Value0, jsonSetter_Value1);
        assertEquals(Nulls.FAIL, jsonSetter_Value1.getValueNulls());
        assertSame(jsonSetter_Value2, jsonSetter_Value0);
        assertEquals(Nulls.DEFAULT, jsonSetter_Value1.getContentNulls());
    }
}

package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest18 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Nulls nulls0 = Nulls.AS_EMPTY;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0, nulls0);
        Nulls nulls1 = Nulls.SKIP;
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.construct(nulls0, nulls1);
        JsonSetter.Value jsonSetter_Value2 = JsonSetter.Value.merge(jsonSetter_Value0, jsonSetter_Value1);
        assertNotSame(jsonSetter_Value2, jsonSetter_Value1);
        assertEquals(Nulls.SKIP, jsonSetter_Value2.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value2.getValueNulls());
    }
}

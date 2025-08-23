package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest21 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Nulls nulls0 = Nulls.AS_EMPTY;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0, nulls0);
        Nulls nulls1 = Nulls.DEFAULT;
        JsonSetter.Value jsonSetter_Value1 = jsonSetter_Value0.withValueNulls(nulls1);
        JsonSetter.Value jsonSetter_Value2 = jsonSetter_Value0.withOverrides(jsonSetter_Value1);
        assertEquals(Nulls.DEFAULT, jsonSetter_Value1.getValueNulls());
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value1.getContentNulls());
        assertSame(jsonSetter_Value2, jsonSetter_Value0);
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value2.getValueNulls());
    }
}

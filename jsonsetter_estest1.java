package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest1 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Nulls nulls0 = Nulls.AS_EMPTY;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0, nulls0);
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.merge((JsonSetter.Value) null, jsonSetter_Value0);
        assertNotNull(jsonSetter_Value1);
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value1.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value1.getValueNulls());
    }
}

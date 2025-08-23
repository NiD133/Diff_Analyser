package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest19 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Nulls nulls0 = Nulls.AS_EMPTY;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0, nulls0);
        JsonSetter jsonSetter0 = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn((Nulls) null).when(jsonSetter0).contentNulls();
        doReturn((Nulls) null).when(jsonSetter0).nulls();
        JsonSetter.Value jsonSetter_Value1 = JsonSetter.Value.from(jsonSetter0);
        JsonSetter.Value jsonSetter_Value2 = JsonSetter.Value.merge(jsonSetter_Value1, jsonSetter_Value0);
        assertNotSame(jsonSetter_Value2, jsonSetter_Value0);
        assertTrue(jsonSetter_Value2.equals((Object) jsonSetter_Value0));
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value2.getValueNulls());
    }
}
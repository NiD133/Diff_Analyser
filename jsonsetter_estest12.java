package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest12 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        JsonSetter jsonSetter0 = mock(JsonSetter.class, CALLS_REAL_METHODS);
        doReturn((Nulls) null).when(jsonSetter0).contentNulls();
        doReturn((Nulls) null).when(jsonSetter0).nulls();
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.from(jsonSetter0);
        Nulls nulls0 = Nulls.DEFAULT;
        JsonSetter.Value jsonSetter_Value1 = jsonSetter_Value0.withContentNulls(nulls0);
        assertSame(jsonSetter_Value1, jsonSetter_Value0);
    }
}

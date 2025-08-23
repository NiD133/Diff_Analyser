package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest5 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Nulls nulls0 = Nulls.AS_EMPTY;
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.forValueNulls(nulls0, nulls0);
        boolean boolean0 = jsonSetter_Value0.equals((Object) null);
        assertFalse(boolean0);
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value0.getContentNulls());
        assertEquals(Nulls.AS_EMPTY, jsonSetter_Value0.getValueNulls());
    }
}

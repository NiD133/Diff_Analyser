package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest11 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        JsonSetter.Value jsonSetter_Value0 = JsonSetter.Value.EMPTY;
        JsonSetter.Value jsonSetter_Value1 = jsonSetter_Value0.withContentNulls((Nulls) null);
        assertSame(jsonSetter_Value0, jsonSetter_Value1);
    }
}

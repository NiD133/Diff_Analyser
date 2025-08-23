package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonSetter_ESTestTest31 extends JsonSetter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Nulls nulls0 = Nulls.FAIL;
        JsonSetter.Value jsonSetter_Value0 = new JsonSetter.Value(nulls0, nulls0);
        Nulls nulls1 = jsonSetter_Value0.getValueNulls();
        assertEquals(Nulls.FAIL, nulls1);
    }
}

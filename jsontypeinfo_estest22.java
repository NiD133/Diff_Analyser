package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest22 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        Class<Object> class0 = Object.class;
        JsonTypeInfo.Value jsonTypeInfo_Value1 = jsonTypeInfo_Value0.withDefaultImpl(class0);
        assertFalse(jsonTypeInfo_Value1.equals((Object) jsonTypeInfo_Value0));
        assertNotSame(jsonTypeInfo_Value1, jsonTypeInfo_Value0);
        assertFalse(jsonTypeInfo_Value1.getIdVisible());
    }
}
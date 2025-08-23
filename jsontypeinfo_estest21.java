package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest21 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.Value jsonTypeInfo_Value1 = jsonTypeInfo_Value0.withIdType(jsonTypeInfo_Id0);
        assertEquals(JsonTypeInfo.Id.CLASS, jsonTypeInfo_Value1.getIdType());
        assertFalse(jsonTypeInfo_Value1.getIdVisible());
    }
}

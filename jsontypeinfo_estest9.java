package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest9 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<Object> class0 = Object.class;
        Boolean boolean0 = Boolean.valueOf("@2LLQRbW9{J2*\"1GY");
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "@2LLQRbW9{J2*\"1GY", class0, false, boolean0);
        boolean boolean1 = jsonTypeInfo_Value0.equals(jsonTypeInfo_Value0);
        assertEquals("@2LLQRbW9{J2*\"1GY", jsonTypeInfo_Value0.getPropertyName());
        assertTrue(boolean1);
        assertFalse(jsonTypeInfo_Value0.getIdVisible());
    }
}

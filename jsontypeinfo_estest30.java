package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest30 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.EXISTING_PROPERTY;
        Class<Object> class0 = Object.class;
        Boolean boolean0 = Boolean.valueOf(false);
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "<CLLX)Xg+tvh?s;", class0, true, boolean0);
        jsonTypeInfo_Value0.getIdType();
        assertTrue(jsonTypeInfo_Value0.getIdVisible());
        assertEquals("<CLLX)Xg+tvh?s;", jsonTypeInfo_Value0.getPropertyName());
    }
}

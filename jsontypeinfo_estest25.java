package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest25 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.CLASS;
        Boolean boolean0 = new Boolean("");
        Class<JsonTypeInfo> class0 = jsonTypeInfo_Value0.valueFor();
        JsonTypeInfo.Value jsonTypeInfo_Value1 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "F%n af6M@k!62Ai", class0, true, boolean0);
        assertTrue(jsonTypeInfo_Value1.getIdVisible());
        assertEquals("F%n af6M@k!62Ai", jsonTypeInfo_Value1.getPropertyName());
    }
}

package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest29 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.DEDUCTION;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<Integer> class0 = Integer.class;
        Boolean boolean0 = Boolean.TRUE;
        JsonTypeInfo.Value jsonTypeInfo_Value0 = new JsonTypeInfo.Value(jsonTypeInfo_Id0, jsonTypeInfo_As0, "H:a", class0, true, boolean0);
        String string0 = jsonTypeInfo_Value0.getPropertyName();
        assertTrue(jsonTypeInfo_Value0.getIdVisible());
        assertNotNull(string0);
    }
}

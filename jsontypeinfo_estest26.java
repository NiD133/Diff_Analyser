package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest26 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.NAME;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.PROPERTY;
        Class<Integer> class0 = Integer.class;
        Boolean boolean0 = Boolean.FALSE;
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, (String) null, class0, false, boolean0);
        assertEquals("@type", jsonTypeInfo_Value0.getPropertyName());
        assertFalse(jsonTypeInfo_Value0.getIdVisible());
    }
}

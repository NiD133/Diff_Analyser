package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest6 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.DEDUCTION;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<Object> class0 = Object.class;
        Boolean boolean0 = Boolean.FALSE;
        JsonTypeInfo.Value jsonTypeInfo_Value1 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "", class0, false, boolean0);
        boolean boolean1 = jsonTypeInfo_Value1.equals(jsonTypeInfo_Value0);
        assertFalse(boolean1);
        assertFalse(jsonTypeInfo_Value1.getIdVisible());
    }
}

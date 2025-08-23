package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest27 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<Integer> class0 = Integer.class;
        Boolean boolean0 = Boolean.TRUE;
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "dwi ]|QzT;beNe4DG", class0, false, boolean0);
        Class<?> class1 = jsonTypeInfo_Value0.getDefaultImpl();
        assertNotNull(class1);
        assertFalse(jsonTypeInfo_Value0.getIdVisible());
        assertEquals("dwi ]|QzT;beNe4DG", jsonTypeInfo_Value0.getPropertyName());
    }
}
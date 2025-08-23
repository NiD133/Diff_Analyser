package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonIncludeProperties_ESTestTest8 extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        JsonIncludeProperties.Value jsonIncludeProperties_Value0 = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties jsonIncludeProperties0 = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn((String[]) null).when(jsonIncludeProperties0).value();
        JsonIncludeProperties.Value jsonIncludeProperties_Value1 = JsonIncludeProperties.Value.from(jsonIncludeProperties0);
        JsonIncludeProperties.Value jsonIncludeProperties_Value2 = jsonIncludeProperties_Value1.withOverrides(jsonIncludeProperties_Value0);
        boolean boolean0 = jsonIncludeProperties_Value2.equals(jsonIncludeProperties_Value0);
        assertFalse(boolean0);
        assertSame(jsonIncludeProperties_Value2, jsonIncludeProperties_Value1);
    }
}

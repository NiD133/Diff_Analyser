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

public class JsonIncludeProperties_ESTestTest2 extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        String[] stringArray0 = new String[5];
        JsonIncludeProperties jsonIncludeProperties0 = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(stringArray0).when(jsonIncludeProperties0).value();
        JsonIncludeProperties.Value jsonIncludeProperties_Value0 = JsonIncludeProperties.Value.from(jsonIncludeProperties0);
        assertNotNull(jsonIncludeProperties_Value0);
    }
}

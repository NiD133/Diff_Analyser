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

public class JsonIncludeProperties_ESTestTest15 extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        JsonIncludeProperties.Value jsonIncludeProperties_Value0 = JsonIncludeProperties.Value.ALL;
        String string0 = jsonIncludeProperties_Value0.toString();
        assertEquals("JsonIncludeProperties.Value(included=null)", string0);
    }
}

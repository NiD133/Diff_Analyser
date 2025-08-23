package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest5 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value jsonTypeInfo_Value1 = jsonTypeInfo_Value0.withIdVisible(true);
        boolean boolean0 = jsonTypeInfo_Value1.equals(jsonTypeInfo_Value0);
        assertFalse(boolean0);
        assertFalse(jsonTypeInfo_Value0.equals((Object) jsonTypeInfo_Value1));
    }
}

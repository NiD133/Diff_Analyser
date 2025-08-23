package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonIgnoreProperties_ESTestTest61 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        JsonIgnoreProperties jsonIgnoreProperties0 = mock(JsonIgnoreProperties.class, CALLS_REAL_METHODS);
        doReturn(false).when(jsonIgnoreProperties0).allowGetters();
        doReturn(false).when(jsonIgnoreProperties0).allowSetters();
        doReturn(false).when(jsonIgnoreProperties0).ignoreUnknown();
        doReturn((String[]) null).when(jsonIgnoreProperties0).value();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.from(jsonIgnoreProperties0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withoutMerge();
        assertFalse(jsonIgnoreProperties_Value0.getMerge());
        assertFalse(jsonIgnoreProperties_Value1.getAllowSetters());
        assertSame(jsonIgnoreProperties_Value1, jsonIgnoreProperties_Value0);
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
    }
}

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

public class JsonIgnoreProperties_ESTestTest51 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withoutIgnoreUnknown();
        assertNotSame(jsonIgnoreProperties_Value1, jsonIgnoreProperties_Value0);
        assertFalse(jsonIgnoreProperties_Value0.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value0.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
        assertFalse(jsonIgnoreProperties_Value1.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value0.getMerge());
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
    }
}

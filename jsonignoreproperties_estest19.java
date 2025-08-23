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

public class JsonIgnoreProperties_ESTestTest19 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.EMPTY;
        Set<String> set0 = jsonIgnoreProperties_Value0.findIgnoredForSerialization();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.construct(set0, false, true, true, true);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_Value1.withoutAllowGetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value3 = jsonIgnoreProperties_Value2.withoutAllowGetters();
        assertNotSame(jsonIgnoreProperties_Value3, jsonIgnoreProperties_Value1);
        assertSame(jsonIgnoreProperties_Value3, jsonIgnoreProperties_Value2);
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value3.getIgnoreUnknown());
        assertTrue(jsonIgnoreProperties_Value3.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value3.getMerge());
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
    }
}

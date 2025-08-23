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

public class JsonIgnoreProperties_ESTestTest47 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        String[] stringArray0 = new String[1];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withAllowSetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_Value1.withoutAllowSetters();
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertNotSame(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value1);
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value2.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value2.getMerge());
        assertFalse(jsonIgnoreProperties_Value2.getAllowSetters());
        assertNotSame(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value0);
        assertFalse(jsonIgnoreProperties_Value2.getIgnoreUnknown());
    }
}

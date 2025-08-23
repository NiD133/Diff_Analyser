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

public class JsonIgnoreProperties_ESTestTest41 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        String[] stringArray0 = new String[1];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withAllowSetters();
        Set<String> set0 = jsonIgnoreProperties_Value1.findIgnoredForDeserialization();
        assertFalse(jsonIgnoreProperties_Value0.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value0.getIgnoreUnknown());
        assertNotSame(jsonIgnoreProperties_Value1, jsonIgnoreProperties_Value0);
        assertEquals(0, set0.size());
        assertTrue(jsonIgnoreProperties_Value0.getMerge());
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
    }
}

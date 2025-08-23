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

public class JsonIgnoreProperties_ESTestTest14 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        JsonIgnoreProperties.Value[] jsonIgnoreProperties_ValueArray0 = new JsonIgnoreProperties.Value[5];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoreUnknown(false);
        assertFalse(jsonIgnoreProperties_Value0.getIgnoreUnknown());
        jsonIgnoreProperties_ValueArray0[2] = jsonIgnoreProperties_Value0;
        Set<String> set0 = jsonIgnoreProperties_ValueArray0[2].findIgnoredForSerialization();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.construct(set0, true, false, true, false);
        assertTrue(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value1.getAllowSetters());
        jsonIgnoreProperties_ValueArray0[4] = jsonIgnoreProperties_Value1;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_ValueArray0[4].withMerge();
        assertNotSame(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value1);
        assertTrue(jsonIgnoreProperties_Value2.getAllowSetters());
        assertFalse(jsonIgnoreProperties_Value2.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value2.getMerge());
        assertTrue(jsonIgnoreProperties_Value2.getIgnoreUnknown());
    }
}

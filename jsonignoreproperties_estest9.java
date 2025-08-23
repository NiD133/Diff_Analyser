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

public class JsonIgnoreProperties_ESTestTest9 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.EMPTY;
        Set<String> set0 = jsonIgnoreProperties_Value0.findIgnoredForSerialization();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.construct(set0, false, true, true, true);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_Value1.withoutAllowGetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value3 = (JsonIgnoreProperties.Value) jsonIgnoreProperties_Value2.readResolve();
        assertTrue(jsonIgnoreProperties_Value3.getMerge());
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertTrue(jsonIgnoreProperties_Value3.getAllowSetters());
        assertNotSame(jsonIgnoreProperties_Value3, jsonIgnoreProperties_Value1);
        assertFalse(jsonIgnoreProperties_Value3.equals((Object) jsonIgnoreProperties_Value1));
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
        assertFalse(jsonIgnoreProperties_Value3.getIgnoreUnknown());
    }
}

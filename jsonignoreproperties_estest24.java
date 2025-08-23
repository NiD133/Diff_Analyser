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

public class JsonIgnoreProperties_ESTestTest24 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.EMPTY;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withAllowGetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_Value1.withIgnoreUnknown();
        assertTrue(jsonIgnoreProperties_Value2.getMerge());
        assertFalse(jsonIgnoreProperties_Value2.getAllowSetters());
        assertFalse(jsonIgnoreProperties_Value1.getAllowSetters());
        assertNotSame(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value1);
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
        assertFalse(jsonIgnoreProperties_Value2.equals((Object) jsonIgnoreProperties_Value1));
        assertTrue(jsonIgnoreProperties_Value2.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value1.getAllowGetters());
    }
}

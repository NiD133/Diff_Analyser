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

public class JsonIgnoreProperties_ESTestTest30 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        String[] stringArray0 = new String[0];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withAllowSetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = JsonIgnoreProperties.Value.merge(jsonIgnoreProperties_Value0, jsonIgnoreProperties_Value1);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value3 = JsonIgnoreProperties.Value.merge(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value1);
        assertFalse(jsonIgnoreProperties_Value3.getIgnoreUnknown());
        assertTrue(jsonIgnoreProperties_Value2.equals((Object) jsonIgnoreProperties_Value1));
        assertSame(jsonIgnoreProperties_Value3, jsonIgnoreProperties_Value2);
        assertTrue(jsonIgnoreProperties_Value3.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value3.getMerge());
        assertFalse(jsonIgnoreProperties_Value3.getAllowGetters());
    }
}

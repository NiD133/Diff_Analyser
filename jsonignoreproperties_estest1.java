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

public class JsonIgnoreProperties_ESTestTest1 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        String[] stringArray0 = new String[1];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = jsonIgnoreProperties_Value0.withAllowSetters();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = jsonIgnoreProperties_Value0.withOverrides(jsonIgnoreProperties_Value1);
        assertFalse(jsonIgnoreProperties_Value2.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value2.getAllowGetters());
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value3 = jsonIgnoreProperties_Value0.withoutMerge();
        assertFalse(jsonIgnoreProperties_Value3.getMerge());
        assertFalse(jsonIgnoreProperties_Value3.getAllowSetters());
        JsonIgnoreProperties.Value[] jsonIgnoreProperties_ValueArray0 = new JsonIgnoreProperties.Value[2];
        jsonIgnoreProperties_ValueArray0[0] = jsonIgnoreProperties_Value3;
        jsonIgnoreProperties_ValueArray0[1] = jsonIgnoreProperties_Value2;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value4 = JsonIgnoreProperties.Value.mergeAll(jsonIgnoreProperties_ValueArray0);
        assertNotSame(jsonIgnoreProperties_Value4, jsonIgnoreProperties_Value1);
        assertTrue(jsonIgnoreProperties_Value4.equals((Object) jsonIgnoreProperties_Value1));
        assertTrue(jsonIgnoreProperties_Value4.getAllowSetters());
    }
}

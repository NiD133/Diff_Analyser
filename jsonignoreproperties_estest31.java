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

public class JsonIgnoreProperties_ESTestTest31 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        String[] stringArray0 = new String[5];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.merge((JsonIgnoreProperties.Value) null, jsonIgnoreProperties_Value0);
        assertFalse(jsonIgnoreProperties_Value1.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertFalse(jsonIgnoreProperties_Value1.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
        assertNotNull(jsonIgnoreProperties_Value1);
        assertSame(jsonIgnoreProperties_Value1, jsonIgnoreProperties_Value0);
    }
}

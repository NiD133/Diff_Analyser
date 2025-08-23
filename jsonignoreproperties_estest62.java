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

public class JsonIgnoreProperties_ESTestTest62 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties((Set<String>) linkedHashSet0);
        jsonIgnoreProperties_Value0.valueFor();
        assertFalse(jsonIgnoreProperties_Value0.getAllowSetters());
        assertFalse(jsonIgnoreProperties_Value0.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value0.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value0.getMerge());
    }
}

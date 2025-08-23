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

public class JsonIgnoreProperties_ESTestTest57 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        JsonIgnoreProperties.Value[] jsonIgnoreProperties_ValueArray0 = new JsonIgnoreProperties.Value[2];
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties((Set<String>) linkedHashSet0);
        assertFalse(jsonIgnoreProperties_Value0.getAllowGetters());
        jsonIgnoreProperties_ValueArray0[0] = jsonIgnoreProperties_Value0;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.construct(linkedHashSet0, false, false, false, false);
        jsonIgnoreProperties_ValueArray0[1] = jsonIgnoreProperties_Value1;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = JsonIgnoreProperties.Value.mergeAll(jsonIgnoreProperties_ValueArray0);
        assertFalse(jsonIgnoreProperties_Value2.getAllowSetters());
        assertFalse(jsonIgnoreProperties_Value2.getIgnoreUnknown());
        assertFalse(jsonIgnoreProperties_Value2.getAllowGetters());
        assertNotNull(jsonIgnoreProperties_Value2);
        assertFalse(jsonIgnoreProperties_Value2.getMerge());
    }
}

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

public class JsonIgnoreProperties_ESTestTest34 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        JsonIgnoreProperties.Value[] jsonIgnoreProperties_ValueArray0 = new JsonIgnoreProperties.Value[8];
        String[] stringArray0 = new String[0];
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.forIgnoredProperties(stringArray0);
        jsonIgnoreProperties_ValueArray0[0] = jsonIgnoreProperties_Value0;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        assertFalse(jsonIgnoreProperties_Value1.getAllowSetters());
        assertFalse(jsonIgnoreProperties_Value1.getAllowGetters());
        assertTrue(jsonIgnoreProperties_Value1.getMerge());
        jsonIgnoreProperties_ValueArray0[2] = jsonIgnoreProperties_Value1;
        boolean boolean0 = jsonIgnoreProperties_ValueArray0[2].equals(jsonIgnoreProperties_ValueArray0[0]);
        assertFalse(boolean0);
    }
}

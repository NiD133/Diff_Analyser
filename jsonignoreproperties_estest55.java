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

public class JsonIgnoreProperties_ESTestTest55 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.EMPTY;
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        JsonIgnoreProperties.Value[] jsonIgnoreProperties_ValueArray0 = new JsonIgnoreProperties.Value[8];
        jsonIgnoreProperties_ValueArray0[0] = jsonIgnoreProperties_Value0;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = new JsonIgnoreProperties.Value(linkedHashSet0, false, true, true, true);
        jsonIgnoreProperties_ValueArray0[5] = jsonIgnoreProperties_Value1;
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value2 = JsonIgnoreProperties.Value.mergeAll(jsonIgnoreProperties_ValueArray0);
        assertTrue(jsonIgnoreProperties_Value2.getAllowSetters());
        assertTrue(jsonIgnoreProperties_Value2.equals((Object) jsonIgnoreProperties_Value1));
        assertFalse(jsonIgnoreProperties_Value2.getIgnoreUnknown());
        assertTrue(jsonIgnoreProperties_Value2.getAllowGetters());
        assertNotSame(jsonIgnoreProperties_Value2, jsonIgnoreProperties_Value1);
        assertNotNull(jsonIgnoreProperties_Value2);
    }
}

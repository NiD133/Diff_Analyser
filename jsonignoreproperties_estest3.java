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

public class JsonIgnoreProperties_ESTestTest3 extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value0 = JsonIgnoreProperties.Value.EMPTY;
        Set<String> set0 = jsonIgnoreProperties_Value0.findIgnoredForSerialization();
        JsonIgnoreProperties.Value jsonIgnoreProperties_Value1 = JsonIgnoreProperties.Value.construct(set0, false, true, false, true);
        String string0 = jsonIgnoreProperties_Value1.toString();
        assertEquals("JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=true,allowSetters=false,merge=true)", string0);
    }
}

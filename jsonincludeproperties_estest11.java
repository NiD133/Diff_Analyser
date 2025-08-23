package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonIncludeProperties_ESTestTest11 extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        linkedHashSet0.add("LI }f.Ax<09fr");
        JsonIncludeProperties.Value jsonIncludeProperties_Value0 = new JsonIncludeProperties.Value(linkedHashSet0);
        JsonIncludeProperties.Value jsonIncludeProperties_Value1 = jsonIncludeProperties_Value0.withOverrides(jsonIncludeProperties_Value0);
        assertTrue(jsonIncludeProperties_Value1.equals((Object) jsonIncludeProperties_Value0));
        assertNotSame(jsonIncludeProperties_Value1, jsonIncludeProperties_Value0);
    }
}

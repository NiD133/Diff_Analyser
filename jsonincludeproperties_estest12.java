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

public class JsonIncludeProperties_ESTestTest12 extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        String[] stringArray0 = new String[0];
        JsonIncludeProperties jsonIncludeProperties0 = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(stringArray0).when(jsonIncludeProperties0).value();
        JsonIncludeProperties.Value jsonIncludeProperties_Value0 = JsonIncludeProperties.Value.from(jsonIncludeProperties0);
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        linkedHashSet0.add("K|D{$!:2zda");
        JsonIncludeProperties.Value jsonIncludeProperties_Value1 = new JsonIncludeProperties.Value(linkedHashSet0);
        JsonIncludeProperties.Value jsonIncludeProperties_Value2 = jsonIncludeProperties_Value0.withOverrides(jsonIncludeProperties_Value1);
        assertNotSame(jsonIncludeProperties_Value2, jsonIncludeProperties_Value0);
        assertTrue(jsonIncludeProperties_Value2.equals((Object) jsonIncludeProperties_Value0));
    }
}

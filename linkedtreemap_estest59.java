package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class LinkedTreeMap_ESTestTest59 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer>((Comparator<? super LinkedTreeMap<Integer, Integer>>) null, false);
        LinkedTreeMap<Integer, Integer> linkedTreeMap1 = new LinkedTreeMap<Integer, Integer>(false);
        Integer integer0 = new Integer(529);
        // Undeclared exception!
        try {
            linkedTreeMap0.put(linkedTreeMap1, integer0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // com.google.gson.internal.LinkedTreeMap is not Comparable
            //
            verifyException("com.google.gson.internal.LinkedTreeMap", e);
        }
    }
}

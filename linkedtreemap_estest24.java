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

public class LinkedTreeMap_ESTestTest24 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        LinkedTreeMap<Object, Integer> linkedTreeMap1 = new LinkedTreeMap<Object, Integer>();
        // Undeclared exception!
        try {
            linkedTreeMap1.find(linkedTreeMap0, true);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // com.google.gson.internal.LinkedTreeMap is not Comparable
            //
            verifyException("com.google.gson.internal.LinkedTreeMap", e);
        }
    }
}
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

public class LinkedTreeMap_ESTestTest23 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Object>> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Object>>();
        LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node0 = new LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Object>>(true);
        linkedTreeMap0.root = linkedTreeMap_Node0;
        LinkedTreeMap<Integer, Integer> linkedTreeMap1 = new LinkedTreeMap<Integer, Integer>();
        // Undeclared exception!
        try {
            linkedTreeMap0.find(linkedTreeMap1, true);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // com.google.gson.internal.LinkedTreeMap cannot be cast to java.lang.Comparable
            //
            verifyException("com.google.gson.internal.LinkedTreeMap", e);
        }
    }
}

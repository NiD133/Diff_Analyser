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

public class LinkedTreeMap_ESTestTest52 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer>();
        LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap_Node0 = new LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, Integer>(true);
        LinkedTreeMap<Integer, Integer> linkedTreeMap1 = new LinkedTreeMap<Integer, Integer>();
        LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, Integer>(true, linkedTreeMap_Node0, linkedTreeMap1, linkedTreeMap_Node0, linkedTreeMap_Node0);
        // Undeclared exception!
        try {
            linkedTreeMap0.removeInternal(linkedTreeMap_Node1, true);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

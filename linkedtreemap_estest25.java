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

public class LinkedTreeMap_ESTestTest25 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Integer, Integer>(true);
        Integer integer0 = new Integer(1995);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Integer, Integer>(true, linkedTreeMap_Node0, integer0, linkedTreeMap_Node0, linkedTreeMap_Node0);
        linkedTreeMap0.root = linkedTreeMap_Node1;
        Integer integer1 = new Integer((-1170));
        linkedTreeMap0.putIfAbsent(integer1, integer1);
        Integer integer2 = new Integer((-985));
        // Undeclared exception!
        try {
            linkedTreeMap0.find(integer2, true);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

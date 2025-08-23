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

public class LinkedTreeMap_ESTestTest18 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        Integer integer0 = new Integer(1);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = linkedTreeMap0.find(integer0, true);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Integer, Integer>(true, linkedTreeMap_Node0, (Integer) null, linkedTreeMap_Node0, linkedTreeMap_Node0);
        linkedTreeMap0.root = linkedTreeMap_Node1;
        // Undeclared exception!
        try {
            linkedTreeMap0.remove((Object) integer0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
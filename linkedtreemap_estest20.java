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

public class LinkedTreeMap_ESTestTest20 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        LinkedTreeMap<Object, Object> linkedTreeMap0 = new LinkedTreeMap<Object, Object>();
        LinkedTreeMap.Node<Object, Object> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Object, Object>(false);
        LinkedTreeMap.Node<Object, Object> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Object, Object>(false, linkedTreeMap_Node0, linkedTreeMap0, linkedTreeMap_Node0, linkedTreeMap_Node0);
        LinkedTreeMap.Node<Object, Object> linkedTreeMap_Node2 = linkedTreeMap_Node1.first();
        linkedTreeMap0.root = linkedTreeMap_Node2;
        Integer integer0 = new Integer((-1));
        // Undeclared exception!
        try {
            linkedTreeMap0.put(integer0, integer0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

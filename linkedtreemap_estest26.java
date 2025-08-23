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

public class LinkedTreeMap_ESTestTest26 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node0 = new LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>>(true);
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node1 = new LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>>(true);
        linkedTreeMap_Node0.left = linkedTreeMap_Node1;
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node2 = linkedTreeMap_Node0.first();
        assertNotNull(linkedTreeMap_Node2);
    }
}

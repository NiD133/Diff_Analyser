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

public class LinkedTreeMap_ESTestTest40 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node0 = new LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>>(false);
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> linkedTreeMap_Node1 = new LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>>(false);
        linkedTreeMap_Node0.next = linkedTreeMap_Node1;
        LinkedTreeMap<Integer, Object> linkedTreeMap0 = new LinkedTreeMap<Integer, Object>();
        LinkedTreeMap<Integer, Object> linkedTreeMap1 = linkedTreeMap_Node0.next.setValue(linkedTreeMap0);
        assertEquals(0, linkedTreeMap0.size());
        assertNull(linkedTreeMap1);
        boolean boolean0 = linkedTreeMap_Node1.equals(linkedTreeMap_Node0);
        assertFalse(boolean0);
    }
}

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

public class LinkedTreeMap_ESTestTest12 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        LinkedTreeMap<Object, LinkedTreeMap<Integer, Integer>> linkedTreeMap0 = new LinkedTreeMap<Object, LinkedTreeMap<Integer, Integer>>(false);
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        LinkedTreeMap<Map.Entry<Integer, Integer>, Object> linkedTreeMap1 = new LinkedTreeMap<Map.Entry<Integer, Integer>, Object>(comparator0, true);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Integer, Integer>(false);
        Integer integer0 = new Integer(1);
        linkedTreeMap_Node0.value = integer0;
        linkedTreeMap1.put(linkedTreeMap_Node0, linkedTreeMap0);
        linkedTreeMap1.get(linkedTreeMap_Node0.value);
        assertEquals(1, linkedTreeMap1.size());
    }
}

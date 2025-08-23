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

public class LinkedTreeMap_ESTestTest34 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Integer integer0 = new Integer((-2139));
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.putIfAbsent(integer0, integer0);
        LinkedTreeMap.EntrySet linkedTreeMap_EntrySet0 = linkedTreeMap0.new EntrySet();
        LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object>(false);
        AbstractMap.SimpleEntry<Integer, Integer> abstractMap_SimpleEntry0 = new AbstractMap.SimpleEntry<Integer, Integer>(integer0, integer0);
        LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object>(false, linkedTreeMap_Node0, abstractMap_SimpleEntry0, linkedTreeMap_Node0, linkedTreeMap_Node0);
        Object object0 = linkedTreeMap_Node1.getKey();
        boolean boolean0 = linkedTreeMap_EntrySet0.contains(object0);
        assertEquals(1, linkedTreeMap0.size());
        assertTrue(boolean0);
    }
}

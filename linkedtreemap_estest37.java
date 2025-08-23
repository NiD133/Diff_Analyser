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

public class LinkedTreeMap_ESTestTest37 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        Integer integer0 = new Integer((-1));
        Integer integer1 = Integer.valueOf(1);
        Integer integer2 = new Integer((-1));
        linkedTreeMap0.putIfAbsent(integer2, integer2);
        Integer integer3 = linkedTreeMap0.putIfAbsent(integer1, integer1);
        assertNull(integer3);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = linkedTreeMap0.find(integer0, true);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node1 = linkedTreeMap_Node0.last();
        assertNotSame(linkedTreeMap_Node1, linkedTreeMap_Node0);
        assertNotNull(linkedTreeMap_Node1);
    }
}

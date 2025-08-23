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

public class LinkedTreeMap_ESTestTest44 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        Integer integer0 = new Integer((-2139));
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = linkedTreeMap0.find(integer0, true);
        LinkedTreeMap.Node<Object, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Object, Integer>(true);
        boolean boolean0 = linkedTreeMap_Node1.equals(linkedTreeMap_Node0);
        assertEquals(1, linkedTreeMap0.size());
        assertFalse(boolean0);
    }
}

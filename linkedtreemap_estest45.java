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

public class LinkedTreeMap_ESTestTest45 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        LinkedTreeMap<Integer, Object> linkedTreeMap0 = new LinkedTreeMap<Integer, Object>();
        Integer integer0 = new Integer((-2139));
        LinkedTreeMap<Integer, Integer> linkedTreeMap1 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap1.putIfAbsent(integer0, integer0);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = linkedTreeMap1.findByObject(integer0);
        boolean boolean0 = linkedTreeMap_Node0.equals(linkedTreeMap0);
        assertEquals(1, linkedTreeMap1.size());
        assertFalse(boolean0);
    }
}

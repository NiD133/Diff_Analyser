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

public class LinkedTreeMap_ESTestTest54 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        Integer integer0 = new Integer(2017);
        BiFunction<Integer, Object, Integer> biFunction0 = (BiFunction<Integer, Object, Integer>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
        linkedTreeMap0.merge(integer0, integer0, biFunction0);
        Integer integer1 = new Integer((-1102));
        linkedTreeMap0.putIfAbsent(integer1, integer1);
        Integer integer2 = new Integer((-968));
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = linkedTreeMap0.find(integer2, true);
        Integer integer3 = new Integer((-1));
        linkedTreeMap0.find(integer3, true);
        linkedTreeMap0.removeInternal(linkedTreeMap_Node0, true);
        assertEquals(3, linkedTreeMap0.size());
    }
}
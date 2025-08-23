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

public class LinkedTreeMap_ESTestTest39 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Integer, Integer>(false);
        Integer integer0 = Integer.valueOf((-2));
        linkedTreeMap_Node0.value = integer0;
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Integer, Integer>(true);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node2 = new LinkedTreeMap.Node<Integer, Integer>(true, linkedTreeMap_Node0, (Integer) null, linkedTreeMap_Node1, linkedTreeMap_Node0);
        boolean boolean0 = linkedTreeMap_Node2.equals(linkedTreeMap_Node0);
        assertFalse(boolean0);
    }
}

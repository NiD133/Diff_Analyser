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

public class LinkedTreeMap_ESTestTest41 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        Integer integer0 = new Integer((-1));
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Integer, Integer>(true);
        LinkedTreeMap.Node<Integer, Integer> linkedTreeMap_Node1 = new LinkedTreeMap.Node<Integer, Integer>(true, linkedTreeMap_Node0, integer0, linkedTreeMap_Node0, linkedTreeMap_Node0);
        boolean boolean0 = linkedTreeMap_Node1.equals(linkedTreeMap_Node0);
        assertFalse(boolean0);
    }
}

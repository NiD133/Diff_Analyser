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

public class LinkedTreeMap_ESTestTest57 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Integer>> linkedTreeMap_Node0 = new LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, LinkedTreeMap<Integer, Integer>>(false);
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer>((Comparator<? super LinkedTreeMap<Integer, Integer>>) null, false);
        linkedTreeMap0.findByEntry(linkedTreeMap_Node0);
        assertEquals(0, linkedTreeMap0.size());
    }
}

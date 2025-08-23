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

public class LinkedTreeMap_ESTestTest11 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Object, Map.Entry<Object, Integer>> linkedTreeMap0 = new LinkedTreeMap<Object, Map.Entry<Object, Integer>>(comparator0, true);
        LinkedTreeMap.Node<Object, Map.Entry<Object, Integer>> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Object, Map.Entry<Object, Integer>>(true);
        linkedTreeMap0.removeInternal(linkedTreeMap_Node0, true);
        linkedTreeMap0.keySet();
        assertEquals((-1), linkedTreeMap0.size());
    }
}

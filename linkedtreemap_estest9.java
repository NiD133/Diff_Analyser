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

public class LinkedTreeMap_ESTestTest9 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        LinkedTreeMap<LinkedTreeMap<Integer, Object>, Object> linkedTreeMap1 = new LinkedTreeMap<LinkedTreeMap<Integer, Object>, Object>(comparator0, false);
        LinkedTreeMap<Integer, Object> linkedTreeMap2 = new LinkedTreeMap<Integer, Object>();
        Comparator<Object> comparator1 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Object, Integer> linkedTreeMap3 = new LinkedTreeMap<Object, Integer>(comparator1, true);
        BiFunction<Object, Object, Integer> biFunction0 = (BiFunction<Object, Object, Integer>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
        linkedTreeMap1.merge(linkedTreeMap2, linkedTreeMap3, biFunction0);
        linkedTreeMap1.put(linkedTreeMap2, linkedTreeMap0);
        assertEquals(1, linkedTreeMap1.size());
    }
}

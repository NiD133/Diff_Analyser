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

public class LinkedTreeMap_ESTestTest7 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Object> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Object>(comparator0, true);
        Comparator<Object> comparator1 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Integer, Integer> linkedTreeMap1 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.put(linkedTreeMap1, linkedTreeMap1);
        LinkedTreeMap<Object, Integer> linkedTreeMap2 = new LinkedTreeMap<Object, Integer>(comparator1, true);
        linkedTreeMap0.removeInternalByKey(linkedTreeMap2);
        assertEquals(0, linkedTreeMap0.size());
    }
}

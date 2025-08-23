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

public class LinkedTreeMap_ESTestTest75 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test74() throws Throwable {
        Comparator<LinkedTreeMap<Integer, Integer>> comparator0 = (Comparator<LinkedTreeMap<Integer, Integer>>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer>(comparator0, true);
        LinkedTreeMap.EntrySet linkedTreeMap_EntrySet0 = linkedTreeMap0.new EntrySet();
        int int0 = linkedTreeMap_EntrySet0.size();
        assertEquals(0, int0);
    }
}

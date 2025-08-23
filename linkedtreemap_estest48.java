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

public class LinkedTreeMap_ESTestTest48 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.entrySet();
        Set<Map.Entry<Integer, Integer>> set0 = (Set<Map.Entry<Integer, Integer>>) linkedTreeMap0.entrySet();
        assertEquals(0, set0.size());
    }
}

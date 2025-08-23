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

public class LinkedTreeMap_ESTestTest47 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.keySet();
        Set<Integer> set0 = linkedTreeMap0.keySet();
        assertTrue(set0.isEmpty());
        assertNotNull(set0);
    }
}

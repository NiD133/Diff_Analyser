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

public class LinkedTreeMap_ESTestTest50 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        Integer integer0 = new Integer(21);
        Integer integer1 = new Integer(0);
        linkedTreeMap0.put(integer0, integer0);
        linkedTreeMap0.putIfAbsent(integer1, integer1);
        Integer integer2 = new Integer((-16));
        Integer integer3 = linkedTreeMap0.putIfAbsent(integer2, (Integer) null);
        assertNull(integer3);
    }
}

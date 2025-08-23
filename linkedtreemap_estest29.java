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

public class LinkedTreeMap_ESTestTest29 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Integer integer0 = new Integer((-2139));
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.putIfAbsent(integer0, integer0);
        LinkedTreeMap.KeySet linkedTreeMap_KeySet0 = linkedTreeMap0.new KeySet();
        boolean boolean0 = linkedTreeMap_KeySet0.remove(integer0);
        assertEquals(0, linkedTreeMap_KeySet0.size());
        assertTrue(boolean0);
    }
}

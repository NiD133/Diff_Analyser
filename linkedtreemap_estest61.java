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

public class LinkedTreeMap_ESTestTest61 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        Integer integer0 = new Integer((-2139));
        LinkedTreeMap<Integer, Integer> linkedTreeMap0 = new LinkedTreeMap<Integer, Integer>();
        linkedTreeMap0.putIfAbsent(integer0, integer0);
        Integer integer1 = new Integer((-36));
        linkedTreeMap0.putIfAbsent(integer1, (Integer) null);
        Integer integer2 = new Integer(1);
        linkedTreeMap0.putIfAbsent(integer2, integer0);
        assertEquals(3, linkedTreeMap0.size());
    }
}

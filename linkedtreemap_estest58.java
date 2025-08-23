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

public class LinkedTreeMap_ESTestTest58 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test57() throws Throwable {
        Comparator<LinkedTreeMap<Integer, Integer>> comparator0 = (Comparator<LinkedTreeMap<Integer, Integer>>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> linkedTreeMap0 = new LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer>(comparator0, false);
        linkedTreeMap0.get((Object) null);
        assertEquals(0, linkedTreeMap0.size());
    }
}

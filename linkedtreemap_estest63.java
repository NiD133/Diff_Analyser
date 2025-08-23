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

public class LinkedTreeMap_ESTestTest63 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        LinkedTreeMap<Integer, LinkedTreeMap<Integer, Object>> linkedTreeMap0 = new LinkedTreeMap<Integer, LinkedTreeMap<Integer, Object>>(false);
        Integer integer0 = new Integer(816);
        // Undeclared exception!
        try {
            linkedTreeMap0.put(integer0, (LinkedTreeMap<Integer, Object>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // value == null
            //
            verifyException("com.google.gson.internal.LinkedTreeMap", e);
        }
    }
}

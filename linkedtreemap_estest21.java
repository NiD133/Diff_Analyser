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

public class LinkedTreeMap_ESTestTest21 extends LinkedTreeMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        LinkedTreeMap<Map.Entry<Integer, Object>, Object> linkedTreeMap0 = new LinkedTreeMap<Map.Entry<Integer, Object>, Object>(false);
        LinkedTreeMap.Node<Map.Entry<Integer, Object>, Object> linkedTreeMap_Node0 = new LinkedTreeMap.Node<Map.Entry<Integer, Object>, Object>(false);
        linkedTreeMap0.root = linkedTreeMap_Node0;
        Integer integer0 = new Integer((-2105));
        // Undeclared exception!
        try {
            linkedTreeMap0.get(integer0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

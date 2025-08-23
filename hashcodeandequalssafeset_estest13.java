package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HashCodeAndEqualsSafeSet_ESTestTest13 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        Object[] objectArray0 = new Object[6];
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet1 = HashCodeAndEqualsSafeSet.of(objectArray0);
        boolean boolean0 = hashCodeAndEqualsSafeSet0.containsAll(hashCodeAndEqualsSafeSet1);
        assertFalse(boolean0);
    }
}

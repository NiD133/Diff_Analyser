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

public class HashCodeAndEqualsSafeSet_ESTestTest1 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        LinkedHashSet<Object> linkedHashSet0 = new LinkedHashSet<Object>();
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet1 = HashCodeAndEqualsSafeSet.of((Iterable<Object>) linkedHashSet0);
        boolean boolean0 = hashCodeAndEqualsSafeSet1.equals(hashCodeAndEqualsSafeSet0);
        assertTrue(boolean0);
    }
}

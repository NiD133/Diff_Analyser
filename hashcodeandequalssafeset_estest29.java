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

public class HashCodeAndEqualsSafeSet_ESTestTest29 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper[] hashCodeAndEqualsMockWrapperArray0 = new HashCodeAndEqualsMockWrapper[6];
        HashCodeAndEqualsMockWrapper[] hashCodeAndEqualsMockWrapperArray1 = hashCodeAndEqualsSafeSet0.toArray(hashCodeAndEqualsMockWrapperArray0);
        assertEquals(6, hashCodeAndEqualsMockWrapperArray1.length);
    }
}

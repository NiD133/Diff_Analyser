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

public class HashCodeAndEqualsSafeSet_ESTestTest27 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = new HashCodeAndEqualsSafeSet();
        Object object0 = new Object();
        hashCodeAndEqualsSafeSet0.add(object0);
        HashCodeAndEqualsMockWrapper[] hashCodeAndEqualsMockWrapperArray0 = new HashCodeAndEqualsMockWrapper[4];
        // Undeclared exception!
        try {
            hashCodeAndEqualsSafeSet0.toArray(hashCodeAndEqualsMockWrapperArray0);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            //
            // java.lang.Object
            //
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet", e);
        }
    }
}

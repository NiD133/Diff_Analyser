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

public class HashCodeAndEqualsSafeSet_ESTestTest33 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Object[] objectArray0 = new Object[1];
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet0 = HashCodeAndEqualsSafeSet.of(objectArray0);
        try {
            hashCodeAndEqualsSafeSet0.clone();
            fail("Expecting exception: CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet", e);
        }
    }
}

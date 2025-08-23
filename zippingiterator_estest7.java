package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ZippingIterator_ESTestTest7 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ZippingIterator<Integer> zippingIterator0 = null;
        try {
            zippingIterator0 = new ZippingIterator<Integer>((Iterator<? extends Integer>) null, (Iterator<? extends Integer>) null, (Iterator<? extends Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // iterator
            //
            verifyException("java.util.Objects", e);
        }
    }
}

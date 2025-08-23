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

public class ZippingIterator_ESTestTest11 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Iterator<Integer> iterator0 = linkedList0.iterator();
        ZippingIterator<Integer> zippingIterator0 = new ZippingIterator<Integer>(iterator0, iterator0, iterator0);
        ZippingIterator<Object> zippingIterator1 = new ZippingIterator<Object>(zippingIterator0, iterator0, iterator0);
        // Undeclared exception!
        try {
            zippingIterator1.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // No value can be removed at present
            //
            verifyException("org.apache.commons.collections4.iterators.ZippingIterator", e);
        }
    }
}

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

public class ZippingIterator_ESTestTest13 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Iterator<Integer> iterator0 = linkedList0.iterator();
        ZippingIterator<Object> zippingIterator0 = new ZippingIterator<Object>(iterator0, iterator0, iterator0);
        try {
            zippingIterator0.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.iterators.ZippingIterator", e);
        }
    }
}

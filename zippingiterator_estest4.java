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

public class ZippingIterator_ESTestTest4 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Integer integer0 = new Integer((-550));
        linkedList0.add(integer0);
        Iterator<Integer> iterator0 = linkedList0.iterator();
        ZippingIterator<Object> zippingIterator0 = new ZippingIterator<Object>(iterator0, iterator0, iterator0);
        Object object0 = zippingIterator0.next();
        linkedList0.removeLastOccurrence(object0);
        // Undeclared exception!
        try {
            zippingIterator0.remove();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}

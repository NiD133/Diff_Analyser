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

public class ZippingIterator_ESTestTest5 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        LinkedList<InstanceofPredicate> linkedList0 = new LinkedList<InstanceofPredicate>();
        ListIterator<InstanceofPredicate> listIterator0 = linkedList0.listIterator();
        linkedList0.add((InstanceofPredicate) null);
        ZippingIterator<InstanceofPredicate> zippingIterator0 = new ZippingIterator<InstanceofPredicate>(listIterator0, listIterator0, listIterator0);
        // Undeclared exception!
        try {
            zippingIterator0.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}

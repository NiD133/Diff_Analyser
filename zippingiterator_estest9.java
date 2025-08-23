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

public class ZippingIterator_ESTestTest9 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        LinkedList<InstanceofPredicate> linkedList0 = new LinkedList<InstanceofPredicate>();
        ListIterator<InstanceofPredicate> listIterator0 = linkedList0.listIterator();
        Class<Integer> class0 = Integer.class;
        InstanceofPredicate instanceofPredicate0 = new InstanceofPredicate(class0);
        linkedList0.add(instanceofPredicate0);
        ZippingIterator<InstanceofPredicate> zippingIterator0 = new ZippingIterator<InstanceofPredicate>(listIterator0, listIterator0);
        zippingIterator0.hasNext();
        boolean boolean0 = zippingIterator0.hasNext();
        assertTrue(boolean0);
    }
}

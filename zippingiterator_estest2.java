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

public class ZippingIterator_ESTestTest2 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.add((Integer) null);
        Iterator<Integer> iterator0 = linkedList0.descendingIterator();
        ZippingIterator<Object> zippingIterator0 = new ZippingIterator<Object>(iterator0, iterator0);
        Object object0 = zippingIterator0.next();
        assertNull(object0);
    }
}

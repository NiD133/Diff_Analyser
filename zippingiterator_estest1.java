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

public class ZippingIterator_ESTestTest1 extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Iterator<Integer> iterator0 = linkedList0.descendingIterator();
        ZippingIterator<Integer> zippingIterator0 = new ZippingIterator<Integer>(iterator0, iterator0);
        Iterator<Integer>[] iteratorArray0 = (Iterator<Integer>[]) Array.newInstance(Iterator.class, 1);
        iteratorArray0[0] = (Iterator<Integer>) zippingIterator0;
        ZippingIterator<Integer> zippingIterator1 = new ZippingIterator<Integer>(iteratorArray0);
        assertFalse(zippingIterator1.equals((Object) zippingIterator0));
    }
}

package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LoopingListIterator_ESTestTest14 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        List<Integer> list0 = linkedList0.subList(0, 0);
        Integer integer0 = new Integer(0);
        linkedList0.add(integer0);
        LoopingListIterator<Integer> loopingListIterator0 = null;
        try {
            loopingListIterator0 = new LoopingListIterator<Integer>(list0);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.SubList", e);
        }
    }
}

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

public class LoopingListIterator_ESTestTest22 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        LinkedList<LinkedList<Object>> linkedList0 = new LinkedList<LinkedList<Object>>();
        LoopingListIterator<LinkedList<Object>> loopingListIterator0 = new LoopingListIterator<LinkedList<Object>>(linkedList0);
        // Undeclared exception!
        try {
            loopingListIterator0.nextIndex();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // There are no elements for this iterator to loop on
            //
            verifyException("org.apache.commons.collections4.iterators.LoopingListIterator", e);
        }
    }
}

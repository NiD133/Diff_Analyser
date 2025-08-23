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

public class LoopingListIterator_ESTestTest31 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        LinkedList<LinkedList<Integer>> linkedList1 = new LinkedList<LinkedList<Integer>>();
        LoopingListIterator<LinkedList<Integer>> loopingListIterator0 = new LoopingListIterator<LinkedList<Integer>>(linkedList1);
        // Undeclared exception!
        try {
            loopingListIterator0.set(linkedList0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}
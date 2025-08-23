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

public class LoopingListIterator_ESTestTest25 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.add((Integer) null);
        LoopingListIterator<Integer> loopingListIterator0 = new LoopingListIterator<Integer>(linkedList0);
        Integer integer0 = loopingListIterator0.next();
        assertNull(integer0);
    }
}

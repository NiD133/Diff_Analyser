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

public class LoopingListIterator_ESTestTest20 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        LinkedList<Closure<Integer>> linkedList0 = new LinkedList<Closure<Integer>>();
        linkedList0.add((Closure<Integer>) null);
        LoopingListIterator<Closure<Integer>> loopingListIterator0 = new LoopingListIterator<Closure<Integer>>(linkedList0);
        Closure<Integer> closure0 = loopingListIterator0.previous();
        assertNull(closure0);
    }
}

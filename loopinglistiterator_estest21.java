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

public class LoopingListIterator_ESTestTest21 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        LinkedList<InstanceofPredicate> linkedList0 = new LinkedList<InstanceofPredicate>();
        LoopingListIterator<InstanceofPredicate> loopingListIterator0 = new LoopingListIterator<InstanceofPredicate>(linkedList0);
        Class<Object> class0 = Object.class;
        InstanceofPredicate instanceofPredicate0 = new InstanceofPredicate(class0);
        loopingListIterator0.add(instanceofPredicate0);
        int int0 = loopingListIterator0.nextIndex();
        assertEquals(0, int0);
    }
}

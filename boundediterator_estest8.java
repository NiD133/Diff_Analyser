package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NOPClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class BoundedIterator_ESTestTest8 extends BoundedIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Closure<Integer> closure0 = NOPClosure.nopClosure();
        Iterator<Closure<Integer>> iterator0 = (Iterator<Closure<Integer>>) mock(Iterator.class, new ViolatedAssumptionAnswer());
        doReturn(closure0).when(iterator0).next();
        BoundedIterator<Closure<Integer>> boundedIterator0 = new BoundedIterator<Closure<Integer>>(iterator0, 0L, 909L);
        boundedIterator0.next();
        boundedIterator0.remove();
    }
}

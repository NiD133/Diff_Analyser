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

public class BoundedIterator_ESTestTest9 extends BoundedIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Iterator<Closure<Integer>> iterator0 = (Iterator<Closure<Integer>>) mock(Iterator.class, new ViolatedAssumptionAnswer());
        doReturn(true, false).when(iterator0).hasNext();
        doReturn((Object) null).when(iterator0).next();
        BoundedIterator<Closure<Integer>> boundedIterator0 = new BoundedIterator<Closure<Integer>>(iterator0, 1L, 5275L);
        BoundedIterator<Closure<Integer>> boundedIterator1 = new BoundedIterator<Closure<Integer>>(boundedIterator0, 5275L, 0L);
        assertFalse(boundedIterator1.equals((Object) boundedIterator0));
    }
}

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

public class BoundedIterator_ESTestTest1 extends BoundedIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Iterator<Predicate<Object>> iterator0 = (Iterator<Predicate<Object>>) mock(Iterator.class, new ViolatedAssumptionAnswer());
        doReturn(true, true, false, false).when(iterator0).hasNext();
        doReturn((Object) null, (Object) null, (Object) null).when(iterator0).next();
        BoundedIterator<Predicate<Object>> boundedIterator0 = new BoundedIterator<Predicate<Object>>(iterator0, 1031L, 1031L);
        boundedIterator0.next();
        BoundedIterator<Predicate<Object>> boundedIterator1 = new BoundedIterator<Predicate<Object>>(boundedIterator0, 1031L, 0L);
        // Undeclared exception!
        try {
            boundedIterator1.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // remove() cannot be called before calling next()
            //
            verifyException("org.apache.commons.collections4.iterators.BoundedIterator", e);
        }
    }
}

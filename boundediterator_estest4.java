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

public class BoundedIterator_ESTestTest4 extends BoundedIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        Iterator<Object> iterator0 = linkedList0.descendingIterator();
        BoundedIterator<Object> boundedIterator0 = new BoundedIterator<Object>(iterator0, 5L, 5L);
        linkedList0.add((Object) iterator0);
        // Undeclared exception!
        try {
            boundedIterator0.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}

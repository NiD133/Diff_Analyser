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

public class BoundedIterator_ESTestTest6 extends BoundedIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        BoundedIterator<Boolean> boundedIterator0 = null;
        try {
            boundedIterator0 = new BoundedIterator<Boolean>((Iterator<? extends Boolean>) null, 0L, 0L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // iterator
            //
            verifyException("java.util.Objects", e);
        }
    }
}

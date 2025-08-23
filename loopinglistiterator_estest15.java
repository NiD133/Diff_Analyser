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

public class LoopingListIterator_ESTestTest15 extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        LoopingListIterator<Object> loopingListIterator0 = null;
        try {
            loopingListIterator0 = new LoopingListIterator<Object>((List<Object>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // collection
            //
            verifyException("java.util.Objects", e);
        }
    }
}

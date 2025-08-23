package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.sql.SQLNonTransientConnectionException;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Stack;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CartesianProductIterator_ESTestTest6 extends CartesianProductIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        Iterable<SQLNonTransientConnectionException>[] iterableArray0 = (Iterable<SQLNonTransientConnectionException>[]) Array.newInstance(Iterable.class, 0);
        CartesianProductIterator<SQLNonTransientConnectionException> cartesianProductIterator0 = new CartesianProductIterator<SQLNonTransientConnectionException>(iterableArray0);
        // Undeclared exception!
        try {
            cartesianProductIterator0.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.iterators.CartesianProductIterator", e);
        }
    }
}

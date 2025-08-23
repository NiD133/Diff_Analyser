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

public class CartesianProductIterator_ESTestTest7 extends CartesianProductIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test6() throws Throwable {
        Iterable<Locale.Category>[] iterableArray0 = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 0);
        CartesianProductIterator<Locale.Category> cartesianProductIterator0 = new CartesianProductIterator<Locale.Category>(iterableArray0);
        // Undeclared exception!
        try {
            cartesianProductIterator0.remove();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // remove
            //
            verifyException("org.apache.commons.collections4.iterators.CartesianProductIterator", e);
        }
    }
}

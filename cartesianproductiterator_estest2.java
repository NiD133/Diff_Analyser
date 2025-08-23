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

public class CartesianProductIterator_ESTestTest2 extends CartesianProductIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        Stack<Locale.Category> stack0 = new Stack<Locale.Category>();
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        stack0.add(locale_Category0);
        Iterable<Locale.Category>[] iterableArray0 = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 1);
        iterableArray0[0] = (Iterable<Locale.Category>) stack0;
        CartesianProductIterator<Locale.Category> cartesianProductIterator0 = new CartesianProductIterator<Locale.Category>(iterableArray0);
        stack0.add(locale_Category0);
        // Undeclared exception!
        try {
            cartesianProductIterator0.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Vector$Itr", e);
        }
    }
}

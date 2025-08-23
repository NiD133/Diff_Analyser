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

public class CartesianProductIterator_ESTestTest3 extends CartesianProductIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        Iterable<Locale.Category>[] iterableArray0 = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 1);
        CartesianProductIterator<Object> cartesianProductIterator0 = null;
        try {
            cartesianProductIterator0 = new CartesianProductIterator<Object>(iterableArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // iterable
            //
            verifyException("java.util.Objects", e);
        }
    }
}

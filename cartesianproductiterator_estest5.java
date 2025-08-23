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

public class CartesianProductIterator_ESTestTest5 extends CartesianProductIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        Class<Locale.Category> class0 = Locale.Category.class;
        EnumSet<Locale.Category> enumSet0 = EnumSet.allOf(class0);
        Iterable<Locale.Category>[] iterableArray0 = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 2);
        iterableArray0[0] = (Iterable<Locale.Category>) enumSet0;
        iterableArray0[1] = (Iterable<Locale.Category>) enumSet0;
        CartesianProductIterator<Locale.Category> cartesianProductIterator0 = new CartesianProductIterator<Locale.Category>(iterableArray0);
        cartesianProductIterator0.next();
        cartesianProductIterator0.next();
        List<Locale.Category> list0 = cartesianProductIterator0.next();
        assertFalse(list0.isEmpty());
    }
}

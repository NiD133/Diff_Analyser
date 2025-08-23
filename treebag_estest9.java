package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TreeBag_ESTestTest9 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        TreeBag<Object> treeBag0 = new TreeBag<Object>();
        Object object0 = new Object();
        // Undeclared exception!
        try {
            treeBag0.add(object0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Objects of type class java.lang.Object cannot be added to a naturally ordered TreeBag as it does not implement Comparable
            //
            verifyException("org.apache.commons.collections4.bag.TreeBag", e);
        }
    }
}
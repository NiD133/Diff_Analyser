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

public class TreeBag_ESTestTest15 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        HashBag<Predicate<Object>> hashBag0 = new HashBag<Predicate<Object>>();
        Predicate<Object> predicate0 = AnyPredicate.anyPredicate((Collection<? extends Predicate<? super Object>>) hashBag0);
        hashBag0.add(predicate0);
        TreeBag<Predicate<Object>> treeBag0 = null;
        try {
            treeBag0 = new TreeBag<Predicate<Object>>((Collection<? extends Predicate<Object>>) hashBag0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Objects of type class org.apache.commons.collections4.functors.FalsePredicate cannot be added to a naturally ordered TreeBag as it does not implement Comparable
            //
            verifyException("org.apache.commons.collections4.bag.TreeBag", e);
        }
    }
}

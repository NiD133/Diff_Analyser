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

public class TreeBag_ESTestTest10 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Locale.Category locale_Category0 = Locale.Category.FORMAT;
        EnumSet<Locale.Category> enumSet0 = EnumSet.of(locale_Category0, locale_Category0);
        TreeBag<Object> treeBag0 = new TreeBag<Object>((Iterable<?>) enumSet0);
        // Undeclared exception!
        try {
            treeBag0.add((Object) "");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

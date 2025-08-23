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

public class TreeBag_ESTestTest3 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        TreeBag<Locale.Category> treeBag0 = new TreeBag<Locale.Category>();
        SortedMap<Locale.Category, AbstractMapBag.MutableInteger> sortedMap0 = (SortedMap<Locale.Category, AbstractMapBag.MutableInteger>) treeBag0.getMap();
        assertEquals(0, sortedMap0.size());
    }
}

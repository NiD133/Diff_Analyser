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

public class TreeBag_ESTestTest7 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null, (String) null).when(comparator0).toString();
        doReturn(0, 0).when(comparator0).compare(any(), any());
        TreeBag<Object> treeBag0 = new TreeBag<Object>(comparator0);
        TreeBag<Locale.FilteringMode> treeBag1 = new TreeBag<Locale.FilteringMode>();
        boolean boolean0 = treeBag0.add((Object) treeBag1);
        AbstractMapBag.MutableInteger abstractMapBag_MutableInteger0 = new AbstractMapBag.MutableInteger((-45));
        boolean boolean1 = treeBag0.add((Object) abstractMapBag_MutableInteger0);
        assertFalse(boolean1 == boolean0);
    }
}

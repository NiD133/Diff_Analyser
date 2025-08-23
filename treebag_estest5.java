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

public class TreeBag_ESTestTest5 extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparator0).toString();
        doReturn(0).when(comparator0).compare(any(), any());
        TreeBag<AbstractMapBag.MutableInteger> treeBag0 = new TreeBag<AbstractMapBag.MutableInteger>(comparator0);
        treeBag0.add((AbstractMapBag.MutableInteger) null);
        AbstractMapBag.MutableInteger abstractMapBag_MutableInteger0 = treeBag0.first();
        assertNull(abstractMapBag_MutableInteger0);
    }
}

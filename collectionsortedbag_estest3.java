package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CollectionSortedBag_ESTestTest3 extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        TreeBag<Object> treeBag0 = new TreeBag<Object>(comparator0);
        CollectionSortedBag<Object> collectionSortedBag0 = new CollectionSortedBag<Object>(treeBag0);
        Object object0 = new Object();
        collectionSortedBag0.add(object0, 1021);
        Comparator<Object> comparator1 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Transformer<Integer, Locale.Category>> treeBag1 = new TreeBag<Transformer<Integer, Locale.Category>>(comparator1);
        SynchronizedSortedBag<Transformer<Integer, Locale.Category>> synchronizedSortedBag0 = SynchronizedSortedBag.synchronizedSortedBag((SortedBag<Transformer<Integer, Locale.Category>>) treeBag1);
        CollectionSortedBag<Transformer<Integer, Locale.Category>> collectionSortedBag1 = new CollectionSortedBag<Transformer<Integer, Locale.Category>>(synchronizedSortedBag0);
        boolean boolean0 = collectionSortedBag1.containsAll(treeBag0);
        assertTrue(treeBag0.contains(0));
        assertFalse(boolean0);
    }
}

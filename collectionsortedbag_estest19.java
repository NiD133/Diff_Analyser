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

public class CollectionSortedBag_ESTestTest19 extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        TreeBag<Object> treeBag0 = new TreeBag<Object>(comparator0);
        Object object0 = new Object();
        Predicate<Object> predicate0 = EqualPredicate.equalPredicate(object0);
        TransformedPredicate<Object> transformedPredicate0 = new TransformedPredicate<Object>((Transformer<? super Object, ?>) null, predicate0);
        IfTransformer<Object, Object> ifTransformer0 = new IfTransformer<Object, Object>(transformedPredicate0, (Transformer<? super Object, ?>) null, (Transformer<? super Object, ?>) null);
        treeBag0.add((Object) ifTransformer0);
        Predicate<Object> predicate1 = UniquePredicate.uniquePredicate();
        PredicatedSortedBag<Object> predicatedSortedBag0 = PredicatedSortedBag.predicatedSortedBag((SortedBag<Object>) treeBag0, (Predicate<? super Object>) predicate1);
        CollectionSortedBag<Object> collectionSortedBag0 = new CollectionSortedBag<Object>(predicatedSortedBag0);
        // Undeclared exception!
        try {
            collectionSortedBag0.addAll(predicatedSortedBag0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add Object 'org.apache.commons.collections4.functors.IfTransformer@4' - Predicate 'org.apache.commons.collections4.functors.UniquePredicate@6' rejected it
            //
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }
}
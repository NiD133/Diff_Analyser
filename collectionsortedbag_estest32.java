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

public class CollectionSortedBag_ESTestTest32 extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Predicate<Object>> treeBag0 = new TreeBag<Predicate<Object>>(comparator0);
        IdentityPredicate<Object> identityPredicate0 = new IdentityPredicate<Object>(treeBag0);
        PredicatedSortedBag<Predicate<Object>> predicatedSortedBag0 = PredicatedSortedBag.predicatedSortedBag((SortedBag<Predicate<Object>>) treeBag0, (Predicate<? super Predicate<Object>>) identityPredicate0);
        CollectionSortedBag<Predicate<Object>> collectionSortedBag0 = new CollectionSortedBag<Predicate<Object>>(predicatedSortedBag0);
        // Undeclared exception!
        try {
            collectionSortedBag0.add((Predicate<Object>) identityPredicate0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add Object 'org.apache.commons.collections4.functors.IdentityPredicate@2' - Predicate 'org.apache.commons.collections4.functors.IdentityPredicate@2' rejected it
            //
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }
}

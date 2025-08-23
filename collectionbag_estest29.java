package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CollectionBag_ESTestTest29 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        CollectionBag<Integer> collectionBag0 = new CollectionBag<Integer>(treeBag0);
        TreeBag<Object> treeBag1 = new TreeBag<Object>((Collection<?>) collectionBag0);
        SynchronizedSortedBag<Object> synchronizedSortedBag0 = SynchronizedSortedBag.synchronizedSortedBag((SortedBag<Object>) treeBag1);
        Transformer<Object, Integer> transformer0 = ConstantTransformer.nullTransformer();
        TransformedSortedBag<Object> transformedSortedBag0 = TransformedSortedBag.transformingSortedBag((SortedBag<Object>) synchronizedSortedBag0, (Transformer<? super Object, ?>) transformer0);
        SortedBag<Object> sortedBag0 = CollectionSortedBag.collectionSortedBag((SortedBag<Object>) transformedSortedBag0);
        CollectionBag<Object> collectionBag1 = new CollectionBag<Object>(sortedBag0);
        // Undeclared exception!
        try {
            collectionBag1.add((Object) collectionBag0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.TreeMap", e);
        }
    }
}

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

public class CollectionBag_ESTestTest18 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        Integer integer0 = new Integer((-926));
        treeBag0.add(integer0);
        CollectionBag<Integer> collectionBag0 = new CollectionBag<Integer>(treeBag0);
        TreeBag<Object> treeBag1 = new TreeBag<Object>((Iterable<?>) collectionBag0);
        HashMap<Object, Object> hashMap0 = new HashMap<Object, Object>();
        Transformer<Object, Object> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ?>) hashMap0);
        Bag<Object> bag0 = TransformedBag.transformingBag((Bag<Object>) treeBag1, (Transformer<? super Object, ?>) transformer0);
        SynchronizedBag<Object> synchronizedBag0 = new SynchronizedBag<Object>(bag0, collectionBag0);
        CollectionBag<Object> collectionBag1 = new CollectionBag<Object>(synchronizedBag0);
        // Undeclared exception!
        try {
            collectionBag1.addAll(treeBag1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.TreeMap", e);
        }
    }
}

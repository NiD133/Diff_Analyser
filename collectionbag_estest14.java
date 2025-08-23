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

public class CollectionBag_ESTestTest14 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        Integer integer0 = new Integer((-1));
        treeBag0.add(integer0);
        TreeBag<Object> treeBag1 = new TreeBag<Object>();
        SynchronizedSortedBag<Object> synchronizedSortedBag0 = SynchronizedSortedBag.synchronizedSortedBag((SortedBag<Object>) treeBag1);
        Class<Object>[] classArray0 = (Class<Object>[]) Array.newInstance(Class.class, 0);
        InvokerTransformer<Object, Object> invokerTransformer0 = new InvokerTransformer<Object, Object>("LESS", classArray0, classArray0);
        TransformedBag<Object> transformedBag0 = new TransformedBag<Object>(synchronizedSortedBag0, invokerTransformer0);
        CollectionBag<Object> collectionBag0 = new CollectionBag<Object>(transformedBag0);
        // Undeclared exception!
        try {
            collectionBag0.addAll(treeBag0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method 'LESS' on 'class java.lang.Integer' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}

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

public class CollectionBag_ESTestTest26 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        TreeBag<Object> treeBag0 = new TreeBag<Object>();
        Class<Object>[] classArray0 = (Class<Object>[]) Array.newInstance(Class.class, 0);
        InvokerTransformer<Object, Object> invokerTransformer0 = new InvokerTransformer<Object, Object>("R5.G^N;x-<AR<<c", classArray0, classArray0);
        TransformedSortedBag<Object> transformedSortedBag0 = TransformedSortedBag.transformingSortedBag((SortedBag<Object>) treeBag0, (Transformer<? super Object, ?>) invokerTransformer0);
        CollectionBag<Object> collectionBag0 = new CollectionBag<Object>(transformedSortedBag0);
        Object object0 = new Object();
        // Undeclared exception!
        try {
            collectionBag0.add(object0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method 'R5.G^N;x-<AR<<c' on 'class java.lang.Object' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}

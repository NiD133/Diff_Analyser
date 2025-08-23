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

public class CollectionBag_ESTestTest22 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        TreeBag<Predicate<Object>> treeBag0 = new TreeBag<Predicate<Object>>();
        Predicate<Object> predicate0 = FalsePredicate.falsePredicate();
        IfTransformer<Object, Predicate<Object>> ifTransformer0 = new IfTransformer<Object, Predicate<Object>>(predicate0, (Transformer<? super Object, ? extends Predicate<Object>>) null, (Transformer<? super Object, ? extends Predicate<Object>>) null);
        Bag<Predicate<Object>> bag0 = TransformedBag.transformedBag((Bag<Predicate<Object>>) treeBag0, (Transformer<? super Predicate<Object>, ? extends Predicate<Object>>) ifTransformer0);
        SynchronizedBag<Predicate<Object>> synchronizedBag0 = new SynchronizedBag<Predicate<Object>>(bag0, predicate0);
        CollectionBag<Predicate<Object>> collectionBag0 = new CollectionBag<Predicate<Object>>(synchronizedBag0);
        // Undeclared exception!
        try {
            collectionBag0.add(predicate0, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.functors.IfTransformer", e);
        }
    }
}

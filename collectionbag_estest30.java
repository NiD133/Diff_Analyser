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

public class CollectionBag_ESTestTest30 extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        Integer integer0 = new Integer(4);
        IdentityPredicate<Integer> identityPredicate0 = new IdentityPredicate<Integer>(integer0);
        PredicatedSortedBag<Integer> predicatedSortedBag0 = new PredicatedSortedBag<Integer>(treeBag0, identityPredicate0);
        CollectionBag<Integer> collectionBag0 = new CollectionBag<Integer>(predicatedSortedBag0);
        Integer integer1 = new Integer(4);
        // Undeclared exception!
        try {
            collectionBag0.add(integer1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add Object '4' - Predicate 'org.apache.commons.collections4.functors.IdentityPredicate@2' rejected it
            //
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }
}

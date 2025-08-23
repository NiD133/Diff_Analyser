package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AndPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TransformedBag_ESTestTest28 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        HashBag<Integer> hashBag0 = new HashBag<Integer>();
        TreeBag<Object> treeBag0 = new TreeBag<Object>((Iterable<?>) hashBag0);
        Predicate<Object> predicate0 = IdentityPredicate.identityPredicate((Object) hashBag0);
        PredicatedSortedBag<Object> predicatedSortedBag0 = PredicatedSortedBag.predicatedSortedBag((SortedBag<Object>) treeBag0, (Predicate<? super Object>) predicate0);
        Integer integer0 = new Integer(1480);
        Transformer<Object, Integer> transformer0 = ConstantTransformer.constantTransformer(integer0);
        TransformedSortedBag<Object> transformedSortedBag0 = TransformedSortedBag.transformedSortedBag((SortedBag<Object>) predicatedSortedBag0, (Transformer<? super Object, ?>) transformer0);
        // Undeclared exception!
        try {
            transformedSortedBag0.add((Object) integer0, 39);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add Object '1480' - Predicate 'org.apache.commons.collections4.functors.IdentityPredicate@4' rejected it
            //
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }
}

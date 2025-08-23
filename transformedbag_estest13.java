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

public class TransformedBag_ESTestTest13 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        HashBag<Integer> hashBag0 = new HashBag<Integer>();
        Integer integer0 = new Integer(417);
        hashBag0.add(integer0);
        ConstantTransformer<Object, Integer> constantTransformer0 = new ConstantTransformer<Object, Integer>(integer0);
        Predicate<Integer> predicate0 = UniquePredicate.uniquePredicate();
        PredicatedBag<Integer> predicatedBag0 = PredicatedBag.predicatedBag((Bag<Integer>) hashBag0, (Predicate<? super Integer>) predicate0);
        IfTransformer<Integer, Integer> ifTransformer0 = new IfTransformer<Integer, Integer>(predicate0, constantTransformer0, constantTransformer0);
        // Undeclared exception!
        try {
            TransformedBag.transformedBag((Bag<Integer>) predicatedBag0, (Transformer<? super Integer, ? extends Integer>) ifTransformer0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add Object '417' - Predicate 'org.apache.commons.collections4.functors.UniquePredicate@4' rejected it
            //
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }
}
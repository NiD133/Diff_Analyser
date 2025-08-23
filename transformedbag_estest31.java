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

public class TransformedBag_ESTestTest31 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>();
        SynchronizedSortedBag<Integer> synchronizedSortedBag0 = new SynchronizedSortedBag<Integer>(treeBag0);
        Predicate<Object> predicate0 = FalsePredicate.falsePredicate();
        NotPredicate<Object> notPredicate0 = new NotPredicate<Object>(predicate0);
        AndPredicate<Integer> andPredicate0 = new AndPredicate<Integer>(notPredicate0, notPredicate0);
        PredicatedSortedBag<Integer> predicatedSortedBag0 = new PredicatedSortedBag<Integer>(synchronizedSortedBag0, andPredicate0);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 3);
        predicateArray0[0] = predicate0;
        predicateArray0[1] = (Predicate<Object>) notPredicate0;
        Transformer<Object, Integer>[] transformerArray0 = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 0);
        Transformer<Object, Integer> transformer0 = ExceptionTransformer.exceptionTransformer();
        SwitchTransformer<Object, Integer> switchTransformer0 = new SwitchTransformer<Object, Integer>(predicateArray0, transformerArray0, transformer0);
        TransformedSortedBag<Integer> transformedSortedBag0 = TransformedSortedBag.transformingSortedBag((SortedBag<Integer>) predicatedSortedBag0, (Transformer<? super Integer, ? extends Integer>) switchTransformer0);
        Integer integer0 = new Integer(2934);
        // Undeclared exception!
        try {
            transformedSortedBag0.add(integer0, 2934);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 1
            //
            verifyException("org.apache.commons.collections4.functors.SwitchTransformer", e);
        }
    }
}

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

public class TransformedBag_ESTestTest24 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Predicate<Object>> treeBag0 = new TreeBag<Predicate<Object>>(comparator0);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        UniquePredicate<Object> uniquePredicate0 = new UniquePredicate<Object>();
        predicateArray0[0] = (Predicate<Object>) uniquePredicate0;
        predicateArray0[1] = (Predicate<Object>) uniquePredicate0;
        NonePredicate<Object> nonePredicate0 = new NonePredicate<Object>(predicateArray0);
        predicateArray0[2] = (Predicate<Object>) nonePredicate0;
        OnePredicate<Object> onePredicate0 = new OnePredicate<Object>(predicateArray0);
        Transformer<Object, InstanceofPredicate> transformer0 = ConstantTransformer.nullTransformer();
        Transformer<Object, InstanceofPredicate> transformer1 = IfTransformer.ifTransformer((Predicate<? super Object>) onePredicate0, (Transformer<? super Object, ? extends InstanceofPredicate>) transformer0, (Transformer<? super Object, ? extends InstanceofPredicate>) transformer0);
        TransformedSortedBag<Predicate<Object>> transformedSortedBag0 = TransformedSortedBag.transformedSortedBag((SortedBag<Predicate<Object>>) treeBag0, (Transformer<? super Predicate<Object>, ? extends Predicate<Object>>) transformer1);
        // Undeclared exception!
        transformedSortedBag0.add(predicateArray0[2], 1487);
    }
}

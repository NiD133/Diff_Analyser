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

public class TransformedBag_ESTestTest37 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0, 0).when(comparator0).compare(any(), any());
        TreeBag<Object> treeBag0 = new TreeBag<Object>(comparator0);
        CollectionSortedBag<Object> collectionSortedBag0 = new CollectionSortedBag<Object>(treeBag0);
        Integer integer0 = new Integer(2);
        ConstantFactory<Integer> constantFactory0 = new ConstantFactory<Integer>(integer0);
        Transformer<Object, Integer> transformer0 = FactoryTransformer.factoryTransformer((Factory<? extends Integer>) constantFactory0);
        TransformedSortedBag<Object> transformedSortedBag0 = new TransformedSortedBag<Object>(collectionSortedBag0, transformer0);
        Transformer<Object, Predicate<Object>> transformer1 = InvokerTransformer.invokerTransformer("jA]A&z5z!a+Jbr(nH");
        transformedSortedBag0.add((Object) integer0, 2);
        // Undeclared exception!
        try {
            TransformedBag.transformedBag((Bag<Object>) transformedSortedBag0, (Transformer<? super Object, ?>) transformer1);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method 'jA]A&z5z!a+Jbr(nH' on 'class java.lang.Integer' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}
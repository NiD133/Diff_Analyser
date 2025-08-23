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

public class TransformedBag_ESTestTest42 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        Integer integer0 = new Integer(2);
        ConstantFactory<Integer> constantFactory0 = new ConstantFactory<Integer>(integer0);
        Transformer<Object, Integer> transformer0 = FactoryTransformer.factoryTransformer((Factory<? extends Integer>) constantFactory0);
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Predicate<Object>> treeBag0 = new TreeBag<Predicate<Object>>(comparator0);
        Transformer<Object, Predicate<Object>> transformer1 = InvokerTransformer.invokerTransformer("jA]A&z5z!a+Jbr(nH");
        TransformedSortedBag<Predicate<Object>> transformedSortedBag0 = new TransformedSortedBag<Predicate<Object>>(treeBag0, transformer1);
        Comparator<Object> comparator1 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Integer> treeBag1 = new TreeBag<Integer>(comparator1);
        SynchronizedSortedBag<Integer> synchronizedSortedBag0 = new SynchronizedSortedBag<Integer>(treeBag1);
        TransformedSortedBag<Integer> transformedSortedBag1 = new TransformedSortedBag<Integer>(synchronizedSortedBag0, transformer0);
        int int0 = transformedSortedBag1.getCount(transformedSortedBag0);
        assertEquals(0, int0);
    }
}

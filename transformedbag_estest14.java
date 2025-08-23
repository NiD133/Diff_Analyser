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

public class TransformedBag_ESTestTest14 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        HashBag<Integer> hashBag0 = new HashBag<Integer>();
        hashBag0.add((Integer) null);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);
        Integer integer0 = new Integer(5);
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(any(), any());
        ComparatorPredicate.Criterion comparatorPredicate_Criterion0 = ComparatorPredicate.Criterion.EQUAL;
        Predicate<Object> predicate0 = ComparatorPredicate.comparatorPredicate((Object) integer0, comparator0, (ComparatorPredicate.Criterion) comparatorPredicate_Criterion0);
        predicateArray0[0] = predicate0;
        Transformer<Object, Integer>[] transformerArray0 = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 0);
        HashMap<Object, Integer> hashMap0 = new HashMap<Object, Integer>();
        Transformer<Object, Integer> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ? extends Integer>) hashMap0);
        SwitchTransformer<Object, Integer> switchTransformer0 = new SwitchTransformer<Object, Integer>(predicateArray0, transformerArray0, transformer0);
        // Undeclared exception!
        try {
            TransformedBag.transformedBag((Bag<Integer>) hashBag0, (Transformer<? super Integer, ? extends Integer>) switchTransformer0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.collections4.functors.SwitchTransformer", e);
        }
    }
}

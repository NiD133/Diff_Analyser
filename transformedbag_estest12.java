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

public class TransformedBag_ESTestTest12 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        HashBag<Integer> hashBag0 = new HashBag<Integer>();
        Integer integer0 = new Integer(1);
        hashBag0.add(integer0);
        ConstantTransformer<Integer, Integer> constantTransformer0 = new ConstantTransformer<Integer, Integer>((Integer) null);
        TreeBag<Integer> treeBag0 = new TreeBag<Integer>((Iterable<? extends Integer>) hashBag0);
        TransformedSortedBag<Integer> transformedSortedBag0 = new TransformedSortedBag<Integer>(treeBag0, constantTransformer0);
        SynchronizedSortedBag<Integer> synchronizedSortedBag0 = SynchronizedSortedBag.synchronizedSortedBag((SortedBag<Integer>) transformedSortedBag0);
        // Undeclared exception!
        try {
            TransformedBag.transformedBag((Bag<Integer>) synchronizedSortedBag0, (Transformer<? super Integer, ? extends Integer>) constantTransformer0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // object
            //
            verifyException("java.util.Objects", e);
        }
    }
}

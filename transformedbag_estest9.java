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

public class TransformedBag_ESTestTest9 extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        HashBag<Integer> hashBag0 = new HashBag<Integer>();
        hashBag0.add((Integer) null);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 4);
        Object object0 = new Object();
        Predicate<Object> predicate0 = IdentityPredicate.identityPredicate(object0);
        AndPredicate<Object> andPredicate0 = new AndPredicate<Object>(predicate0, predicate0);
        predicateArray0[0] = (Predicate<Object>) andPredicate0;
        AnyPredicate<Object> anyPredicate0 = new AnyPredicate<Object>(predicateArray0);
        predicateArray0[1] = (Predicate<Object>) anyPredicate0;
        Transformer<Object, Integer>[] transformerArray0 = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 0);
        Integer integer0 = new Integer(1040);
        ConstantTransformer<Object, Integer> constantTransformer0 = new ConstantTransformer<Object, Integer>(integer0);
        SwitchTransformer<Object, Integer> switchTransformer0 = new SwitchTransformer<Object, Integer>(predicateArray0, transformerArray0, constantTransformer0);
        // Undeclared exception!
        TransformedBag.transformedBag((Bag<Integer>) hashBag0, (Transformer<? super Integer, ? extends Integer>) switchTransformer0);
    }
}
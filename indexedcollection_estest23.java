package org.apache.commons.collections4.collection;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest23 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 7);
        Predicate<Object> predicate0 = FalsePredicate.falsePredicate();
        predicateArray0[0] = predicate0;
        Integer integer0 = new Integer((-521));
        ConstantFactory<Integer> constantFactory0 = new ConstantFactory<Integer>(integer0);
        FactoryTransformer<Object, Integer> factoryTransformer0 = new FactoryTransformer<Object, Integer>(constantFactory0);
        TransformedPredicate<Object> transformedPredicate0 = new TransformedPredicate<Object>(factoryTransformer0, predicate0);
        predicateArray0[1] = (Predicate<Object>) transformedPredicate0;
        UniquePredicate<Object> uniquePredicate0 = new UniquePredicate<Object>();
        predicateArray0[2] = (Predicate<Object>) uniquePredicate0;
        predicateArray0[3] = predicate0;
        DefaultEquator<Object> defaultEquator0 = DefaultEquator.defaultEquator();
        EqualPredicate<Object> equalPredicate0 = new EqualPredicate<Object>(integer0, defaultEquator0);
        predicateArray0[4] = (Predicate<Object>) equalPredicate0;
        predicateArray0[5] = (Predicate<Object>) transformedPredicate0;
        Predicate<Object> predicate1 = TransformedPredicate.transformedPredicate((Transformer<? super Object, ?>) factoryTransformer0, (Predicate<? super Object>) uniquePredicate0);
        predicateArray0[6] = predicate1;
        Transformer<Object, Integer>[] transformerArray0 = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 5);
        transformerArray0[2] = (Transformer<Object, Integer>) factoryTransformer0;
        SwitchTransformer<Object, Object> switchTransformer0 = new SwitchTransformer<Object, Object>(predicateArray0, transformerArray0, (Transformer<? super Object, ?>) null);
        IndexedCollection<Object, Object> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection((Collection<Object>) linkedList0, (Transformer<Object, Object>) switchTransformer0);
        indexedCollection0.add(defaultEquator0.HASHCODE_NULL);
        // Undeclared exception!
        try {
            indexedCollection0.removeAll(linkedList0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 6
            //
            verifyException("org.apache.commons.collections4.functors.SwitchTransformer", e);
        }
    }
}

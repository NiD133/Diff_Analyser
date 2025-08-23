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

public class IndexedCollection_ESTestTest36 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        Transformer<Object, Object> transformer0 = ConstantTransformer.nullTransformer();
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        Class<Integer> class0 = Integer.class;
        InstanceofPredicate instanceofPredicate0 = new InstanceofPredicate(class0);
        NullIsFalsePredicate<Object> nullIsFalsePredicate0 = new NullIsFalsePredicate<Object>(instanceofPredicate0);
        predicateArray0[0] = (Predicate<Object>) nullIsFalsePredicate0;
        AllPredicate<Object> allPredicate0 = new AllPredicate<Object>(predicateArray0);
        predicateArray0[1] = (Predicate<Object>) allPredicate0;
        NonePredicate<Object> nonePredicate0 = new NonePredicate<Object>(predicateArray0);
        predicateArray0[2] = (Predicate<Object>) nonePredicate0;
        Transformer<Object, Object> transformer1 = IfTransformer.ifTransformer((Predicate<? super Object>) predicateArray0[2], (Transformer<? super Object, ?>) transformer0, (Transformer<? super Object, ?>) transformer0);
        IndexedCollection<Object, Object> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection((Collection<Object>) linkedList0, transformer1);
        // Undeclared exception!
        indexedCollection0.contains(predicateArray0[3]);
    }
}

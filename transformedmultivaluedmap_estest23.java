package org.apache.commons.collections4.multimap;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TransformedMultiValuedMap_ESTestTest23 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Integer integer0 = new Integer(1270);
        Factory<Integer> factory0 = ConstantFactory.constantFactory(integer0);
        HashMap<Integer, Integer> hashMap0 = new HashMap<Integer, Integer>();
        LinkedHashSetValuedLinkedHashMap<Integer, Integer> linkedHashSetValuedLinkedHashMap0 = new LinkedHashSetValuedLinkedHashMap<Integer, Integer>(hashMap0);
        UnmodifiableMultiValuedMap<Integer, Object> unmodifiableMultiValuedMap0 = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap((MultiValuedMap<? extends Integer, ?>) linkedHashSetValuedLinkedHashMap0);
        Transformer<Object, Integer> transformer0 = FactoryTransformer.factoryTransformer((Factory<? extends Integer>) factory0);
        Closure<Object> closure0 = TransformerClosure.transformerClosure((Transformer<? super Object, ?>) transformer0);
        Transformer<Object, Object> transformer1 = ClosureTransformer.closureTransformer((Closure<? super Object>) closure0);
        TransformedMultiValuedMap<Integer, Object> transformedMultiValuedMap0 = new TransformedMultiValuedMap<Integer, Object>(unmodifiableMultiValuedMap0, transformer0, transformer1);
        ArrayDeque<Integer> arrayDeque0 = new ArrayDeque<Integer>();
        arrayDeque0.offerFirst(integer0);
        // Undeclared exception!
        try {
            transformedMultiValuedMap0.putAll(integer0, (Iterable<?>) arrayDeque0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.collection.UnmodifiableCollection", e);
        }
    }
}

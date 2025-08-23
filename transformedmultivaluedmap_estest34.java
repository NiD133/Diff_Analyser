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

public class TransformedMultiValuedMap_ESTestTest34 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        HashMap<Predicate<Object>, Transformer<Integer, Integer>> hashMap0 = new HashMap<Predicate<Object>, Transformer<Integer, Integer>>();
        Transformer<Integer, Integer> transformer0 = SwitchTransformer.switchTransformer((Map<? extends Predicate<? super Integer>, ? extends Transformer<? super Integer, ? extends Integer>>) hashMap0);
        LinkedHashSetValuedLinkedHashMap<Object, Integer> linkedHashSetValuedLinkedHashMap0 = new LinkedHashSetValuedLinkedHashMap<Object, Integer>();
        Transformer<Object, Object> transformer1 = InvokerTransformer.invokerTransformer("82{wMeA6MsD+dn#");
        TransformedMultiValuedMap<Object, Integer> transformedMultiValuedMap0 = TransformedMultiValuedMap.transformedMap((MultiValuedMap<Object, Integer>) linkedHashSetValuedLinkedHashMap0, (Transformer<? super Object, ?>) transformer1, (Transformer<? super Integer, ? extends Integer>) transformer0);
        Object object0 = new Object();
        Comparator<Integer> comparator0 = (Comparator<Integer>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        PriorityQueue<Integer> priorityQueue0 = new PriorityQueue<Integer>(1, comparator0);
        boolean boolean0 = transformedMultiValuedMap0.putAll(object0, (Iterable<? extends Integer>) priorityQueue0);
        assertFalse(boolean0);
    }
}

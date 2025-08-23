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

public class TransformedMultiValuedMap_ESTestTest16 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> linkedHashSetValuedLinkedHashMap0 = new LinkedHashSetValuedLinkedHashMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>>(2566);
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        InvokerTransformer<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> invokerTransformer0 = new InvokerTransformer<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>>((String) null, classArray0, classArray0);
        TransformedMultiValuedMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> transformedMultiValuedMap0 = TransformedMultiValuedMap.transformedMap((MultiValuedMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>>) linkedHashSetValuedLinkedHashMap0, (Transformer<? super Object, ?>) invokerTransformer0, (Transformer<? super AbstractMap.SimpleImmutableEntry<Integer, Integer>, ? extends AbstractMap.SimpleImmutableEntry<Integer, Integer>>) invokerTransformer0);
        // Undeclared exception!
        try {
            transformedMultiValuedMap0.transformKey(invokerTransformer0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
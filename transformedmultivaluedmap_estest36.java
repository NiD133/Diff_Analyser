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

public class TransformedMultiValuedMap_ESTestTest36 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Integer, Predicate<Object>> linkedHashSetValuedLinkedHashMap0 = new LinkedHashSetValuedLinkedHashMap<Integer, Predicate<Object>>(1843);
        UnmodifiableMultiValuedMap<Integer, Predicate<Object>> unmodifiableMultiValuedMap0 = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap((MultiValuedMap<? extends Integer, ? extends Predicate<Object>>) linkedHashSetValuedLinkedHashMap0);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        AllPredicate<Object> allPredicate0 = new AllPredicate<Object>(predicateArray0);
        NullIsExceptionPredicate<Object> nullIsExceptionPredicate0 = new NullIsExceptionPredicate<Object>(allPredicate0);
        AnyPredicate<Object> anyPredicate0 = new AnyPredicate<Object>(predicateArray0);
        NotPredicate<Object> notPredicate0 = new NotPredicate<Object>(anyPredicate0);
        ConstantTransformer<Object, Predicate<Object>> constantTransformer0 = new ConstantTransformer<Object, Predicate<Object>>(allPredicate0);
        IfTransformer<Object, Predicate<Object>> ifTransformer0 = new IfTransformer<Object, Predicate<Object>>(notPredicate0, constantTransformer0, constantTransformer0);
        TransformedMultiValuedMap<Integer, Predicate<Object>> transformedMultiValuedMap0 = TransformedMultiValuedMap.transformingMap((MultiValuedMap<Integer, Predicate<Object>>) unmodifiableMultiValuedMap0, (Transformer<? super Integer, ? extends Integer>) null, (Transformer<? super Predicate<Object>, ? extends Predicate<Object>>) ifTransformer0);
        Integer integer0 = new Integer(1843);
        // Undeclared exception!
        try {
            transformedMultiValuedMap0.put(integer0, nullIsExceptionPredicate0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.functors.AnyPredicate", e);
        }
    }
}

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

public class TransformedMultiValuedMap_ESTestTest27 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        ArrayListValuedLinkedHashMap<Integer, Integer> arrayListValuedLinkedHashMap0 = new ArrayListValuedLinkedHashMap<Integer, Integer>();
        UnmodifiableMultiValuedMap<Integer, Object> unmodifiableMultiValuedMap0 = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap((MultiValuedMap<? extends Integer, ?>) arrayListValuedLinkedHashMap0);
        Integer integer0 = new Integer(0);
        Transformer<Object, Integer> transformer0 = ConstantTransformer.constantTransformer(integer0);
        TransformedMultiValuedMap<Integer, Object> transformedMultiValuedMap0 = new TransformedMultiValuedMap<Integer, Object>(unmodifiableMultiValuedMap0, (Transformer<? super Integer, ? extends Integer>) null, transformer0);
        // Undeclared exception!
        try {
            transformedMultiValuedMap0.put(integer0, integer0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap", e);
        }
    }
}

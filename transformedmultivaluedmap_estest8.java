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

public class TransformedMultiValuedMap_ESTestTest8 extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        ArrayListValuedHashMap<Integer, Integer> arrayListValuedHashMap0 = new ArrayListValuedHashMap<Integer, Integer>(1, 1);
        Transformer<Object, Integer> transformer0 = ConstantTransformer.nullTransformer();
        TransformedMultiValuedMap<Integer, Integer> transformedMultiValuedMap0 = TransformedMultiValuedMap.transformedMap((MultiValuedMap<Integer, Integer>) arrayListValuedHashMap0, (Transformer<? super Integer, ? extends Integer>) null, (Transformer<? super Integer, ? extends Integer>) transformer0);
        Integer integer0 = new Integer(1);
        transformedMultiValuedMap0.put(integer0, (Integer) null);
        HashSetValuedHashMap<Integer, Object> hashSetValuedHashMap0 = new HashSetValuedHashMap<Integer, Object>();
        TransformedMultiValuedMap<Integer, Object> transformedMultiValuedMap1 = new TransformedMultiValuedMap<Integer, Object>(hashSetValuedHashMap0, transformer0, transformer0);
        boolean boolean0 = transformedMultiValuedMap1.putAll((MultiValuedMap<? extends Integer, ?>) arrayListValuedHashMap0);
        assertTrue(boolean0);
    }
}

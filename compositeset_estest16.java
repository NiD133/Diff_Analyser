package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedClosure;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.OrPredicate;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.functors.WhileClosure;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CompositeSet_ESTestTest16 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, linkedHashSet0.size());
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>(linkedHashSet0);
        assertNotNull(compositeSet0);
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, linkedHashSet0.size());
        Integer integer0 = new Integer((-6));
        assertNotNull(integer0);
        assertEquals((-6), (int) integer0);
        boolean boolean0 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertTrue(boolean0);
        LinkedHashSet<Integer>[] linkedHashSetArray0 = (LinkedHashSet<Integer>[]) Array.newInstance(LinkedHashSet.class, 9);
        linkedHashSetArray0[0] = linkedHashSet0;
        linkedHashSetArray0[1] = linkedHashSet0;
        linkedHashSetArray0[2] = linkedHashSet0;
        linkedHashSetArray0[3] = linkedHashSet0;
        linkedHashSetArray0[4] = linkedHashSet0;
        linkedHashSetArray0[5] = linkedHashSet0;
        linkedHashSetArray0[6] = linkedHashSet0;
        Predicate<Integer> predicate0 = FalsePredicate.falsePredicate();
        assertNotNull(predicate0);
        EqualPredicate<Integer> equalPredicate0 = new EqualPredicate<Integer>(integer0);
        assertNotNull(equalPredicate0);
        Integer[] integerArray0 = new Integer[5];
        integerArray0[0] = integer0;
        integerArray0[1] = integer0;
        integerArray0[2] = integer0;
        integerArray0[3] = integer0;
        integerArray0[4] = integer0;
        Integer[] integerArray1 = compositeSet0.toArray(integerArray0);
        assertEquals(5, integerArray1.length);
        assertEquals(5, integerArray0.length);
        assertNotNull(integerArray1);
        assertSame(integerArray1, integerArray0);
        assertSame(integerArray0, integerArray1);
        assertTrue(linkedHashSet0.contains(integer0));
        assertTrue(compositeSet0.contains(integer0));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        // Undeclared exception!
        try {
            compositeSet0.addAll(linkedHashSet0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // addAll() is not supported on CompositeSet without a SetMutator strategy
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

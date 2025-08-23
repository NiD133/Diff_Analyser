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

public class CompositeSet_ESTestTest14 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        Integer integer0 = new Integer((-468));
        assertNotNull(integer0);
        assertEquals((-468), (int) integer0);
        boolean boolean0 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        assertTrue(boolean0);
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>(linkedHashSet0);
        assertNotNull(compositeSet0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertTrue(compositeSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        Stream<Integer> stream0 = compositeSet0.parallelStream();
        assertNotNull(stream0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertTrue(compositeSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        Iterator<Integer> iterator0 = compositeSet0.iterator();
        assertNotNull(iterator0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertTrue(compositeSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        int int0 = compositeSet0.size();
        assertTrue(linkedHashSet0.contains(integer0));
        assertFalse(linkedHashSet0.contains(int0));
        assertFalse(compositeSet0.contains(int0));
        assertTrue(compositeSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, int0);
        Set<Integer> set0 = compositeSet0.toSet();
        assertNotNull(set0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertFalse(linkedHashSet0.contains(int0));
        assertFalse(compositeSet0.contains(int0));
        assertTrue(compositeSet0.contains(integer0));
        assertFalse(set0.contains(int0));
        assertTrue(set0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, set0.size());
        assertFalse(set0.isEmpty());
        // Undeclared exception!
        try {
            compositeSet0.addComposited((Set<Integer>) linkedHashSet0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Collision adding composited set with no SetMutator set
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

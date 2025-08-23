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

public class CompositeSet_ESTestTest45 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        Integer integer0 = new Integer(3270);
        assertNotNull(integer0);
        assertEquals(3270, (int) integer0);
        boolean boolean0 = linkedHashSet0.add((Integer) null);
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertTrue(boolean0);
        boolean boolean1 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains(3270));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(2, linkedHashSet0.size());
        assertTrue(boolean1 == boolean0);
        assertTrue(boolean1);
        boolean boolean2 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains(3270));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(2, linkedHashSet0.size());
        assertFalse(boolean2 == boolean0);
        assertFalse(boolean2 == boolean1);
        assertFalse(boolean2);
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>(linkedHashSet0);
        assertNotNull(compositeSet0);
        assertTrue(linkedHashSet0.contains(3270));
        assertTrue(compositeSet0.contains(3270));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(2, linkedHashSet0.size());
        boolean boolean3 = compositeSet0.removeAll(linkedHashSet0);
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(compositeSet0.contains(3270));
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertTrue(boolean3 == boolean1);
        assertFalse(boolean3 == boolean2);
        assertTrue(boolean3 == boolean0);
        assertTrue(boolean3);
        boolean boolean4 = compositeSet0.containsAll(linkedHashSet0);
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(compositeSet0.contains(3270));
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertFalse(boolean4 == boolean2);
        assertTrue(boolean4 == boolean1);
        assertTrue(boolean4 == boolean0);
        assertTrue(boolean4 == boolean3);
        assertTrue(boolean4);
        compositeSet0.hashCode();
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(compositeSet0.contains(3270));
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        compositeSet0.clear();
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(compositeSet0.contains(3270));
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        Integer integer1 = new Integer(0);
        assertNotNull(integer1);
        assertFalse(integer1.equals((Object) integer0));
        assertEquals(0, (int) integer1);
        boolean boolean5 = linkedHashSet0.add(integer1);
        assertTrue(linkedHashSet0.contains(integer1));
        assertFalse(linkedHashSet0.contains(3270));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertFalse(integer1.equals((Object) integer0));
        assertTrue(boolean5 == boolean0);
        assertTrue(boolean5 == boolean1);
        assertTrue(boolean5 == boolean4);
        assertTrue(boolean5 == boolean3);
        assertFalse(boolean5 == boolean2);
        assertTrue(boolean5);
        // Undeclared exception!
        try {
            compositeSet0.addComposited((Set<Integer>) linkedHashSet0, (Set<Integer>) linkedHashSet0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Collision adding composited set with no SetMutator set
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

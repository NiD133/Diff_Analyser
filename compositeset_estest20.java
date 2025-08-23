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

public class CompositeSet_ESTestTest20 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Set<Object>[] setArray0 = (Set<Object>[]) Array.newInstance(Set.class, 2);
        CompositeSet<Object> compositeSet0 = new CompositeSet<Object>();
        assertNotNull(compositeSet0);
        setArray0[0] = (Set<Object>) compositeSet0;
        setArray0[1] = (Set<Object>) compositeSet0;
        CompositeSet<Object> compositeSet1 = new CompositeSet<Object>(setArray0);
        assertEquals(2, setArray0.length);
        assertNotNull(compositeSet1);
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        compositeSet1.clear();
        assertEquals(2, setArray0.length);
        assertNotSame(compositeSet1, compositeSet0);
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        int int0 = compositeSet1.size();
        assertEquals(2, setArray0.length);
        assertNotSame(compositeSet1, compositeSet0);
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        assertEquals(0, int0);
        Integer integer0 = new Integer(0);
        assertNotNull(integer0);
        assertTrue(integer0.equals((Object) int0));
        assertEquals(0, (int) integer0);
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertFalse(linkedHashSet0.contains(int0));
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, linkedHashSet0.size());
        Integer integer1 = new Integer(0);
        assertNotNull(integer1);
        assertTrue(integer1.equals((Object) int0));
        assertTrue(integer1.equals((Object) integer0));
        assertEquals(0, (int) integer1);
        boolean boolean0 = linkedHashSet0.add(integer1);
        assertTrue(linkedHashSet0.contains(int0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        assertTrue(integer1.equals((Object) int0));
        assertTrue(integer1.equals((Object) integer0));
        assertTrue(boolean0);
        boolean boolean1 = compositeSet0.isEmpty();
        assertNotSame(compositeSet0, compositeSet1);
        assertFalse(compositeSet0.contains(0));
        assertTrue(compositeSet0.equals((Object) compositeSet1));
        assertTrue(boolean1 == boolean0);
        assertTrue(boolean1);
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>(linkedHashSet0);
        assertNotNull(compositeSet2);
        assertTrue(linkedHashSet0.contains(int0));
        assertTrue(compositeSet2.contains(int0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        LinkedHashSet<Integer>[] linkedHashSetArray0 = (LinkedHashSet<Integer>[]) Array.newInstance(LinkedHashSet.class, 0);
        // Undeclared exception!
        try {
            compositeSet2.toArray(linkedHashSetArray0);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            //
            // java.lang.Integer
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

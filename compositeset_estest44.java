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

public class CompositeSet_ESTestTest44 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        Set<Integer>[] setArray0 = (Set<Integer>[]) Array.newInstance(Set.class, 5);
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>();
        assertNotNull(compositeSet0);
        Set<Integer> set0 = compositeSet0.toSet();
        assertNotNull(set0);
        assertTrue(set0.isEmpty());
        assertEquals(0, set0.size());
        setArray0[0] = set0;
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        CompositeSet<Integer> compositeSet1 = new CompositeSet<Integer>(linkedHashSet0);
        assertNotNull(compositeSet1);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        setArray0[1] = (Set<Integer>) compositeSet1;
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>(setArray0);
        assertEquals(5, setArray0.length);
        assertNotNull(compositeSet2);
        assertTrue(compositeSet2.equals((Object) compositeSet0));
        assertTrue(compositeSet2.equals((Object) compositeSet1));
        setArray0[2] = (Set<Integer>) compositeSet2;
        Set<Integer> set1 = compositeSet1.toSet();
        assertNotNull(set1);
        assertNotSame(compositeSet1, compositeSet0);
        assertNotSame(compositeSet1, compositeSet2);
        assertNotSame(set1, set0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, set1.size());
        assertTrue(set1.isEmpty());
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        assertTrue(compositeSet1.equals((Object) compositeSet2));
        assertTrue(set1.equals((Object) set0));
        setArray0[3] = set1;
        CompositeSet<Integer> compositeSet3 = new CompositeSet<Integer>(setArray0);
        assertEquals(5, setArray0.length);
        assertNotNull(compositeSet3);
        assertTrue(compositeSet3.equals((Object) compositeSet2));
        assertTrue(compositeSet3.equals((Object) compositeSet0));
        assertTrue(compositeSet3.equals((Object) compositeSet1));
        boolean boolean0 = compositeSet3.retainAll(set1);
        assertEquals(5, setArray0.length);
        assertNotSame(compositeSet1, compositeSet0);
        assertNotSame(compositeSet1, compositeSet2);
        assertNotSame(compositeSet1, compositeSet3);
        assertNotSame(set1, set0);
        assertNotSame(compositeSet3, compositeSet0);
        assertNotSame(compositeSet3, compositeSet1);
        assertNotSame(compositeSet3, compositeSet2);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, set1.size());
        assertTrue(set1.isEmpty());
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        assertTrue(compositeSet1.equals((Object) compositeSet2));
        assertTrue(compositeSet1.equals((Object) compositeSet3));
        assertTrue(set1.equals((Object) set0));
        assertTrue(compositeSet3.equals((Object) compositeSet2));
        assertTrue(compositeSet3.equals((Object) compositeSet0));
        assertTrue(compositeSet3.equals((Object) compositeSet1));
        assertFalse(boolean0);
        setArray0[4] = (Set<Integer>) compositeSet3;
        // Undeclared exception!
        try {
            compositeSet3.forEach((Consumer<? super Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Objects", e);
        }
    }
}

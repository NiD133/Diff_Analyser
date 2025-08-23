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

public class CompositeSet_ESTestTest7 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Set<Integer>[] setArray0 = (Set<Integer>[]) Array.newInstance(Set.class, 3);
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>();
        assertNotNull(compositeSet0);
        CompositeSet<Integer> compositeSet1 = new CompositeSet<Integer>(compositeSet0);
        assertNotNull(compositeSet1);
        assertTrue(compositeSet1.equals((Object) compositeSet0));
        setArray0[0] = (Set<Integer>) compositeSet1;
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertTrue(linkedHashSet0.isEmpty());
        assertEquals(0, linkedHashSet0.size());
        setArray0[1] = (Set<Integer>) linkedHashSet0;
        Integer integer0 = new Integer(6);
        assertNotNull(integer0);
        assertEquals(6, (int) integer0);
        boolean boolean0 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains(integer0));
        assertEquals(1, linkedHashSet0.size());
        assertFalse(linkedHashSet0.isEmpty());
        assertTrue(boolean0);
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>();
        assertNotNull(compositeSet2);
        assertFalse(compositeSet2.contains(integer0));
        assertTrue(compositeSet2.equals((Object) compositeSet1));
        assertTrue(compositeSet2.equals((Object) compositeSet0));
        setArray0[2] = (Set<Integer>) compositeSet2;
        CompositeSet<Integer> compositeSet3 = new CompositeSet<Integer>(setArray0);
        assertEquals(3, setArray0.length);
        assertNotNull(compositeSet3);
        assertTrue(compositeSet3.contains(integer0));
        assertFalse(compositeSet3.equals((Object) compositeSet2));
        assertFalse(compositeSet3.equals((Object) compositeSet1));
        assertFalse(compositeSet3.equals((Object) compositeSet0));
        // Undeclared exception!
        try {
            compositeSet3.addComposited(setArray0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Collision adding composited set with no SetMutator set
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

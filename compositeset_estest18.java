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

public class CompositeSet_ESTestTest18 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        assertNotNull(linkedHashSet0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        Integer integer0 = new Integer((-6));
        assertNotNull(integer0);
        assertEquals((-6), (int) integer0);
        boolean boolean0 = linkedHashSet0.add(integer0);
        assertTrue(linkedHashSet0.contains((-6)));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertTrue(boolean0);
        LinkedHashSet<Integer>[] linkedHashSetArray0 = (LinkedHashSet<Integer>[]) Array.newInstance(LinkedHashSet.class, 9);
        linkedHashSetArray0[0] = linkedHashSet0;
        linkedHashSetArray0[1] = linkedHashSet0;
        linkedHashSetArray0[2] = linkedHashSet0;
        linkedHashSetArray0[3] = linkedHashSet0;
        linkedHashSetArray0[5] = linkedHashSet0;
        linkedHashSetArray0[6] = linkedHashSet0;
        linkedHashSetArray0[7] = linkedHashSet0;
        CompositeSet<Object> compositeSet0 = new CompositeSet<Object>();
        assertNotNull(compositeSet0);
        assertFalse(compositeSet0.contains((-6)));
        CompositeSet.SetMutator<Object> compositeSet_SetMutator0 = compositeSet0.getMutator();
        assertNull(compositeSet_SetMutator0);
        assertFalse(compositeSet0.contains((-6)));
        compositeSet0.setMutator((CompositeSet.SetMutator<Object>) null);
        assertFalse(compositeSet0.contains((-6)));
        compositeSet0.addComposited((Set<Object>) null);
        assertFalse(compositeSet0.contains((-6)));
        boolean boolean1 = compositeSet0.containsAll(linkedHashSet0);
        assertTrue(linkedHashSet0.contains((-6)));
        assertFalse(compositeSet0.contains((-6)));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertFalse(boolean1 == boolean0);
        assertFalse(boolean1);
    }
}

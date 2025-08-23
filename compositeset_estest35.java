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

public class CompositeSet_ESTestTest35 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        LinkedHashSet<Object> linkedHashSet0 = new LinkedHashSet<Object>();
        assertNotNull(linkedHashSet0);
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        Object object0 = new Object();
        assertNotNull(object0);
        boolean boolean0 = linkedHashSet0.add(object0);
        assertTrue(linkedHashSet0.contains(object0));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        assertTrue(boolean0);
        CompositeSet<Object> compositeSet0 = new CompositeSet<Object>(linkedHashSet0);
        assertNotNull(compositeSet0);
        assertTrue(linkedHashSet0.contains(object0));
        assertTrue(compositeSet0.contains(object0));
        assertFalse(linkedHashSet0.isEmpty());
        assertEquals(1, linkedHashSet0.size());
        boolean boolean1 = compositeSet0.removeAll(linkedHashSet0);
        assertFalse(linkedHashSet0.contains(object0));
        assertFalse(compositeSet0.contains(object0));
        assertEquals(0, linkedHashSet0.size());
        assertTrue(linkedHashSet0.isEmpty());
        assertTrue(boolean1 == boolean0);
        assertTrue(boolean1);
    }
}

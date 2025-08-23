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

public class CompositeSet_ESTestTest92 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test91() throws Throwable {
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>((Set<Integer>[]) null);
        compositeSet0.hashCode();
        CompositeSet<Integer> compositeSet1 = new CompositeSet<Integer>((Set<Integer>[]) null);
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>(compositeSet1);
        boolean boolean0 = compositeSet2.retainAll(compositeSet1);
        CompositeSet.SetMutator<Integer> compositeSet_SetMutator0 = (CompositeSet.SetMutator<Integer>) mock(CompositeSet.SetMutator.class, new ViolatedAssumptionAnswer());
        compositeSet0.setMutator(compositeSet_SetMutator0);
        boolean boolean1 = compositeSet0.containsAll(compositeSet2);
        assertFalse(boolean1 == boolean0);
        compositeSet0.toSet();
        compositeSet0.getSets();
        compositeSet0.size();
        compositeSet2.toArray();
        compositeSet0.addComposited((Set<Integer>) compositeSet1);
        compositeSet2.getMutator();
        assertTrue(compositeSet2.equals((Object) compositeSet1));
    }
}

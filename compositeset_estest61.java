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

public class CompositeSet_ESTestTest61 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        Set<Integer>[] setArray0 = (Set<Integer>[]) Array.newInstance(Set.class, 3);
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        setArray0[0] = (Set<Integer>) linkedHashSet0;
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>();
        compositeSet0.addComposited(setArray0[0]);
        Set<Integer> set0 = compositeSet0.toSet();
        setArray0[1] = set0;
        Set<Integer> set1 = compositeSet0.toSet();
        compositeSet0.retainAll(linkedHashSet0);
        setArray0[2] = set1;
        CompositeSet<Integer> compositeSet1 = new CompositeSet<Integer>(setArray0);
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>(compositeSet1);
        // Undeclared exception!
        try {
            compositeSet2.addAll(set1);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // addAll() is not supported on CompositeSet without a SetMutator strategy
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

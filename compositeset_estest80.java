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

public class CompositeSet_ESTestTest80 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test79() throws Throwable {
        Set<Integer>[] setArray0 = (Set<Integer>[]) Array.newInstance(Set.class, 7);
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>();
        setArray0[0] = (Set<Integer>) compositeSet0;
        Set<Integer>[] setArray1 = (Set<Integer>[]) Array.newInstance(Set.class, 1);
        setArray1[0] = (Set<Integer>) compositeSet0;
        CompositeSet<Integer> compositeSet1 = new CompositeSet<Integer>(setArray1);
        setArray0[1] = (Set<Integer>) compositeSet1;
        CompositeSet<Integer> compositeSet2 = new CompositeSet<Integer>(setArray0[1]);
        setArray0[2] = (Set<Integer>) compositeSet2;
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        setArray0[3] = (Set<Integer>) linkedHashSet0;
        CompositeSet<Integer> compositeSet3 = new CompositeSet<Integer>(setArray0);
        setArray0[4] = (Set<Integer>) compositeSet3;
        CompositeSet<Integer> compositeSet4 = new CompositeSet<Integer>(setArray0[2]);
        setArray0[5] = (Set<Integer>) compositeSet4;
        LinkedHashSet<Integer> linkedHashSet1 = new LinkedHashSet<Integer>();
        setArray0[6] = (Set<Integer>) linkedHashSet1;
        CompositeSet<Integer> compositeSet5 = new CompositeSet<Integer>(setArray0);
        AnyPredicate<Integer> anyPredicate0 = new AnyPredicate<Integer>((Predicate<? super Integer>[]) null);
        OnePredicate<Integer> onePredicate0 = new OnePredicate<Integer>((Predicate<? super Integer>[]) null);
        Predicate<Integer> predicate0 = NullIsTruePredicate.nullIsTruePredicate((Predicate<? super Integer>) onePredicate0);
        anyPredicate0.or(predicate0);
        boolean boolean0 = compositeSet5.removeIf(anyPredicate0);
        assertFalse(boolean0);
    }
}
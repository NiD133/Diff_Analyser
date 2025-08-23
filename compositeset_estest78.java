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

public class CompositeSet_ESTestTest78 extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test77() throws Throwable {
        LinkedHashSet<Integer> linkedHashSet0 = new LinkedHashSet<Integer>();
        CompositeSet<Integer> compositeSet0 = new CompositeSet<Integer>(linkedHashSet0);
        Integer integer0 = new Integer((-6));
        linkedHashSet0.add(integer0);
        LinkedHashSet<Integer>[] linkedHashSetArray0 = (LinkedHashSet<Integer>[]) Array.newInstance(LinkedHashSet.class, 9);
        linkedHashSetArray0[0] = linkedHashSet0;
        linkedHashSetArray0[1] = linkedHashSet0;
        linkedHashSetArray0[2] = linkedHashSet0;
        linkedHashSetArray0[3] = linkedHashSet0;
        compositeSet0.toArray();
        linkedHashSetArray0[4] = linkedHashSet0;
        linkedHashSetArray0[5] = linkedHashSet0;
        linkedHashSetArray0[6] = linkedHashSet0;
        linkedHashSetArray0[7] = linkedHashSet0;
        Integer integer1 = new Integer((-6));
        linkedHashSet0.add(integer1);
        linkedHashSetArray0[8] = linkedHashSet0;
        // Undeclared exception!
        try {
            compositeSet0.toArray(linkedHashSetArray0);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            //
            // java.lang.Integer
            //
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }
}

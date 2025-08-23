package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CollectionSortedBag_ESTestTest28 extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        TreeBag<Object> treeBag0 = new TreeBag<Object>();
        CollectionSortedBag<Object> collectionSortedBag0 = new CollectionSortedBag<Object>(treeBag0);
        Transformer<Object, Object> transformer0 = ExceptionTransformer.exceptionTransformer();
        TransformedSortedBag<Object> transformedSortedBag0 = new TransformedSortedBag<Object>(collectionSortedBag0, transformer0);
        CollectionSortedBag<Object> collectionSortedBag1 = new CollectionSortedBag<Object>(transformedSortedBag0);
        // Undeclared exception!
        try {
            collectionSortedBag1.add((Object) collectionSortedBag0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ExceptionTransformer invoked
            //
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }
}

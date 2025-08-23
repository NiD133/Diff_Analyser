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

public class CollectionSortedBag_ESTestTest26 extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        TreeBag<ComparatorPredicate.Criterion> treeBag0 = new TreeBag<ComparatorPredicate.Criterion>();
        TreeBag<Object> treeBag1 = new TreeBag<Object>((Collection<?>) treeBag0);
        CollectionSortedBag<Object> collectionSortedBag0 = new CollectionSortedBag<Object>(treeBag1);
        // Undeclared exception!
        try {
            collectionSortedBag0.add((Object) treeBag0, 1000);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

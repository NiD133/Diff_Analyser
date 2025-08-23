package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.functors.WhileClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class FilterListIterator_ESTestTest25 extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Predicate<Integer> predicate0 = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterListIterator0 = new FilterListIterator<Integer>(predicate0);
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        ExceptionPredicate.exceptionPredicate();
        Predicate<Object> predicate1 = NullPredicate.nullPredicate();
        predicate0.and(predicate1);
        Integer integer0 = new Integer(5795);
        linkedList0.add(integer0);
        filterListIterator0.hasPrevious();
        ListIterator<Integer> listIterator0 = linkedList0.listIterator(0);
        filterListIterator0.setListIterator(listIterator0);
        filterListIterator0.hasNext();
        predicate0.negate();
        FilterListIterator<Boolean> filterListIterator1 = new FilterListIterator<Boolean>();
        filterListIterator1.getListIterator();
        FilterListIterator<Integer> filterListIterator2 = new FilterListIterator<Integer>(filterListIterator0, predicate1);
        filterListIterator0.hasPrevious();
        // Undeclared exception!
        try {
            filterListIterator0.previous();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.iterators.FilterListIterator", e);
        }
    }
}

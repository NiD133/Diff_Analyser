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

public class FilterListIterator_ESTestTest13 extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        FilterListIterator<Boolean> filterListIterator0 = new FilterListIterator<Boolean>();
        Object object0 = new Object();
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.removeFirstOccurrence(filterListIterator0);
        Predicate<Object> predicate0 = ExceptionPredicate.exceptionPredicate();
        Predicate<Object> predicate1 = NullPredicate.nullPredicate();
        LinkedList<Predicate<Object>> linkedList1 = new LinkedList<Predicate<Object>>();
        Predicate<Integer> predicate2 = OnePredicate.onePredicate((Collection<? extends Predicate<? super Integer>>) linkedList1);
        predicate2.and(predicate1);
        Integer integer0 = new Integer(5795);
        linkedList0.add((Integer) null);
        ListIterator<Integer> listIterator0 = linkedList0.listIterator();
        FilterListIterator<Integer> filterListIterator1 = new FilterListIterator<Integer>(listIterator0, predicate1);
        filterListIterator1.hasPrevious();
        linkedList0.listIterator(1);
        filterListIterator1.setListIterator(listIterator0);
        filterListIterator1.hasNext();
        java.util.function.Predicate<Integer> predicate3 = new IdentityPredicate<Integer>(integer0);
        FilterListIterator<Boolean> filterListIterator2 = new FilterListIterator<Boolean>();
        FilterListIterator<Boolean> filterListIterator3 = new FilterListIterator<Boolean>();
        FilterListIterator<Integer> filterListIterator4 = new FilterListIterator<Integer>(listIterator0, predicate0);
        filterListIterator1.hasPrevious();
        // Undeclared exception!
        try {
            filterListIterator4.hasNext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ExceptionPredicate invoked
            //
            verifyException("org.apache.commons.collections4.functors.ExceptionPredicate", e);
        }
    }
}

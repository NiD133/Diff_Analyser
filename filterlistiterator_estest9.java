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

public class FilterListIterator_ESTestTest9 extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        FilterListIterator<Boolean> filterListIterator0 = new FilterListIterator<Boolean>();
        Predicate<Object> predicate0 = ExceptionPredicate.exceptionPredicate();
        Predicate<Object> predicate1 = NullPredicate.nullPredicate();
        Predicate<Integer> predicate2 = NullIsExceptionPredicate.nullIsExceptionPredicate((Predicate<? super Integer>) predicate0);
        predicate2.and(predicate1);
        Integer integer0 = new Integer(0);
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.add((Integer) null);
        FilterListIterator<Integer> filterListIterator1 = new FilterListIterator<Integer>();
        filterListIterator1.hasPrevious();
        ListIterator<Integer> listIterator0 = linkedList0.listIterator(0);
        FilterListIterator<Integer> filterListIterator2 = new FilterListIterator<Integer>(listIterator0, predicate0);
        filterListIterator2.setListIterator(listIterator0);
        // Undeclared exception!
        try {
            filterListIterator2.hasNext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ExceptionPredicate invoked
            //
            verifyException("org.apache.commons.collections4.functors.ExceptionPredicate", e);
        }
    }
}

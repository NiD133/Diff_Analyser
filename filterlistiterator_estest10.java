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

public class FilterListIterator_ESTestTest10 extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        FilterListIterator<Boolean> filterListIterator0 = new FilterListIterator<Boolean>();
        Integer integer0 = new Integer((-2175));
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        linkedList0.add(integer0);
        ListIterator<Integer> listIterator0 = linkedList0.listIterator();
        FilterListIterator<Integer> filterListIterator1 = new FilterListIterator<Integer>(listIterator0);
        filterListIterator1.hasPrevious();
        ListIterator<Integer> listIterator1 = linkedList0.listIterator(1);
        filterListIterator1.setListIterator(listIterator1);
        filterListIterator1.hasNext();
        Predicate<Integer> predicate0 = NotNullPredicate.notNullPredicate();
        predicate0.negate();
        FilterListIterator<Boolean> filterListIterator2 = new FilterListIterator<Boolean>();
        filterListIterator2.getListIterator();
        FilterListIterator<Integer> filterListIterator3 = new FilterListIterator<Integer>(listIterator0, predicate0);
        filterListIterator3.setPredicate(predicate0);
        filterListIterator3.hasPrevious();
        filterListIterator1.hasNext();
        // Undeclared exception!
        try {
            filterListIterator1.previous();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.iterators.FilterListIterator", e);
        }
    }
}
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

public class FilterListIterator_ESTestTest31 extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        linkedList0.poll();
        ListIterator<Object> listIterator0 = linkedList0.listIterator(0);
        FilterListIterator<Object> filterListIterator0 = new FilterListIterator<Object>(listIterator0);
        Integer integer0 = new Integer(512);
        Predicate<Object> predicate0 = IdentityPredicate.identityPredicate((Object) integer0);
        NullIsExceptionPredicate<Object> nullIsExceptionPredicate0 = new NullIsExceptionPredicate<Object>(predicate0);
        Closure<Object> closure0 = ExceptionClosure.exceptionClosure();
        Transformer<Object, Object> transformer0 = ClosureTransformer.closureTransformer((Closure<? super Object>) closure0);
        TransformedPredicate<Object> transformedPredicate0 = new TransformedPredicate<Object>(transformer0, nullIsExceptionPredicate0);
        java.util.function.Predicate.isEqual((Object) nullIsExceptionPredicate0);
        IfClosure<Object> ifClosure0 = new IfClosure<Object>(transformedPredicate0, closure0, closure0);
        Closure<Object> closure1 = IfClosure.ifClosure((Predicate<? super Object>) nullIsExceptionPredicate0, (Closure<? super Object>) ifClosure0, (Closure<? super Object>) ifClosure0);
        filterListIterator0.forEachRemaining(closure1);
        FilterListIterator<InstanceofPredicate> filterListIterator1 = new FilterListIterator<InstanceofPredicate>();
        FilterListIterator<Predicate<Object>> filterListIterator2 = new FilterListIterator<Predicate<Object>>(filterListIterator1, transformedPredicate0);
        filterListIterator2.getPredicate();
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
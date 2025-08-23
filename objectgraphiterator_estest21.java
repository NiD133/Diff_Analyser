package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AndPredicate;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.apache.commons.collections4.functors.OrPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ObjectGraphIterator_ESTestTest21 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Integer integer0 = new Integer(566);
        ObjectGraphIterator<Integer> objectGraphIterator0 = new ObjectGraphIterator<Integer>(integer0, (Transformer<? super Integer, ? extends Integer>) null);
        ObjectGraphIterator<Integer> objectGraphIterator1 = new ObjectGraphIterator<Integer>(objectGraphIterator0);
        Object object0 = objectGraphIterator1.next();
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        ComparatorPredicate.Criterion comparatorPredicate_Criterion0 = ComparatorPredicate.Criterion.LESS_OR_EQUAL;
        Predicate<Object> predicate0 = ComparatorPredicate.comparatorPredicate((Object) object0, comparator0, (ComparatorPredicate.Criterion) comparatorPredicate_Criterion0);
        OrPredicate.orPredicate((Predicate<? super Object>) predicate0, (Predicate<? super Object>) predicate0);
        String string0 = "comparator";
        // Undeclared exception!
        try {
            objectGraphIterator1.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Iterator remove() cannot be called at this time
            //
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }
}

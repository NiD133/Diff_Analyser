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

public class ObjectGraphIterator_ESTestTest9 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ObjectGraphIterator<Predicate<Object>> objectGraphIterator0 = new ObjectGraphIterator<Predicate<Object>>((Iterator<? extends Predicate<Object>>) null);
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Iterator<Integer> iterator0 = linkedList0.descendingIterator();
        ObjectGraphIterator<Integer> objectGraphIterator1 = new ObjectGraphIterator<Integer>(iterator0);
        objectGraphIterator1.findNextByIterator(iterator0);
        ObjectGraphIterator<Object> objectGraphIterator2 = new ObjectGraphIterator<Object>(objectGraphIterator1, (Transformer<? super Object, ?>) null);
        ObjectGraphIterator<Object> objectGraphIterator3 = new ObjectGraphIterator<Object>(objectGraphIterator2);
        objectGraphIterator3.hasNext();
        PredicateTransformer<Transformer<Object, Integer>> predicateTransformer0 = new PredicateTransformer<Transformer<Object, Integer>>((Predicate<? super Transformer<Object, Integer>>) null);
        HashMap<Object, Integer> hashMap0 = new HashMap<Object, Integer>();
        HashMap<Object, Integer> hashMap1 = new HashMap<Object, Integer>();
        Transformer<Object, Integer> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ? extends Integer>) hashMap1);
        // Undeclared exception!
        try {
            predicateTransformer0.transform(transformer0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.functors.PredicateTransformer", e);
        }
    }
}

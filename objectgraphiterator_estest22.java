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

public class ObjectGraphIterator_ESTestTest22 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Object object0 = new Object();
        Integer integer0 = new Integer((-350));
        Factory<Integer> factory0 = ConstantFactory.constantFactory(integer0);
        FactoryTransformer<Object, Object> factoryTransformer0 = new FactoryTransformer<Object, Object>(factory0);
        ObjectGraphIterator<Object> objectGraphIterator0 = new ObjectGraphIterator<Object>(object0, factoryTransformer0);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        IdentityPredicate<Object> identityPredicate0 = new IdentityPredicate<Object>(integer0);
        Predicate<Object> predicate0 = NullIsFalsePredicate.nullIsFalsePredicate((Predicate<? super Object>) identityPredicate0);
        predicateArray0[0] = predicate0;
        Predicate<Object> predicate1 = FalsePredicate.falsePredicate();
        predicateArray0[1] = predicate1;
        Predicate<Object> predicate2 = NullIsTruePredicate.nullIsTruePredicate((Predicate<? super Object>) predicate1);
        predicateArray0[2] = predicate2;
        predicate2.test(predicate0);
        Predicate<Object> predicate3 = AndPredicate.andPredicate((Predicate<? super Object>) identityPredicate0, (Predicate<? super Object>) predicate0);
        predicateArray0[3] = predicate3;
        EqualPredicate<Object> equalPredicate0 = new EqualPredicate<Object>(factory0);
        predicateArray0[4] = (Predicate<Object>) equalPredicate0;
        Predicate<Object> predicate4 = NonePredicate.nonePredicate((Predicate<? super Object>[]) predicateArray0);
        PredicateTransformer<Transformer<Object, Integer>> predicateTransformer0 = new PredicateTransformer<Transformer<Object, Integer>>(predicate4);
        predicate4.test(predicate0);
        FactoryTransformer<Object, Integer> factoryTransformer1 = new FactoryTransformer<Object, Integer>(factory0);
        Boolean boolean0 = predicateTransformer0.transform(factoryTransformer1);
        Transformer<Object, Boolean> transformer0 = PredicateTransformer.predicateTransformer((Predicate<? super Object>) predicate0);
        ObjectGraphIterator<Boolean> objectGraphIterator1 = new ObjectGraphIterator<Boolean>(boolean0, transformer0);
        predicate4.negate();
        ObjectGraphIterator<Boolean> objectGraphIterator2 = new ObjectGraphIterator<Boolean>(boolean0, transformer0);
        objectGraphIterator1.findNextByIterator(objectGraphIterator2);
        Object object1 = new Object();
        objectGraphIterator0.findNext(object1);
        ObjectGraphIterator<Integer> objectGraphIterator3 = new ObjectGraphIterator<Integer>(integer0, factoryTransformer1);
        objectGraphIterator3.hasNext();
        objectGraphIterator0.findNextByIterator(objectGraphIterator3);
        objectGraphIterator0.next();
        // Undeclared exception!
        try {
            objectGraphIterator3.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Iterator remove() cannot be called at this time
            //
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }
}

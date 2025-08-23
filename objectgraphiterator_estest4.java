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

public class ObjectGraphIterator_ESTestTest4 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Object[] objectArray0 = new Object[7];
        objectArray0[0] = (Object) linkedList0;
        objectArray0[1] = (Object) linkedList0;
        objectArray0[2] = (Object) linkedList0;
        objectArray0[3] = (Object) linkedList0;
        objectArray0[4] = (Object) linkedList0;
        objectArray0[5] = (Object) linkedList0;
        objectArray0[6] = (Object) linkedList0;
        linkedList0.toArray(objectArray0);
        ListIterator<Integer> listIterator0 = linkedList0.listIterator();
        Comparator<Integer> comparator0 = (Comparator<Integer>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        linkedList0.sort(comparator0);
        linkedList0.add((Integer) null);
        ObjectGraphIterator<Integer> objectGraphIterator0 = new ObjectGraphIterator<Integer>(listIterator0);
        // Undeclared exception!
        try {
            objectGraphIterator0.updateCurrentIterator();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}

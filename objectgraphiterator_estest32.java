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

public class ObjectGraphIterator_ESTestTest32 extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Iterator<Integer> iterator0 = linkedList0.descendingIterator();
        Integer integer0 = new Integer(3471);
        HashMap<Object, Integer> hashMap0 = new HashMap<Object, Integer>();
        Transformer<Object, Integer> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ? extends Integer>) hashMap0);
        ObjectGraphIterator<Integer> objectGraphIterator0 = new ObjectGraphIterator<Integer>(integer0, transformer0);
        objectGraphIterator0.updateCurrentIterator();
        objectGraphIterator0.findNextByIterator(iterator0);
        HashMap<Object, Integer> hashMap1 = new HashMap<Object, Integer>();
        Integer integer1 = new Integer(1945);
        objectGraphIterator0.findNext(integer1);
        ObjectGraphIterator<Object> objectGraphIterator1 = new ObjectGraphIterator<Object>(iterator0);
        objectGraphIterator1.findNext(hashMap0);
        Integer integer2 = new Integer(1945);
        ObjectGraphIterator<Integer> objectGraphIterator2 = new ObjectGraphIterator<Integer>(integer2, transformer0);
        objectGraphIterator0.findNextByIterator(objectGraphIterator2);
        assertFalse(objectGraphIterator2.equals((Object) objectGraphIterator0));
    }
}

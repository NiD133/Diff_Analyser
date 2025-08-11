package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ObjectGraphIterator_ESTest extends ObjectGraphIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveOnCopiedIterator() {
        Integer initialValue = 2;
        ObjectGraphIterator<Integer> originalIterator = new ObjectGraphIterator<>(initialValue, null);
        LinkedList<Integer> list = new LinkedList<>();
        list.add(-2817);
        Iterator<Integer> descendingIterator = list.descendingIterator();
        originalIterator.findNextByIterator(descendingIterator);

        ObjectGraphIterator<Integer> copiedIterator = new ObjectGraphIterator<>(originalIterator);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(copiedIterator);

        wrappedIterator.next();
        copiedIterator.remove();

        assertFalse(copiedIterator.equals(originalIterator));
    }

    @Test(timeout = 4000)
    public void testHasNextWithNullTransformer() {
        Integer initialValue = -200;
        Predicate<Object> falsePredicate = FalsePredicate.falsePredicate();
        IfTransformer<Object, Integer> ifTransformer = new IfTransformer<>(falsePredicate, null, null);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, ifTransformer);

        // Expecting NullPointerException due to null transformer
        try {
            iterator.hasNext();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.IfTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateCurrentIteratorWithNullTransformer() {
        Integer initialValue = 1;
        Predicate<Object> booleanPredicate = InstanceofPredicate.instanceOfPredicate(Boolean.class);
        IfTransformer<Object, Integer> ifTransformer = new IfTransformer<>(booleanPredicate, null, null);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, ifTransformer);
        ObjectGraphIterator<Integer> copiedIterator = new ObjectGraphIterator<>(iterator);

        // Expecting NullPointerException due to null transformer
        try {
            copiedIterator.updateCurrentIterator();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.IfTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnUpdateCurrentIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        ListIterator<Integer> listIterator = list.listIterator();
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());

        list.sort(comparator);
        list.add(null);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(listIterator);

        // Expecting ConcurrentModificationException due to list modification
        try {
            iterator.updateCurrentIterator();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindNextByIteratorWithEmptyList() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);

        ListIterator<Integer> listIterator = list.listIterator();
        objectIterator.findNextByIterator(listIterator);

        assertFalse(listIterator.hasPrevious());
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnNext() {
        LinkedList<Integer> list = new LinkedList<>();
        list.push(1329);
        ListIterator<Integer> listIterator = list.listIterator();
        list.add(-1797);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(listIterator);

        // Expecting ConcurrentModificationException due to list modification
        try {
            iterator.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextWithNullInvokerTransformer() {
        Integer initialValue = 4;
        InvokerTransformer<Object, Integer> invokerTransformer = new InvokerTransformer<>(null, null, null);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, invokerTransformer);

        // Expecting NullPointerException due to null invoker transformer
        try {
            iterator.next();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFindNextByIteratorWithRuntimeException() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);

        InstanceofPredicate instanceofPredicate = new InstanceofPredicate(Integer.class);
        Transformer<InstanceofPredicate, InstanceofPredicate> transformer = InvokerTransformer.invokerTransformer("'#+Y@U&ZFCL9E$FCS");

        ObjectGraphIterator<InstanceofPredicate> iterator1 = new ObjectGraphIterator<>(instanceofPredicate, transformer);
        ObjectGraphIterator<InstanceofPredicate> iterator2 = new ObjectGraphIterator<>(instanceofPredicate, transformer);

        // Expecting RuntimeException due to invalid method invocation
        try {
            iterator1.findNextByIterator(iterator2);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnPredicateTransformer() {
        ObjectGraphIterator<Predicate<Object>> predicateIterator = new ObjectGraphIterator<>(null);
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(descendingIterator);

        integerIterator.findNextByIterator(descendingIterator);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(integerIterator, null);
        ObjectGraphIterator<Object> copiedIterator = new ObjectGraphIterator<>(wrappedIterator);

        copiedIterator.hasNext();

        PredicateTransformer<Transformer<Object, Integer>> predicateTransformer = new PredicateTransformer<>(null);
        HashMap<Object, Integer> hashMap = new HashMap<>();
        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(hashMap);

        // Expecting NullPointerException due to null predicate
        try {
            predicateTransformer.transform(mapTransformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.PredicateTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentIterators() {
        Integer initialValue = -12;
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, null);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(integerIterator, null);
        ObjectGraphIterator<Object> copiedIterator = new ObjectGraphIterator<>(wrappedIterator);

        assertFalse(copiedIterator.equals(wrappedIterator));
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnFindNextByIterator() {
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(null);

        // Expecting NullPointerException due to null iterator
        try {
            objectIterator.findNextByIterator(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemove() {
        Integer initialValue = 2;
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, null);
        integerIterator.findNext(initialValue);

        ObjectGraphIterator<Integer> copiedIterator = new ObjectGraphIterator<>(integerIterator);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(copiedIterator);

        wrappedIterator.next();

        // Expecting IllegalStateException due to invalid remove operation
        try {
            copiedIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindNextByIteratorWithConstantTransformer() {
        Class<Integer> integerClass = Integer.class;
        InstanceofPredicate instanceofPredicate = new InstanceofPredicate(integerClass);
        ConstantTransformer<InstanceofPredicate, InstanceofPredicate> constantTransformer = new ConstantTransformer<>(instanceofPredicate);

        ObjectGraphIterator<InstanceofPredicate> iterator1 = new ObjectGraphIterator<>(instanceofPredicate, constantTransformer);
        ObjectGraphIterator<InstanceofPredicate> iterator2 = new ObjectGraphIterator<>(instanceofPredicate, constantTransformer);

        iterator1.findNextByIterator(iterator2);

        assertFalse(iterator1.equals(iterator2));
    }

    @Test(timeout = 4000)
    public void testFindNextByIteratorWithEmptyListIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(descendingIterator);

        ListIterator<Integer> listIterator = list.listIterator();
        integerIterator.findNextByIterator(listIterator);

        assertFalse(listIterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testNoSuchElementExceptionOnNext() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);

        objectIterator.findNext(descendingIterator);

        // Expecting NoSuchElementException due to empty iteration
        try {
            objectIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveAfterFindNextByIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(0);
        ListIterator<Integer> listIterator = list.listIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(listIterator);

        objectIterator.hasNext();
        objectIterator.findNextByIterator(listIterator);
        objectIterator.hasNext();
        objectIterator.findNextByIterator(listIterator);

        // Expecting IllegalStateException due to invalid remove operation
        try {
            objectIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testRuntimeExceptionOnExceptionTransformer() {
        Predicate<Object> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        Transformer<Object, Predicate<Object>> exceptionTransformer = ExceptionTransformer.exceptionTransformer();

        ObjectGraphIterator<Predicate<Object>> iterator = new ObjectGraphIterator<>(exceptionPredicate, exceptionTransformer);

        // Expecting RuntimeException due to exception transformer
        try {
            iterator.updateCurrentIterator();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoSuchElementExceptionOnNextWithPredicateTransformer() {
        ObjectGraphIterator<Predicate<Object>> predicateIterator = new ObjectGraphIterator<>(null);
        predicateIterator.updateCurrentIterator();

        ObjectGraphIterator<Predicate<Object>> copiedIterator = new ObjectGraphIterator<>(predicateIterator);
        predicateIterator.hasNext();

        Integer initialValue = 5;
        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(null);
        Transformer<Integer, Integer> nullTransformer = ConstantTransformer.nullTransformer();

        mapTransformer.andThen(nullTransformer);

        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, mapTransformer);
        integerIterator.next();

        // Expecting NoSuchElementException due to empty iteration
        try {
            integerIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextWithSingleElement() {
        Integer initialValue = -1265;
        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, null);

        Integer nextValue = iterator.next();
        assertEquals(-1265, (int) nextValue);
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveAfterNext() {
        Integer initialValue = 2;
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, null);
        ObjectGraphIterator<Integer> copiedIterator = new ObjectGraphIterator<>(integerIterator);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(copiedIterator);

        wrappedIterator.next();

        // Expecting IllegalStateException due to invalid remove operation
        try {
            copiedIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveWithComparatorPredicate() {
        Integer initialValue = 566;
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, null);
        ObjectGraphIterator<Integer> copiedIterator = new ObjectGraphIterator<>(integerIterator);

        Object nextValue = copiedIterator.next();
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        ComparatorPredicate.Criterion criterion = ComparatorPredicate.Criterion.LESS_OR_EQUAL;
        Predicate<Object> comparatorPredicate = ComparatorPredicate.comparatorPredicate(nextValue, comparator, criterion);

        OrPredicate.orPredicate(comparatorPredicate, comparatorPredicate);

        // Expecting IllegalStateException due to invalid remove operation
        try {
            copiedIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveWithFactoryTransformer() {
        Object object = new Object();
        Integer initialValue = -350;
        Factory<Integer> constantFactory = ConstantFactory.constantFactory(initialValue);
        FactoryTransformer<Object, Object> factoryTransformer = new FactoryTransformer<>(constantFactory);

        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(object, factoryTransformer);

        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        IdentityPredicate<Object> identityPredicate = new IdentityPredicate<>(initialValue);
        Predicate<Object> nullIsFalsePredicate = NullIsFalsePredicate.nullIsFalsePredicate(identityPredicate);
        predicates[0] = nullIsFalsePredicate;
        predicates[1] = FalsePredicate.falsePredicate();
        predicates[2] = NullIsTruePredicate.nullIsTruePredicate(predicates[1]);
        predicates[3] = AndPredicate.andPredicate(identityPredicate, nullIsFalsePredicate);
        predicates[4] = new EqualPredicate<>(constantFactory);

        Predicate<Object> nonePredicate = NonePredicate.nonePredicate(predicates);
        PredicateTransformer<Transformer<Object, Integer>> predicateTransformer = new PredicateTransformer<>(nonePredicate);

        nonePredicate.test(nullIsFalsePredicate);

        FactoryTransformer<Object, Integer> integerFactoryTransformer = new FactoryTransformer<>(constantFactory);
        Boolean transformedValue = predicateTransformer.transform(integerFactoryTransformer);

        Transformer<Object, Boolean> booleanTransformer = PredicateTransformer.predicateTransformer(nullIsFalsePredicate);
        ObjectGraphIterator<Boolean> booleanIterator1 = new ObjectGraphIterator<>(transformedValue, booleanTransformer);

        nonePredicate.negate();

        ObjectGraphIterator<Boolean> booleanIterator2 = new ObjectGraphIterator<>(transformedValue, booleanTransformer);
        booleanIterator1.findNextByIterator(booleanIterator2);

        objectIterator.findNext(new Object());

        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, integerFactoryTransformer);
        integerIterator.hasNext();

        objectIterator.findNextByIterator(integerIterator);
        objectIterator.next();

        // Expecting IllegalStateException due to invalid remove operation
        try {
            integerIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnFindNextByIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(0);
        ListIterator<Integer> listIterator = list.listIterator();
        list.add(0);

        Integer initialValue = -3572;
        Transformer<Object, Integer> invokerTransformer = InvokerTransformer.invokerTransformer("m'GR6`8pM1Io~)aS");

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, invokerTransformer);

        // Expecting ConcurrentModificationException due to list modification
        try {
            iterator.findNextByIterator(listIterator);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationExceptionOnFindNextByIteratorWithNull() {
        LinkedList<Integer> list = new LinkedList<>();
        ListIterator<Integer> listIterator = list.listIterator();
        list.add(null);

        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(listIterator);

        // Expecting ConcurrentModificationException due to list modification
        try {
            objectIterator.findNextByIterator(listIterator);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextWithSingleElementAndNullTransformer() {
        Integer initialValue = 4;
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, null);
        ObjectGraphIterator<Object> wrappedIterator = new ObjectGraphIterator<>(integerIterator);

        Object nextValue = wrappedIterator.next();
        assertEquals(4, nextValue);
    }

    @Test(timeout = 4000)
    public void testObjectGraphIteratorWithNullBooleanAndNullTransformer() {
        Transformer<Object, Boolean> nullTransformer = ConstantTransformer.nullTransformer();
        ObjectGraphIterator<Boolean> booleanIterator = new ObjectGraphIterator<>(null, nullTransformer);
    }

    @Test(timeout = 4000)
    public void testRuntimeExceptionOnExceptionTransformerNext() {
        Object object = new Object();
        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(object, exceptionTransformer);

        // Expecting RuntimeException due to exception transformer
        try {
            objectIterator.next();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextWithEmptyIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(descendingIterator);

        boolean hasNext = integerIterator.hasNext();
        assertFalse(hasNext);
    }

    @Test(timeout = 4000)
    public void testRuntimeExceptionOnInvokerTransformerHasNext() {
        Integer initialValue = -227;
        Object[] methodArgs = new Object[]{ "?}4!038|{z#HLh9" };
        InvokerTransformer<Integer, Integer> invokerTransformer = new InvokerTransformer<>("?}4!038|{z#HLh9", null, methodArgs);

        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(initialValue, invokerTransformer);

        // Expecting RuntimeException due to invalid method invocation
        try {
            iterator.hasNext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveWithEmptyListIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        ListIterator<Integer> listIterator = list.listIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(listIterator);

        // Expecting IllegalStateException due to invalid remove operation
        try {
            objectIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalStateExceptionOnRemoveWithNullIterator() {
        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(null);

        // Expecting IllegalStateException due to invalid remove operation
        try {
            integerIterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindNextByIteratorWithDifferentIterators() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        Integer initialValue = 3471;
        HashMap<Object, Integer> hashMap = new HashMap<>();
        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(hashMap);

        ObjectGraphIterator<Integer> integerIterator = new ObjectGraphIterator<>(initialValue, mapTransformer);
        integerIterator.updateCurrentIterator();
        integerIterator.findNextByIterator(descendingIterator);

        HashMap<Object, Integer> anotherHashMap = new HashMap<>();
        Integer anotherValue = 1945;
        integerIterator.findNext(anotherValue);

        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);
        objectIterator.findNext(hashMap);

        Integer yetAnotherValue = 1945;
        ObjectGraphIterator<Integer> anotherIntegerIterator = new ObjectGraphIterator<>(yetAnotherValue, mapTransformer);

        integerIterator.findNextByIterator(anotherIntegerIterator);

        assertFalse(anotherIntegerIterator.equals(integerIterator));
    }

    @Test(timeout = 4000)
    public void testNoSuchElementExceptionOnNextWithEmptyIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);

        // Expecting NoSuchElementException due to empty iteration
        try {
            objectIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoSuchElementExceptionOnNextWithParallelStream() {
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        list.parallelStream();

        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(descendingIterator);

        // Expecting NoSuchElementException due to empty iteration
        try {
            objectIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoSuchElementExceptionOnNextWithNullIterator() {
        ObjectGraphIterator<Object> objectIterator = new ObjectGraphIterator<>(null);

        // Expecting NoSuchElementException due to empty iteration
        try {
            objectIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ObjectGraphIterator", e);
        }
    }
}
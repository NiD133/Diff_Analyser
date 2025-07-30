package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class FixedOrderComparator_ESTest extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFixedOrderComparatorEquality() throws Throwable {
        Comparable<Object>[] comparableArray = (Comparable<Object>[]) Array.newInstance(Comparable.class, 8);
        FixedOrderComparator<Comparable<Object>> comparatorWithArray = new FixedOrderComparator<>(comparableArray);
        FixedOrderComparator<Object> emptyComparator = new FixedOrderComparator<>();
        boolean areEqual = emptyComparator.equals(comparatorWithArray);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testComparatorLockingAfterComparison() throws Throwable {
        ConstantTransformer<Boolean, Boolean> constantTransformer = new ConstantTransformer<>(null);
        Function<Boolean, Boolean>[] functionArray = (Function<Boolean, Boolean>[]) Array.newInstance(Function.class, 7);
        functionArray[0] = constantTransformer;
        FixedOrderComparator<Function<Boolean, Boolean>> comparator = new FixedOrderComparator<>(functionArray);
        int comparisonResult = comparator.compare(constantTransformer, functionArray[3]);
        assertTrue(comparator.isLocked());
        assertEquals(-1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testAddAsEqualThrowsExceptionForUnknownObject() throws Throwable {
        Object[] objectArray = new Object[2];
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(objectArray);
        Object unknownObject = new Object();
        Object knownObject = new Object();
        try {
            comparator.addAsEqual(unknownObject, knownObject);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testDefaultUnknownObjectBehavior() throws Throwable {
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        comparator.checkLocked();
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());
    }

    @Test(timeout = 4000)
    public void testComparatorLockingAfterComparisonWithUnknownBehavior() throws Throwable {
        LinkedList<Object> linkedList = new LinkedList<>();
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(linkedList);
        Object unknownObject = new Object();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        comparator.compare(linkedList, unknownObject);
        assertTrue(comparator.isLocked());
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionAfterComparison() throws Throwable {
        LinkedList<Object> linkedList = new LinkedList<>();
        Boolean[] booleanArray = new Boolean[21];
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(linkedList);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        Object unknownObject = new Object();
        comparator.compare(unknownObject, booleanArray[0]);
        try {
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionForNullUnknownObjectBehavior() throws Throwable {
        LinkedList<FixedOrderComparator<Object>> linkedList = new LinkedList<>();
        FixedOrderComparator<FixedOrderComparator<Object>> comparator = new FixedOrderComparator<>(linkedList);
        try {
            comparator.setUnknownObjectBehavior(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnCheckLockedAfterComparison() throws Throwable {
        LinkedList<Object> linkedList = new LinkedList<>();
        Boolean[] booleanArray = new Boolean[21];
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(linkedList);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        Object unknownObject = new Object();
        comparator.compare(unknownObject, booleanArray[0]);
        try {
            comparator.checkLocked();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnAddAfterComparison() throws Throwable {
        LinkedList<Object> linkedList = new LinkedList<>();
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(linkedList);
        Object knownObject = new Object();
        IdentityPredicate<Object> identityPredicate = new IdentityPredicate<>(knownObject);
        PredicateTransformer<Comparable<Boolean>> predicateTransformer = new PredicateTransformer<>(identityPredicate);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        Boolean transformedBoolean = predicateTransformer.transform(false);
        comparator.compare(transformedBoolean, knownObject);
        try {
            comparator.add(linkedList);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorForCircularReference() throws Throwable {
        LinkedList<Object> linkedList = new LinkedList<>();
        Object[] objectArray = new Object[9];
        objectArray[5] = linkedList;
        linkedList.add(objectArray[5]);
        try {
            new FixedOrderComparator<>(objectArray);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionForNullArray() throws Throwable {
        try {
            new FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior>((FixedOrderComparator.UnknownObjectBehavior[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionForNullList() throws Throwable {
        try {
            new FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior>((List<FixedOrderComparator.UnknownObjectBehavior>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddReturnsFalseForExistingElement() throws Throwable {
        FixedOrderComparator<Object>[] comparatorArray = (FixedOrderComparator<Object>[]) Array.newInstance(FixedOrderComparator.class, 5);
        LinkedList<Object> linkedList = new LinkedList<>();
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(linkedList);
        comparatorArray[0] = comparator;
        FixedOrderComparator<FixedOrderComparator<Object>> nestedComparator = new FixedOrderComparator<>(comparatorArray);
        boolean added = nestedComparator.add(comparator);
        assertFalse(added);
    }

    @Test(timeout = 4000)
    public void testAddReturnsTrueForNewElement() throws Throwable {
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        Object newObject = new Object();
        boolean added = comparator.add(newObject);
        assertTrue(added);
    }

    @Test(timeout = 4000)
    public void testHashCodeComputation() throws Throwable {
        LinkedList<Comparable<Object>> linkedList = new LinkedList<>();
        FixedOrderComparator<Comparable<Object>> comparator = new FixedOrderComparator<>(linkedList);
        comparator.hashCode(); // Just to ensure no exceptions are thrown
    }

    @Test(timeout = 4000)
    public void testIsLockedReturnsFalseInitially() throws Throwable {
        FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior> comparator = new FixedOrderComparator<>();
        assertFalse(comparator.isLocked());
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForDifferentComparators() throws Throwable {
        FixedOrderComparator<Object>[] comparatorArray = (FixedOrderComparator<Object>[]) Array.newInstance(FixedOrderComparator.class, 4);
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(comparatorArray);
        comparatorArray[0] = comparator;
        FixedOrderComparator<FixedOrderComparator<Object>> nestedComparator = new FixedOrderComparator<>(comparatorArray);
        boolean areEqual = nestedComparator.equals(comparator);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForDifferentTypes() throws Throwable {
        Boolean[] booleanArray = new Boolean[21];
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        Object unknownObject = new Object();
        comparator.compare(unknownObject, booleanArray[0]);
        FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior> nestedComparator = new FixedOrderComparator<>();
        boolean areEqual = nestedComparator.equals(comparator);
        assertTrue(comparator.isLocked());
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForNull() throws Throwable {
        FixedOrderComparator<FixedOrderComparator<Object>> comparator = new FixedOrderComparator<>();
        boolean areEqual = comparator.equals(null);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsTrueForSameInstance() throws Throwable {
        LinkedList<Boolean> linkedList = new LinkedList<>();
        FixedOrderComparator<Boolean> comparator = new FixedOrderComparator<>(linkedList);
        boolean areEqual = comparator.equals(comparator);
        assertTrue(areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsReturnsFalseForDifferentClass() throws Throwable {
        Class<Object> objectClass = Object.class;
        FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior> comparator = new FixedOrderComparator<>();
        boolean areEqual = comparator.equals(objectClass);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testCompareWithNullAndUnknownBehaviorAfter() throws Throwable {
        Boolean[] booleanArray = new Boolean[21];
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(booleanArray);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        int comparisonResult = comparator.compare(null, FixedOrderComparator.UnknownObjectBehavior.AFTER);
        assertTrue(comparator.isLocked());
        assertEquals(-1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testCompareWithSameObjectAndUnknownBehaviorBefore() throws Throwable {
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        int comparisonResult = comparator.compare(comparator, comparator);
        assertTrue(comparator.isLocked());
        assertEquals(0, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testCompareWithNullAndUnknownBehaviorBefore() throws Throwable {
        FixedOrderComparator<Boolean>[] comparatorArray = (FixedOrderComparator<Boolean>[]) Array.newInstance(FixedOrderComparator.class, 4);
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(comparatorArray);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        Object unknownObject = new Object();
        int comparisonResult = comparator.compare(null, unknownObject);
        assertTrue(comparator.isLocked());
        assertEquals(1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testCompareWithNullAndUnknownBehaviorAfter() throws Throwable {
        Predicate<Object>[] predicateArray = (Predicate<Object>[]) Array.newInstance(Predicate.class, 14);
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(predicateArray);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        int comparisonResult = comparator.compare(comparator, null);
        assertTrue(comparator.isLocked());
        assertEquals(1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForUnknownObjectComparison() throws Throwable {
        Predicate<Object>[] predicateArray = (Predicate<Object>[]) Array.newInstance(Predicate.class, 14);
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(predicateArray);
        try {
            comparator.compare(comparator, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetUnknownObjectBehavior() throws Throwable {
        FixedOrderComparator<Predicate<Object>> comparator = new FixedOrderComparator<>();
        FixedOrderComparator.UnknownObjectBehavior behavior = comparator.getUnknownObjectBehavior();
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, behavior);
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnAddAsEqualAfterComparison() throws Throwable {
        FixedOrderComparator<FixedOrderComparator.UnknownObjectBehavior> comparator = new FixedOrderComparator<>();
        LinkedList<Object> linkedList = new LinkedList<>();
        FixedOrderComparator<Object> objectComparator = new FixedOrderComparator<>(linkedList);
        objectComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        objectComparator.compare(comparator, comparator);
        try {
            objectComparator.addAsEqual(FixedOrderComparator.UnknownObjectBehavior.AFTER, comparator);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }
}
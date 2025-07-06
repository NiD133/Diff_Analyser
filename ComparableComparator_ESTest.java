package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ComparableComparatorTest extends ComparableComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCompareGreaterThan() {
        ComparableComparator<Integer> comparator = new ComparableComparator<>();
        Integer larger = 491;
        Integer smaller = 0;
        int result = comparator.compare(larger, smaller);
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testCompareLessThan() {
        ComparableComparator<Integer> comparator = ComparableComparator.comparableComparator();
        Integer smaller = 0;
        Integer larger = 1;
        int result = comparator.compare(smaller, larger);
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testCompareWithNullThrowsException() {
        ComparableComparator<Integer> comparator = ComparableComparator.comparableComparator();
        Integer nonNull = 5;
        try {
            comparator.compare(null, nonNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparableComparator", e);
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() {
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();
        Object differentObject = new Object();
        boolean isEqual = comparator.equals(differentObject);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull() {
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();
        boolean isEqual = comparator.equals(null);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameType() {
        ComparableComparator<ComparatorPredicate.Criterion> comparator1 = new ComparableComparator<>();
        ComparableComparator<ComparatorPredicate.Criterion> comparator2 = ComparableComparator.comparableComparator();
        boolean isEqual = comparator1.equals(comparator2);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() {
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();
        boolean isEqual = comparator.equals(comparator);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testCompareEqualObjects() {
        ComparableComparator<Integer> comparator = new ComparableComparator<>();
        Integer value = 1;
        int result = comparator.compare(value, value);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testHashCode() {
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();
        comparator.hashCode(); // No assertion needed, just testing method execution
    }
}
package org.apache.commons.collections4.comparators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.collections4.functors.ComparatorPredicate;

public class ComparableComparatorTest {

    @Test
    void testComparePositive() {
        // Arrange
        ComparableComparator<Integer> comparator = new ComparableComparator<>();
        Integer value1 = 491;
        Integer value2 = 0;

        // Act
        int result = comparator.compare(value1, value2);

        // Assert
        assertEquals(1, result, "Should return 1 because value1 is greater than value2.");
    }

    @Test
    void testCompareNegative() {
        // Arrange
        ComparableComparator<Integer> comparator = ComparableComparator.comparableComparator();
        Integer value1 = 1;
        Integer value2 = 0;

        // Act
        int result = comparator.compare(value2, value1);

        // Assert
        assertEquals(-1, result, "Should return -1 because value2 is less than value1.");
    }

    @Test
    void testCompareNullFirstArgument() {
        // Arrange
        ComparableComparator<Integer> comparator = ComparableComparator.comparableComparator();
        Integer value = 5;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            comparator.compare(null, value);
        }, "Should throw NullPointerException when the first argument is null.");
    }

    @Test
    void testEqualsDifferentObjectType() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();
        Object otherObject = new Object();

        // Act
        boolean result = comparator.equals(otherObject);

        // Assert
        assertFalse(result, "Should return false because the other object is not a ComparableComparator.");
    }

    @Test
    void testEqualsNullObject() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();

        // Act
        boolean result = comparator.equals(null);

        // Assert
        assertFalse(result, "Should return false because the other object is null.");
    }

    @Test
    void testEqualsSameComparatorType() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparator1 = new ComparableComparator<>();
        ComparableComparator<ComparatorPredicate.Criterion> comparator2 = ComparableComparator.comparableComparator();

        // Act
        boolean result = comparator1.equals(comparator2);

        // Assert
        assertTrue(result, "Should return true because both comparators are of the same type.");
    }

    @Test
    void testEqualsSameInstance() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();

        // Act
        boolean result = comparator.equals(comparator);

        // Assert
        assertTrue(result, "Should return true because the object is compared with itself.");
    }

    @Test
    void testCompareEqual() {
        // Arrange
        ComparableComparator<Integer> comparator = new ComparableComparator<>();
        Integer value = 1;

        // Act
        int result = comparator.compare(value, value);

        // Assert
        assertEquals(0, result, "Should return 0 because both values are equal.");
    }

    @Test
    void testHashCode() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparator = new ComparableComparator<>();

        // Act & Assert
        assertDoesNotThrow(() -> comparator.hashCode(), "Should not throw an exception when calculating hashCode.");
    }
}
package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.functors.ComparatorPredicate;

public class ComparableComparatorTest {

    @Test
    public void testCompare_WithLargerFirstValue_ReturnsPositive() {
        // Arrange
        ComparableComparator<Integer> comparableComparator = new ComparableComparator<>();
        Integer largerValue = 491;
        Integer smallerValue = 0;

        // Act
        int result = comparableComparator.compare(largerValue, smallerValue);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testCompare_WithSmallerFirstValue_ReturnsNegative() {
        // Arrange
        ComparableComparator<Integer> comparableComparator = ComparableComparator.comparableComparator();
        Integer smallerValue = 1;
        Integer largerValue = 0;

        // Act
        int result = comparableComparator.compare(smallerValue, largerValue);

        // Assert
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompare_WithNullFirstValue_ThrowsNullPointerException() {
        // Arrange
        ComparableComparator<Integer> comparableComparator = ComparableComparator.comparableComparator();
        Integer secondValue = 5;

        // Act
        comparableComparator.compare(null, secondValue);
    }

    @Test
    public void testEquals_WithDifferentObject_ReturnsFalse() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparableComparator = new ComparableComparator<>();
        Object differentObject = new Object();

        // Act
        boolean result = comparableComparator.equals(differentObject);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparableComparator = new ComparableComparator<>();

        // Act
        boolean result = comparableComparator.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEquals_WithSameInstance_ReturnsTrue() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparableComparator = new ComparableComparator<>();
        ComparableComparator<ComparatorPredicate.Criterion> sameInstance = comparableComparator;

        // Act
        boolean result = comparableComparator.equals(sameInstance);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEquals_WithSameTypeInstance_ReturnsTrue() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparableComparator = new ComparableComparator<>();
        ComparableComparator<ComparatorPredicate.Criterion> sameTypeInstance = ComparableComparator.comparableComparator();

        // Act
        boolean result = comparableComparator.equals(sameTypeInstance);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCompare_WithEqualValues_ReturnsZero() {
        // Arrange
        ComparableComparator<Integer> comparableComparator = new ComparableComparator<>();
        Integer value = 1;

        // Act
        int result = comparableComparator.compare(value, value);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testHashCode_DoesNotThrowAnyException() {
        // Arrange
        ComparableComparator<ComparatorPredicate.Criterion> comparableComparator = new ComparableComparator<>();

        // Act and Assert
        comparableComparator.hashCode();
    }
}
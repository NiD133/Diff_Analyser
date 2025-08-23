package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for the factory methods of {@link DateTimeComparator}.
 *
 * <p>This version has been refactored for clarity by:
 * <ul>
 *   <li>Migrating from JUnit 3 to JUnit 5.</li>
 *   <li>Removing all unused fields, helper methods, and setup/teardown logic.</li>
 *   <li>Splitting the original test method into two focused tests with descriptive names.</li>
 *   <li>Applying the Arrange-Act-Assert pattern for better structure.</li>
 * </ul>
 */
class DateTimeComparatorTest {

    @Test
    @DisplayName("getInstance(lowerLimit) should return a comparator with the correct lower limit and a null upper limit")
    void getInstance_withLowerLimit_returnsComparatorWithCorrectLimits() {
        // Arrange
        DateTimeFieldType lowerLimit = DateTimeFieldType.hourOfDay();
        String expectedToString = "DateTimeComparator[hourOfDay-]";

        // Act
        DateTimeComparator comparator = DateTimeComparator.getInstance(lowerLimit);

        // Assert
        assertEquals(lowerLimit, comparator.getLowerLimit(), "The lower limit should be correctly set.");
        assertNull(comparator.getUpperLimit(), "The upper limit should be null when only a lower limit is provided.");
        assertEquals(expectedToString, comparator.toString(), "The toString() representation should reflect the specified limits.");
    }

    @Test
    @DisplayName("getInstance(null) should return the same singleton instance as getInstance()")
    void getInstance_withNullArgument_returnsSameSingletonAsNoArgument() {
        // Arrange
        // The Joda-Time documentation states that getInstance(null) is
        // equivalent to getInstance(). This test verifies that they return the
        // same singleton instance.

        // Act
        DateTimeComparator comparatorFromNullArg = DateTimeComparator.getInstance(null);
        DateTimeComparator comparatorFromNoArg = DateTimeComparator.getInstance();

        // Assert
        assertSame(comparatorFromNoArg, comparatorFromNullArg,
                "getInstance(null) should return the same singleton instance as getInstance().");
    }
}
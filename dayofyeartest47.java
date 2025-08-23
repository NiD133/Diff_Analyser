package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the compareTo method in {@link DayOfYear}.
 */
public class DayOfYearCompareToTest {

    @Test
    void compareTo_withNullArgument_throwsNullPointerException() {
        // Arrange: Create an instance of DayOfYear to test against.
        DayOfYear dayOfYear = DayOfYear.of(1);

        // Act & Assert: The Comparable contract requires throwing a NullPointerException
        // when comparing with a null object.
        assertThrows(NullPointerException.class, () -> dayOfYear.compareTo(null));
    }
}
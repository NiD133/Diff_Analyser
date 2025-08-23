package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the isAfter() method in the AbstractPartial class.
 */
public class AbstractPartialIsAfterTest {

    /**
     * Verifies that a partial date-time is not considered to be "after" itself.
     */
    @Test
    public void isAfter_shouldReturnFalse_whenComparingAnInstanceWithItself() {
        // Arrange
        // LocalDateTime is a concrete implementation of ReadablePartial, suitable for
        // testing the behavior inherited from AbstractPartial.
        LocalDateTime aDateTime = LocalDateTime.now();

        // Act
        boolean result = aDateTime.isAfter(aDateTime);

        // Assert
        assertFalse("A partial instance should never be considered after itself.", result);
    }
}
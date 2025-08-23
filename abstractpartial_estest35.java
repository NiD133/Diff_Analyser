package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on comparison methods.
 */
public class AbstractPartialTest {

    /**
     * Tests that the isEqual() method returns true when a partial instant
     * is compared to itself.
     */
    @Test
    public void isEqual_shouldReturnTrue_whenComparingPartialToItself() {
        // Arrange
        // LocalDateTime is a concrete implementation of AbstractPartial, suitable for this test.
        ReadablePartial partial = LocalDateTime.now();

        // Act & Assert
        // An object should always be equal to itself. This verifies the reflexive
        // property of the isEqual method.
        assertTrue("A partial instance should be equal to itself.", partial.isEqual(partial));
    }
}
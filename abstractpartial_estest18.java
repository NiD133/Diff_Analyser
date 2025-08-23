package org.joda.time.base;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link AbstractPartial} class.
 */
public class AbstractPartialTest {

    /**
     * Tests that getFieldType() throws an IndexOutOfBoundsException when called with a negative index.
     */
    @Test
    public void getFieldType_shouldThrowIndexOutOfBoundsException_forNegativeIndex() {
        // Arrange
        // LocalDate is a concrete implementation of AbstractPartial suitable for this test.
        LocalDate partial = new LocalDate();
        int invalidIndex = -1;

        // Act & Assert
        try {
            partial.getFieldType(invalidIndex);
            fail("Expected an IndexOutOfBoundsException but none was thrown.");
        } catch (IndexOutOfBoundsException e) {
            // Success: The expected exception was thrown.
            // We can also verify the exception message for more precise testing.
            assertEquals("Invalid index: " + invalidIndex, e.getMessage());
        }
    }
}
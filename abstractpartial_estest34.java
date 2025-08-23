package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link AbstractPartial#isEqual(ReadablePartial)} method.
 */
public class AbstractPartialTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that isEqual() throws an IllegalArgumentException when the provided
     * partial is null, as per the method's contract.
     */
    @Test
    public void isEqual_whenComparingWithNull_throwsIllegalArgumentException() {
        // Arrange: Create an instance of a class that extends AbstractPartial.
        LocalDateTime partial = LocalDateTime.now();

        // Arrange: Set up the expectation for an exception.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Partial cannot be null");

        // Act: Call the method with a null argument, which should trigger the exception.
        partial.isEqual(null);

        // Assert: The ExpectedException rule automatically verifies the exception and its message.
    }
}
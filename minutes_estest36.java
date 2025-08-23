package org.joda.time;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that minutesBetween() throws an exception when both start and end partials are null.
     * The method must not accept null arguments.
     */
    @Test
    public void minutesBetween_withNullReadablePartials_throwsIllegalArgumentException() {
        // Arrange: Define the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ReadablePartial objects must not be null");

        // Act: Call the method with null arguments, which should trigger the exception.
        // The explicit cast is necessary to resolve ambiguity between method overloads.
        Minutes.minutesBetween((ReadablePartial) null, (ReadablePartial) null);

        // Assert: The ExpectedException rule handles the assertion. If the correct
        // exception is not thrown, the test will fail automatically.
    }
}
package org.joda.time;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that weeksBetween() throws an IllegalArgumentException when both
     * start and end ReadablePartial arguments are null.
     */
    @Test
    public void weeksBetween_withNullReadablePartials_throwsIllegalArgumentException() {
        // Arrange: Define the expected exception type and message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("ReadablePartial objects must not be null"));

        // Act: Call the method under test with null arguments.
        // This call is expected to throw the exception defined above.
        Weeks.weeksBetween((ReadablePartial) null, (ReadablePartial) null);
    }
}
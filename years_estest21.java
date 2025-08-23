package org.joda.time;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Years} class.
 */
public class YearsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that yearsBetween() throws an IllegalArgumentException
     * when both start and end partials are null, as per the method's contract.
     */
    @Test
    public void yearsBetween_withNullPartials_throwsIllegalArgumentException() {
        // Arrange: Define the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ReadablePartial objects must not be null");

        // Act: Call the method that should trigger the exception.
        Years.yearsBetween(null, null);

        // Assert: The ExpectedException rule automatically verifies that the
        // specified exception was thrown. If not, the test fails.
    }
}
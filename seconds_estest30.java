package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class Seconds_ESTestTest30 extends Seconds_ESTest_scaffolding {

    /**
     * Tests that secondsBetween() throws an IllegalArgumentException
     * when both ReadablePartial arguments are null.
     */
    @Test
    public void secondsBetween_withNullPartials_shouldThrowIllegalArgumentException() {
        try {
            // Act: Call the method with null arguments
            Seconds.secondsBetween((ReadablePartial) null, (ReadablePartial) null);
            
            // Assert: Fail the test if no exception is thrown
            fail("Expected an IllegalArgumentException to be thrown for null partials.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the exception message is correct
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }
}
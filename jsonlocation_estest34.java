package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that appending the offset description for the special "Not Available" (NA)
     * location results in a specific "UNKNOWN" placeholder text.
     */
    @Test
    public void appendOffsetDescriptionForNALocationShouldAppendUnknownByteOffset() {
        // Arrange: Set up the test conditions.
        // JsonLocation.NA represents an unknown or unavailable location.
        JsonLocation naLocation = JsonLocation.NA;
        StringBuilder stringBuilder = new StringBuilder();
        String expectedDescription = "byte offset: #UNKNOWN";

        // Act: Call the method under test.
        naLocation.appendOffsetDescription(stringBuilder);

        // Assert: Verify the outcome.
        // The StringBuilder should now contain the expected placeholder text.
        assertEquals(expectedDescription, stringBuilder.toString());
    }
}
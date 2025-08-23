package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on verifying the behavior of the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that the source description for the special 'Not Available' (NA)
     * location constant is "UNKNOWN". This is the expected behavior as of Jackson 2.9.
     */
    @Test
    public void sourceDescriptionForNALocationShouldReturnUnknown() {
        // Arrange: The JsonLocation.NA constant represents a non-available location.
        final String expectedDescription = "UNKNOWN";

        // Act: Get the source description from the NA constant.
        final String actualDescription = JsonLocation.NA.sourceDescription();

        // Assert: The description should match the expected value.
        assertEquals("The source description for JsonLocation.NA should be 'UNKNOWN'",
                expectedDescription, actualDescription);
    }
}
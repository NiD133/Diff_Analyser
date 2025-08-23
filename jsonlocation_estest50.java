package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the special 'Not Applicable' (NA) JsonLocation instance
     * correctly reports its byte offset as -1, which signifies that the
     * location information is not available.
     */
    @Test
    public void getByteOffset_forNaLocation_shouldReturnNegativeOne() {
        // Arrange: The special 'Not Applicable' location constant.
        JsonLocation naLocation = JsonLocation.NA;

        // Act: Retrieve the byte offset from the NA location.
        long byteOffset = naLocation.getByteOffset();

        // Assert: The byte offset should be -1 to indicate it's not available.
        assertEquals("The byte offset for JsonLocation.NA should be -1.", -1L, byteOffset);
    }
}
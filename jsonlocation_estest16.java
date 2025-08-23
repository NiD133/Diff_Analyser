package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Verifies that the special 'Not Available' (NA) instance of JsonLocation
     * returns -1 for the column number, as per its definition.
     */
    @Test
    public void getColumnNr_forNotAvailableLocation_shouldReturnNegativeOne() {
        // Arrange: The JsonLocation.NA constant represents an unavailable location.
        JsonLocation notAvailableLocation = JsonLocation.NA;

        // Act: Retrieve the column number from the NA location.
        int columnNumber = notAvailableLocation.getColumnNr();

        // Assert: The column number should be -1, indicating it is not available.
        assertEquals(-1, columnNumber);
    }
}
package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Changed from junit.Test to junit.jupiter.api.Test for better readability/compatibility
import static org.junit.jupiter.api.Assertions.*; // Changed import for JUnit 5 assertions

public class ByteOrderMarkMatchesTest { // Renamed class for clarity

    @Test
    public void testUTF8DoesNotMatchEmptyArray() { // Renamed test method for clarity

        // Arrange: Define a byte array (represented as an integer array here) with a single element, initialized to 0.
        int[] byteArray = new int[1];

        // Arrange: Get the UTF-8 Byte Order Mark.  This represents the expected starting bytes for a UTF-8 encoded file.
        ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Act: Call the 'matches' method to check if the byte order mark matches the provided byte array.
        boolean matches = utf8Bom.matches(byteArray);

        // Assert: Verify that the byte order mark does *not* match the byte array. Because the array is filled with zeros and doesn't match the UTF-8 BOM.
        assertFalse(matches, "UTF-8 Byte Order Mark should not match a byte array with only a zero value."); // Added a message to the assertion for better error reporting.
    }
}
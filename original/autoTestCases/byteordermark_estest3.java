package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Using JUnit 5 annotations
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

public class ByteOrderMarkComparisonTest {

    @Test
    void testUTF16LE_equals_UTF32LE_returnsFalse() {
        // Arrange: Define two different ByteOrderMark constants.
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;

        // Act: Compare the two ByteOrderMark instances using the equals() method.
        boolean areEqual = utf16LE.equals(utf32LE);

        // Assert:  Ensure that the equals() method returns false, as the two marks are different.
        assertFalse(areEqual, "UTF-16LE and UTF-32LE ByteOrderMarks should not be considered equal.");
    }
}
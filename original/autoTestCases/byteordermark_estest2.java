package org.apache.commons.io;

import org.junit.jupiter.api.Test;  // Changed from org.junit.Test to org.junit.jupiter.api.Test (JUnit 5)
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 Assertions

public class ByteOrderMarkComparisonTest { // More descriptive class name

    @Test
    public void testDifferentByteOrderMarksAreNotEqual() { // Descriptive test method name
        // Arrange (Setup)
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;

        // Act (Perform the action)
        boolean areEqual = utf16BE.equals(utf16LE);

        // Assert (Verify the result)
        assertFalse(areEqual, "UTF-16BE and UTF-16LE ByteOrderMarks should not be equal.");
    }
}
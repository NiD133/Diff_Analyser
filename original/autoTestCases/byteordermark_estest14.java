package org.apache.commons.io;

import org.junit.jupiter.api.Test;  // Changed to JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5 assertions

public class ByteOrderMarkMatchingTest { // Renamed the class for clarity

    @Test
    void testUTF16BEMatchesNullArrayReturnsFalse() {
        // Arrange:  Create a ByteOrderMark for UTF-16BE.
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act: Attempt to match the ByteOrderMark against a null integer array.
        boolean matches = utf16BE.matches((int[]) null);

        // Assert:  Expect that the match fails (returns false) because the input is null.
        assertFalse(matches, "UTF-16BE should not match a null integer array.");
    }
}
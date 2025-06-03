package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Modern JUnit 5 import
import static org.junit.jupiter.api.Assertions.assertFalse; // Modern JUnit 5 import

public class ByteOrderMarkEqualityTest {

    @Test
    public void testUTF32BE_notEquals_UTF16BE() {
        // Arrange: Create two different ByteOrderMark instances.  Descriptive names make it clear what we are testing.
        ByteOrderMark utf32BE = ByteOrderMark.UTF_32BE;
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act:  Compare the two ByteOrderMarks using the equals method.
        boolean areEqual = utf32BE.equals(utf16BE);

        // Assert: Verify that the two ByteOrderMarks are not equal.  The assertion message clarifies the expectation.
        assertFalse(areEqual, "UTF-32BE and UTF-16BE ByteOrderMarks should not be equal.");
    }
}
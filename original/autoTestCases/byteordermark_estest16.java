package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Using JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

public class ByteOrderMarkHashCodeTest { // Renamed class for better clarity

    @Test
    void testUTF8ByteOrderMarkHashCode() { // More descriptive method name
        // Arrange: Create a ByteOrderMark instance for UTF-8
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_8;

        // Act:  Implicitly call hashCode() by not assigning or comparing the result.
        // We're primarily testing that it *doesn't* throw an exception.

        // Assert: The test passes if the hashCode() method executes without error.
        // Implicitly, the test checks if hashCode() is implemented and returns a value.
        // No explicit assertion needed here, as any exception thrown by hashCode() would fail the test.
        byteOrderMark.hashCode();  // Crucial line, calling the method under test.
    }
}
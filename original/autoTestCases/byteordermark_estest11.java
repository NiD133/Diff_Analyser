package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability

import static org.junit.jupiter.api.Assertions.assertEquals; // Using JUnit 5 assertions
import org.apache.commons.io.ByteOrderMark; // Explicitly import the class under test


class ByteOrderMarkTest { // Renamed class for clarity and to follow standard naming conventions

    @Test
    void testUTF8ByteOrderMarkToString() { // More descriptive test name
        // Arrange: Create a ByteOrderMark instance for UTF-8
        ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Act: Get the string representation of the ByteOrderMark
        String bomString = utf8Bom.toString();

        // Assert: Verify that the string representation is as expected
        assertEquals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", bomString, "The toString() method should return the expected string representation.");
    }
}
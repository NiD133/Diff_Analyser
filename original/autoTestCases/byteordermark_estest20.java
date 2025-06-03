package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ByteOrderMarkTest {

    @Test
    void testEquals_UTF8_withDifferentObjectType() {
        // Arrange: We create a ByteOrderMark for UTF-8.
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_8;

        // Act: We compare the ByteOrderMark with a String object. The expected behavior is that the 'equals' method should return false
        //      when comparing with an object of a different type.
        boolean isEqual = byteOrderMark.equals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]");

        // Assert: We assert that the result of the comparison is false.  A ByteOrderMark should not be equal to a String, even if the String's
        //         content represents the ByteOrderMark's string representation.
        assertFalse(isEqual, "A ByteOrderMark should not be equal to a String.");
    }
}
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteOrderMark#hashCode()}.
 */
@DisplayName("ByteOrderMark hashCode()")
public class ByteOrderMarkHashCodeTest {

    @Test
    @DisplayName("should be calculated as the class hash code plus the sum of its byte values")
    void hashCodeIsCalculatedFromClassHashAndSumOfBytes() {
        // This is a white-box test that verifies the specific, non-standard hashCode() implementation.
        // The implementation calculates the hash code as: ByteOrderMark.class.hashCode() + sum of bytes.

        // Arrange
        final ByteOrderMark bomWithOneByte = new ByteOrderMark("test1", 1);
        final ByteOrderMark bomWithTwoBytes = new ByteOrderMark("test2", 1, 2);
        final ByteOrderMark bomWithThreeBytes = new ByteOrderMark("test3", 1, 2, 3);

        final int classHashCode = ByteOrderMark.class.hashCode();

        // Act & Assert
        assertEquals(classHashCode + 1, bomWithOneByte.hashCode());
        assertEquals(classHashCode + (1 + 2), bomWithTwoBytes.hashCode());
        assertEquals(classHashCode + (1 + 2 + 3), bomWithThreeBytes.hashCode());
    }
}
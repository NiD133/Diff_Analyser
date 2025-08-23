package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteOrderMark#equals(Object)} and {@link ByteOrderMark#hashCode()}.
 * This suite focuses on verifying the contract of these methods.
 */
@DisplayName("ByteOrderMark equals() and hashCode()")
class ByteOrderMarkEqualsTest {

    @Test
    @DisplayName("should return true for the same instance (reflexivity)")
    void equals_sameInstance_isTrue() {
        // A non-null reference value must be equal to itself.
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);
    }

    @Test
    @DisplayName("should return true for different instances with identical values")
    void equals_identicalValues_isTrue() {
        // Create a new instance with the same values as the UTF-8 BOM constant
        final ByteOrderMark bom1 = new ByteOrderMark("UTF-8", 0xEF, 0xBB, 0xBF);
        final ByteOrderMark bom2 = new ByteOrderMark("UTF-8", 0xEF, 0xBB, 0xBF);

        assertEquals(bom1, bom2);
    }

    @Test
    @DisplayName("should be symmetrical")
    void equals_isSymmetrical() {
        final ByteOrderMark bom1 = new ByteOrderMark("Test", 1, 2);
        final ByteOrderMark bom2 = new ByteOrderMark("Test", 1, 2);
        final ByteOrderMark bom3 = new ByteOrderMark("Different", 1, 2);

        // If x.equals(y) is true, then y.equals(x) must be true.
        assertTrue(bom1.equals(bom2));
        assertTrue(bom2.equals(bom1));

        // If x.equals(y) is false, then y.equals(x) must be false.
        assertFalse(bom1.equals(bom3));
        assertFalse(bom3.equals(bom1));
    }

    @Test
    @DisplayName("should have consistent hash codes for equal objects")
    void hashCode_isConsistentForEqualObjects() {
        final ByteOrderMark bom1 = new ByteOrderMark("UTF-8", 0xEF, 0xBB, 0xBF);
        final ByteOrderMark bom2 = new ByteOrderMark("UTF-8", 0xEF, 0xBB, 0xBF);

        assertEquals(bom1, bom2, "Precondition: objects must be equal");
        assertEquals(bom1.hashCode(), bom2.hashCode(), "Equal objects must have equal hash codes");
    }

    @Nested
    @DisplayName("should return false when compared to")
    class EqualsIsFalseTests {

        @Test
        @DisplayName("a null object")
        void equals_null_isFalse() {
            // For any non-null reference value x, x.equals(null) must return false.
            assertFalse(ByteOrderMark.UTF_8.equals(null));
        }

        @Test
        @DisplayName("an object of a different type")
        void equals_differentType_isFalse() {
            assertFalse(ByteOrderMark.UTF_8.equals("a string"));
        }

        @Test
        @DisplayName("a BOM with a different charset name")
        void equals_differentCharsetName_isFalse() {
            final ByteOrderMark bom1 = new ByteOrderMark("UTF-8-A", 0xEF, 0xBB, 0xBF);
            final ByteOrderMark bom2 = new ByteOrderMark("UTF-8-B", 0xEF, 0xBB, 0xBF);
            assertNotEquals(bom1, bom2);
        }

        @Test
        @DisplayName("a BOM with different byte content")
        void equals_differentBytesContent_isFalse() {
            final ByteOrderMark bom1 = new ByteOrderMark("Test", 1, 2, 3);
            final ByteOrderMark bom2 = new ByteOrderMark("Test", 1, 2, 4);
            assertNotEquals(bom1, bom2);
        }

        @Test
        @DisplayName("a BOM with a different byte array length")
        void equals_differentByteLength_isFalse() {
            final ByteOrderMark bom1 = new ByteOrderMark("Test", 1, 2);
            final ByteOrderMark bom2 = new ByteOrderMark("Test", 1, 2, 3);
            assertNotEquals(bom1, bom2);
        }

        @Test
        @DisplayName("different standard BOM constants")
        void equals_differentStandardBoms_isFalse() {
            assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
            assertNotEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE);
            assertNotEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE);
        }
    }
}
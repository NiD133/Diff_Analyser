package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the hashCode() method adheres to its contract.
     * Specifically, it verifies that:
     * 1. The hash code is consistent across multiple invocations.
     * 2. Equal objects produce equal hash codes.
     */
    @Test
    public void testHashCodeContract() {
        // Arrange
        // Create a standard BOM instance from the public constant.
        final ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Create another BOM instance that is logically equal to the first one.
        final ByteOrderMark equalUtf8Bom = new ByteOrderMark("UTF-8", 0xEF, 0xBB, 0xBF);

        // Create a different BOM instance for inequality comparison.
        final ByteOrderMark differentBom = ByteOrderMark.UTF_16BE;

        // Assert

        // 1. Consistency: hashCode() must return the same value on the same object.
        assertEquals("Hash code must be consistent across multiple calls.", utf8Bom.hashCode(), utf8Bom.hashCode());

        // 2. Equality: Equal objects must have equal hash codes.
        // First, confirm the objects are indeed equal.
        assertEquals("BOMs with the same charset and bytes should be equal.", utf8Bom, equalUtf8Bom);
        // Then, confirm their hash codes are equal.
        assertEquals("Equal objects must have equal hash codes.", utf8Bom.hashCode(), equalUtf8Bom.hashCode());

        // 3. Inequality (for completeness): Check that unequal objects do not have the same hash code.
        // While not a strict requirement (collisions can occur), it's a good property to test.
        assertNotEquals("Unequal objects should not be equal.", utf8Bom, differentBom);
        assertNotEquals("Unequal objects should ideally have different hash codes.", utf8Bom.hashCode(), differentBom.hashCode());
    }
}
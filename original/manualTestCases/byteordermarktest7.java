package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteOrderMark} class.  Focuses on the hashCode method.
 */
public class ByteOrderMarkHashCodeTest {

    // Example ByteOrderMark instances for testing (replace with actual instances)
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("UTF-8"); // Example, adjust constructor args
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("UTF-16BE"); // Example, adjust constructor args
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("UTF-32LE"); // Example, adjust constructor args

    /**
     * Tests the {@link ByteOrderMark#hashCode()} method.
     *
     * This test verifies that the hashCode method produces distinct and consistent
     * hash codes for different ByteOrderMark instances.  It leverages the
     * fact that the hashCode is partially based on the class's hashCode.
     */
    @Test
    public void testHashCode() {
        // The hashCode calculation depends on the class's own hashCode.
        final int bomClassHash = ByteOrderMark.class.hashCode();

        // Assert that the calculated hash codes for different BOMs are as expected.
        // The specific values (bomClassHash + 1, etc.) are tied to the original
        // implementation of the hashCode method within ByteOrderMark.
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode(), "HashCode for BOM 1 is incorrect");
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode(), "HashCode for BOM 2 is incorrect");
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode(), "HashCode for BOM 3 is incorrect");
    }
}
package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Tests for the {@link ByteUtils} class, focusing on handling of invalid inputs.
 */
public class ByteUtils_ESTestTest23 extends ByteUtils_ESTest_scaffolding {

    /**
     * Verifies that fromLittleEndian throws a NullPointerException when the input array is null.
     */
    @Test(expected = NullPointerException.class)
    public void fromLittleEndianWithNullArrayThrowsNullPointerException() {
        // The offset and length values (8, 8) are arbitrary for this test,
        // as the method should throw an exception due to the null array
        // before these parameters are used.
        ByteUtils.fromLittleEndian(null, 8, 8);
    }
}
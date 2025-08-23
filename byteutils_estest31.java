package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.DataInput;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Verifies that the fromLittleEndian(DataInput, int) method throws a
     * NullPointerException when the DataInput argument is null. This ensures
     * the method correctly handles invalid input by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void fromLittleEndianWithNullDataInputThrowsNullPointerException() {
        // The 'length' argument can be any valid integer (e.g., 1), as the
        // null check on the DataInput parameter should be performed first.
        ByteUtils.fromLittleEndian((DataInput) null, 1);
    }
}
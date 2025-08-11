package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for CpioUtil.
 *
 * Grouped by API method and intent, with descriptive names and assertions.
 * Notes:
 * - Some behaviors reflect the current implementation quirks (e.g., AIOOBE on empty array).
 *   If the implementation is later hardened with explicit validation, update the expectations accordingly.
 */
public class CpioUtilTest {

    // ----------------------------
    // fileType
    // ----------------------------

    @Test
    public void fileType_returnsZeroWhenNoTypeBits() {
        assertEquals(0L, CpioUtil.fileType(0L));
    }

    @Test
    public void fileType_preservesFileTypeBits() {
        assertEquals(61440L, CpioUtil.fileType(61440L));
    }

    // ----------------------------
    // byteArray2long
    // ----------------------------

    @Test
    public void byteArray2long_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CpioUtil.byteArray2long(null, true));
    }

    @Test
    public void byteArray2long_emptyArray_throwsArrayIndexOutOfBoundsException() {
        // Current implementation throws AIOOBE on empty input rather than validating length.
        byte[] empty = new byte[0];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> CpioUtil.byteArray2long(empty, false));
    }

    @Test
    public void byteArray2long_oddLength_throwsUnsupportedOperationException() {
        // As per javadoc: length must be a multiple of two.
        byte[] odd = new byte[3];
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.byteArray2long(odd, true));
    }

    @Test
    public void byteArray2long_fourZeroBytes_returnsZero() {
        byte[] fourZeros = new byte[4];
        assertEquals(0L, CpioUtil.byteArray2long(fourZeros, false));
    }

    @Test
    public void byteArray2long_eightBytes_noSwap_matchesCurrentImplementation() {
        // Only the second byte is non-zero; expectation matches current implementation behavior.
        byte[] bytes = new byte[8];
        bytes[1] = (byte) -51; // 0xCD
        assertEquals(-3674937295934324736L, CpioUtil.byteArray2long(bytes, false));
    }

    // ----------------------------
    // long2byteArray
    // ----------------------------

    @Test
    public void long2byteArray_negativeLength_throwsNegativeArraySizeException() {
        // Negative length triggers JVM-level NegativeArraySizeException before implementation checks.
        assertThrows(NegativeArraySizeException.class, () -> CpioUtil.long2byteArray(0L, -3182, true));
    }

    @Test
    public void long2byteArray_zeroLength_throwsUnsupportedOperationException() {
        // As per javadoc: length must be a positive multiple of two.
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(3061L, 0, true));
    }

    @Test
    public void long2byteArray_lengthNotMultipleOfTwo_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(701L, 701, false));
    }

    @Test
    public void long2byteArray_twoBytes_noSwap_isLittleEndian() {
        // With length=2 and no swap, current behavior is little-endian: least significant byte first.
        byte[] out = CpioUtil.long2byteArray(2L, 2, false);
        assertArrayEquals(new byte[] { 2, 0 }, out);
    }

    @Test
    public void long2byteArray_largeLength_returnsRequestedSize() {
        byte[] out = CpioUtil.long2byteArray(-3070L, 146, false);
        assertEquals(146, out.length);
    }
}
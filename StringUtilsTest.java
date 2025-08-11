package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized tests for StringUtils.convertCharsToBytes(char[]).
 *
 * Purpose:
 * - Verifies that each 16-bit Java char is converted to two bytes in big-endian order:
 *   result[0] = high byte (bits 15..8), result[1] = low byte (bits 7..0).
 * - Covers typical characters, Arabic sample, and boundaries around surrogate ranges.
 */
@RunWith(Parameterized.class)
public class StringUtilsConvertCharsToBytesTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            caseEntry("Null character", '\u0000', 0x00, 0x00),
            caseEntry("Backspace", '\b', 0x00, 0x08),
            caseEntry("Lowercase letter 'a'", 'a', 0x00, 0x61),
            caseEntry("Arabic letter 'ة'", 'ة', 0x06, 0x29),
            caseEntry("Just below surrogate range start", '\ud7ff', 0xD7, 0xFF),
            caseEntry("Surrogate range start", '\ud800', 0xD8, 0x00),
            caseEntry("Inside surrogate range", '\uda82', 0xDA, 0x82),
            caseEntry("Inside surrogate range", '\udbb0', 0xDB, 0xB0),
            caseEntry("Surrogate range end", '\udfff', 0xDF, 0xFF),
            caseEntry("Just above surrogate range end", '\ue000', 0xE0, 0x00),
            caseEntry("Replacement character", '\ufffd', 0xFF, 0xFD),
            caseEntry("Maximum char value", '\uffff', 0xFF, 0xFF),
        });
    }

    private final String description;
    private final char inputChar;
    private final byte expectedHigh;
    private final byte expectedLow;

    public StringUtilsConvertCharsToBytesTest(String description, char inputChar, byte expectedHigh, byte expectedLow) {
        this.description = description;
        this.inputChar = inputChar;
        this.expectedHigh = expectedHigh;
        this.expectedLow = expectedLow;
    }

    @Test
    public void convertSingleCharToBytes_returnsHighAndLowByteInBigEndianOrder() {
        // Given
        char[] chars = { inputChar };

        // When
        byte[] actual = StringUtils.convertCharsToBytes(chars);

        // Then
        String failMsg = String.format(
            "%s. Input %s. Expected [%s, %s] but got [%s, %s].",
            description, toHex(inputChar),
            toHex(expectedHigh), toHex(expectedLow),
            toHex(actual[0]), toHex(actual[1])
        );
        Assert.assertArrayEquals(failMsg, new byte[]{expectedHigh, expectedLow}, actual);
    }

    // ---- helpers ----

    private static Object[] caseEntry(String desc, char ch, int high, int low) {
        String name = String.format("%s (input=%s, expected=[0x%02X 0x%02X])",
                desc, toHex(ch), high, low);
        return new Object[]{ name, ch, (byte) high, (byte) low };
    }

    private static String toHex(char c) {
        return String.format("U+%04X", (int) c);
    }

    private static String toHex(byte b) {
        return String.format("0x%02X", b & 0xFF);
    }
}
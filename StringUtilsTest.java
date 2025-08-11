package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Verifies that {@link StringUtils#convertCharsToBytes(char[])} correctly converts
 * a 16-bit character into its high and low byte representation.
 * <p>
 * This is a parameterized test that covers various character types, including
 * control characters, ASCII, non-ASCII, and special Unicode ranges like surrogates.
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {

    /**
     * Provides test data for the parameterized tests.
     * Each entry contains:
     * 1. The input character.
     * 2. The expected high byte of the character.
     * 3. The expected low byte of the character.
     * 4. A description of the test case.
     *
     * @return A collection of test data.
     */
    @Parameters(name = "{index}: {3} (char: ''\\u{0,x}'')")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {'\u0000', (byte) 0x00, (byte) 0x00, "Null character"},
                {'\b', (byte) 0x00, (byte) 0x08, "Backspace control character"},
                {'a', (byte) 0x00, (byte) 0x61, "ASCII character 'a'"},
                {'ة', (byte) 0x06, (byte) 0x29, "Arabic character"},
                {'\ud7ff', (byte) 0xd7, (byte) 0xff, "Character before high surrogates"},
                {'\ud800', (byte) 0xd8, (byte) 0x00, "First high surrogate character"},
                {'\uda82', (byte) 0xda, (byte) 0x82, "Middle high surrogate character"},
                {'\udbb0', (byte) 0xdb, (byte) 0xb0, "Last high surrogate character"},
                {'\udfff', (byte) 0xdf, (byte) 0xff, "Last low surrogate character"},
                {'\ue000', (byte) 0xe0, (byte) 0x00, "Character after low surrogates (Private Use Area)"},
                {'\ufffd', (byte) 0xff, (byte) 0xfd, "Unicode replacement character"},
                {'\uffff', (byte) 0xff, (byte) 0xff, "Non-character U+FFFF"}
        });
    }

    private final char inputChar;
    private final byte expectedHighByte;
    private final byte expectedLowByte;

    /**
     * Constructs a new test instance with the given parameters.
     *
     * @param inputChar        The character to be converted.
     * @param expectedHighByte The expected high byte of the resulting byte array.
     * @param expectedLowByte  The expected low byte of the resulting byte array.
     * @param description      A description of the test case (used by the test runner).
     */
    public StringUtilsTest(char inputChar, byte expectedHighByte, byte expectedLowByte, String description) {
        this.inputChar = inputChar;
        this.expectedHighByte = expectedHighByte;
        this.expectedLowByte = expectedLowByte;
        // The 'description' parameter is used by the @Parameters(name=...) annotation for readable test reports.
    }

    @Test
    public void convertCharsToBytes_shouldSplitCharIntoHighAndLowBytes() {
        // Given: An input character array and the expected byte representation.
        char[] inputCharArray = {inputChar};
        byte[] expectedBytes = {expectedHighByte, expectedLowByte};

        // When: The character array is converted to a byte array.
        byte[] actualBytes = StringUtils.convertCharsToBytes(inputCharArray);

        // Then: The resulting byte array should match the expected bytes.
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }
}
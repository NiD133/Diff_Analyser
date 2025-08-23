/*
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized unit tests for the method StringUtils::convertCharsToBytes.
 * This test suite verifies that characters are correctly converted to their byte representations.
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {

    // Parameters for the test: each entry contains a character and its expected byte representation.
    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
            {'\u0000', (byte) 0x0, (byte) 0x0},   // Null character
            {'\b', (byte) 0x0, (byte) 0x08},      // Backspace
            {'a', (byte) 0x0, (byte) 0x61},       // Lowercase 'a'
            {'ة', (byte) 0x06, (byte) 0x29},      // Arabic character
            {'\ud7ff', (byte) 0xd7, (byte) 0xff}, // Just outside a special Unicode range
            {'\ud800', (byte) 0xd8, (byte) 0x0},  // Start of a special Unicode range
            {'\uda82', (byte) 0xda, (byte) 0x82}, // Within a special Unicode range
            {'\udbb0', (byte) 0xdb, (byte) 0xb0}, // Within a special Unicode range
            {'\udfff', (byte) 0xdf, (byte) 0xff}, // End of a special Unicode range
            {'\ue000', (byte) 0xe0, (byte) 0x0},  // Just outside a special Unicode range
            {'\ufffd', (byte) 0xff, (byte) 0xfd}, // Replacement character
            {'\uffff', (byte) 0xff, (byte) 0xff}, // Maximum Unicode value
        });
    }

    private final char inputChar;
    private final byte expectedByte1;
    private final byte expectedByte2;

    /**
     * Constructor for parameterized test, initializes input character and expected byte values.
     *
     * @param inputChar     the character to convert
     * @param expectedByte1 the first byte of the expected result
     * @param expectedByte2 the second byte of the expected result
     */
    public StringUtilsTest(char inputChar, byte expectedByte1, byte expectedByte2) {
        this.inputChar = inputChar;
        this.expectedByte1 = expectedByte1;
        this.expectedByte2 = expectedByte2;
    }

    /**
     * Test method for StringUtils::convertCharsToBytes.
     * Verifies that the conversion from a character to a byte array is correct.
     */
    @Test
    public void testConvertCharsToBytes() {
        // Expected byte array result
        byte[] expectedBytes = {expectedByte1, expectedByte2};

        // Convert the input character to a byte array using the method under test
        char[] inputChars = {inputChar};
        byte[] actualBytes = StringUtils.convertCharsToBytes(inputChars);

        // Assert that the actual byte array matches the expected byte array
        Assert.assertArrayEquals("Byte conversion failed for character: " + inputChar, expectedBytes, actualBytes);
    }
}
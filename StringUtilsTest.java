/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
 * Parameterized unit tests for StringUtils.convertCharsToBytes() method.
 * 
 * This test verifies that 16-bit Unicode characters are correctly converted
 * to their 2-byte representation (high byte + low byte).
 *
 * @author benoit
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {

    // Unicode surrogate pair range: U+D800 to U+DFFF
    private static final char SURROGATE_RANGE_START = '\ud800';
    private static final char SURROGATE_RANGE_END = '\udfff';

    @Parameters(name = "char={0} (U+{1}) -> bytes=[0x{2}, 0x{3}]")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
            // Test case format: {inputChar, expectedHighByte, expectedLowByte}
            
            // Basic ASCII and control characters
            createTestCase('\u0000', "NULL character"),
            createTestCase('\b', "backspace character"),
            createTestCase('a', "lowercase 'a'"),
            
            // Non-ASCII Unicode characters
            createTestCase('ة', "Arabic letter Teh Marbuta"),
            
            // Unicode boundary testing - just outside surrogate range
            createTestCase('\ud7ff', "last character before surrogate range"),
            createTestCase('\ue000', "first character after surrogate range"),
            
            // Unicode surrogate range (U+D800 to U+DFFF)
            // These are typically used for UTF-16 encoding of characters beyond Basic Multilingual Plane
            createTestCase('\ud800', "start of surrogate range"),
            createTestCase('\uda82', "middle of surrogate range"),
            createTestCase('\udbb0', "middle of surrogate range"),
            createTestCase('\udfff', "end of surrogate range"),
            
            // High Unicode values
            createTestCase('\ufffd', "Unicode replacement character"),
            createTestCase('\uffff', "maximum Unicode value in Basic Multilingual Plane")
        });
    }

    /**
     * Helper method to create test cases with clearer byte calculation
     */
    private static Object[] createTestCase(char inputChar, String description) {
        int charValue = (int) inputChar;
        byte highByte = (byte) ((charValue >> 8) & 0xFF);  // Extract high 8 bits
        byte lowByte = (byte) (charValue & 0xFF);          // Extract low 8 bits
        return new Object[]{inputChar, highByte, lowByte};
    }

    // Test parameters
    private final char inputCharacter;
    private final byte expectedHighByte;
    private final byte expectedLowByte;

    public StringUtilsTest(char inputCharacter, byte expectedHighByte, byte expectedLowByte) {
        this.inputCharacter = inputCharacter;
        this.expectedHighByte = expectedHighByte;
        this.expectedLowByte = expectedLowByte;
    }

    @Test
    public void shouldConvertSingleCharacterToTwoByteArray() {
        // Given: a single character input
        char[] inputChars = {inputCharacter};
        byte[] expectedBytes = {expectedHighByte, expectedLowByte};

        // When: converting chars to bytes
        byte[] actualBytes = StringUtils.convertCharsToBytes(inputChars);

        // Then: should produce correct 2-byte representation
        Assert.assertArrayEquals(
            String.format("Character '%c' (U+%04X) should convert to bytes [0x%02X, 0x%02X]",
                inputCharacter, (int) inputCharacter, expectedHighByte, expectedLowByte),
            expectedBytes, 
            actualBytes
        );
    }

    @Test
    public void shouldProduceArrayTwiceTheInputSize() {
        // Given: a single character input
        char[] inputChars = {inputCharacter};

        // When: converting chars to bytes
        byte[] result = StringUtils.convertCharsToBytes(inputChars);

        // Then: output array should be twice the size of input array
        Assert.assertEquals("Output byte array should be twice the size of input char array",
            inputChars.length * 2, result.length);
    }
}
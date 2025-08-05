/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    private static final int DATA_SIZE = 10000;
    private byte[] testData;
    private RandomAccessFileOrArray randomAccess;

    @Before
    public void setUp() throws IOException {
        // Arrange: Create a predictable byte array for testing.
        // The values will be 0, 1, 2, ..., 255, 0, 1, ... due to byte casting.
        ByteArrayOutputStream os = new ByteArrayOutputStream(DATA_SIZE);
        for (int i = 0; i < DATA_SIZE; i++) {
            os.write(i);
        }
        testData = os.toByteArray();

        // Create the object under test from the byte array source.
        randomAccess = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(testData));
    }

    /**
     * Verifies that the read() method sequentially reads all bytes from the source
     * and returns -1 at the end of the stream.
     */
    @Test
    public void testSequentialRead_readsAllBytes() throws IOException {
        for (int i = 0; i < testData.length; i++) {
            assertEquals("Byte at position " + i + " should match the source data",
                    testData[i], (byte) randomAccess.read());
        }
        assertEquals("Should return -1 at the end of the stream", -1, randomAccess.read());
    }

    /**
     * Verifies that seek() moves the file pointer and that subsequent reads
     * start from the new position.
     */
    @Test
    public void testSeek_movesPointerForSubsequentReads() throws IOException {
        // Arrange
        final long seekPosition = 72L;

        // Act
        randomAccess.seek(seekPosition);

        // Assert that reading starts from the seek position.
        for (int i = (int) seekPosition; i < testData.length; i++) {
            assertEquals("Byte at position " + i + " should match the source data after seek",
                    testData[i], (byte) randomAccess.read());
        }
    }

    /**
     * Verifies that pushBack() places a byte back into the stream, which is
     * returned by the next read, and that subsequent reads continue from the
     * original position.
     */
    @Test
    public void testPushback_replacesNextReadAndPreservesStream() throws IOException {
        // Arrange: Read the first two bytes and prepare a byte to push back.
        byte originalSecondByte = testData[1];
        byte expectedThirdByte = testData[2];
        randomAccess.read(); // Advance pointer to position 1
        randomAccess.read(); // Advance pointer to position 2

        final byte pushedBackByte = (byte) 0xFE;
        // Sanity check to ensure the test is meaningful.
        assertNotEquals("Pushed back byte should be different from the original byte for test validity",
                originalSecondByte, pushedBackByte);

        // Act: Push the byte back into the stream.
        randomAccess.pushBack(pushedBackByte);

        // Assert:
        // 1. The next read should return the pushed-back byte.
        assertEquals("Read after pushBack should return the pushed back byte",
                pushedBackByte, (byte) randomAccess.read());

        // 2. The following read should return the byte that was originally after the pushed-back position.
        assertEquals("Read after pushed back byte should resume from the original stream",
                expectedThirdByte, (byte) randomAccess.read());
    }

    /**
     * Verifies that calling pushBack() decrements the file pointer by one.
     */
    @Test
    public void testPushback_decrementsFilePointer() throws IOException {
        // Arrange
        final long initialOffset = 72L;
        randomAccess.seek(initialOffset);
        assertEquals("File pointer should be at initial offset after seek",
                initialOffset, randomAccess.getFilePointer());

        final byte byteToPushBack = (byte) 0x2A; // 42

        // Act
        randomAccess.pushBack(byteToPushBack);

        // Assert
        long expectedPosition = initialOffset - 1;
        assertEquals("File pointer should be decremented by 1 after pushBack",
                expectedPosition, randomAccess.getFilePointer());
    }
}
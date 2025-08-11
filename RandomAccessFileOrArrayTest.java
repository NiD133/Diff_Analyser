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

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Test suite for RandomAccessFileOrArray functionality.
 * Tests core operations: sequential reading, seeking, and pushback functionality.
 */
public class RandomAccessFileOrArrayTest {
    
    // Test data constants
    private static final int TEST_DATA_SIZE = 10000;
    private static final int SEEK_POSITION = 72;
    private static final byte PUSHBACK_OFFSET = 42;
    
    // Test fixtures
    private byte[] testData;
    private RandomAccessFileOrArray randomAccessArray;
    
    @Before
    public void setUp() throws Exception {
        testData = createTestData();
        randomAccessArray = createRandomAccessArray(testData);
    }

    @After
    public void tearDown() throws Exception {
        // No cleanup needed - objects will be garbage collected
    }

    /**
     * Test that pushBack() correctly inserts a byte that will be read next,
     * without affecting subsequent reads from the original data.
     */
    @Test
    public void testPushbackInsertsCorrectByteInReadSequence() throws Exception {
        // Read first two bytes normally
        byte firstByte = (byte) randomAccessArray.read();
        byte secondByte = (byte) randomAccessArray.read();
        
        Assert.assertEquals("First byte should match test data", testData[0], firstByte);
        Assert.assertEquals("Second byte should match test data", testData[1], secondByte);
        
        // Push back a different value
        byte pushedBackValue = (byte) (testData[1] + PUSHBACK_OFFSET);
        randomAccessArray.pushBack(pushedBackValue);
        
        // Verify pushback value is read next, then normal sequence continues
        Assert.assertEquals("Next read should return pushed back value", 
                           pushedBackValue, (byte) randomAccessArray.read());
        Assert.assertEquals("Following read should continue from original position", 
                           testData[2], (byte) randomAccessArray.read());
        Assert.assertEquals("Subsequent read should follow original sequence", 
                           testData[3], (byte) randomAccessArray.read());
    }

    /**
     * Test that sequential reading returns all bytes in correct order.
     */
    @Test
    public void testSequentialReadingReturnsAllBytesInOrder() throws Exception {
        for (int position = 0; position < testData.length; position++) {
            byte expectedByte = testData[position];
            byte actualByte = (byte) randomAccessArray.read();
            
            Assert.assertEquals(
                String.format("Byte at position %d should match test data", position),
                expectedByte, 
                actualByte
            );
        }
    }

    /**
     * Test that seek() correctly positions the file pointer and subsequent reads
     * start from the new position.
     */
    @Test
    public void testSeekPositionsFilePointerCorrectly() throws Exception {
        RandomAccessFileOrArray seekTestArray = createRandomAccessArray(testData);
        
        seekTestArray.seek(SEEK_POSITION);
        
        // Verify all reads after seek start from the correct position
        for (int position = SEEK_POSITION; position < testData.length; position++) {
            byte expectedByte = testData[position];
            byte actualByte = (byte) seekTestArray.read();
            
            Assert.assertEquals(
                String.format("After seeking to %d, byte at position %d should match", 
                             SEEK_POSITION, position),
                expectedByte, 
                actualByte
            );
        }
    }
    
    /**
     * Test that getFilePointer() correctly reports position changes after seek() and pushBack().
     */
    @Test
    public void testFilePointerReflectsPositionChangesCorrectly() throws Exception {
        RandomAccessFileOrArray positionTestArray = createRandomAccessArray(testData);
        
        // Test position after seek
        positionTestArray.seek(SEEK_POSITION);
        Assert.assertEquals("File pointer should reflect seek position", 
                           SEEK_POSITION, positionTestArray.getFilePointer());
        
        // Test position after pushback (should move back by 1)
        byte pushbackValue = PUSHBACK_OFFSET;
        positionTestArray.pushBack(pushbackValue);
        long expectedPositionAfterPushback = SEEK_POSITION - 1;
        
        Assert.assertEquals("File pointer should move back by 1 after pushback", 
                           expectedPositionAfterPushback, positionTestArray.getFilePointer());
    }
    
    // Helper methods
    
    /**
     * Creates test data array with predictable byte values for testing.
     * Each byte value is the lower 8 bits of its index position.
     */
    private byte[] createTestData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            outputStream.write(i); // Automatically truncates to byte
        }
        return outputStream.toByteArray();
    }
    
    /**
     * Creates a RandomAccessFileOrArray instance from the given byte array.
     */
    private RandomAccessFileOrArray createRandomAccessArray(byte[] data) {
        RandomAccessSourceFactory sourceFactory = new RandomAccessSourceFactory();
        return new RandomAccessFileOrArray(sourceFactory.createSource(data));
    }
}
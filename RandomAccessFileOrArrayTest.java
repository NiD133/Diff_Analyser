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

import static org.junit.Assert.assertEquals;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class RandomAccessFileOrArrayTest {
    // Test constants
    private static final int TEST_DATA_SIZE = 10000;
    private static final int SEEK_POSITION = 72;
    private static final byte PUSHBACK_OFFSET = 42;
    
    private byte[] data;
    private RandomAccessFileOrArray rafoa;
    
    @Before
    public void setUp() throws IOException {
        // Create test data: [0, 1, 2, ..., 9999] mod 256
        data = new byte[TEST_DATA_SIZE];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        rafoa = new RandomAccessFileOrArray(
            new RandomAccessSourceFactory().createSource(data)
        );
    }

    @Test
    public void pushBack_WhenPushingBackByte_ThenNextReadReturnsPushedByte() 
            throws Exception {
        // Read first two bytes
        assertEquals("First byte should match", data[0], (byte) rafoa.read());
        assertEquals("Second byte should match", data[1], (byte) rafoa.read());
        
        // Push back modified second byte
        byte pushBackVal = (byte) (data[1] + PUSHBACK_OFFSET);
        rafoa.pushBack(pushBackVal);
        
        // Verify pushback and subsequent reads
        assertEquals("Pushed back value should be read", pushBackVal, (byte) rafoa.read());
        assertEquals("Third byte should follow", data[2], (byte) rafoa.read());
        assertEquals("Fourth byte should follow", data[3], (byte) rafoa.read());
    }

    @Test
    public void read_WhenReadingEntireFile_ThenContentMatchesExpected() 
            throws Exception {
        // Read and verify entire data array
        for (int i = 0; i < data.length; i++) {
            assertEquals(
                "Byte at position " + i + " should match", 
                data[i], 
                (byte) rafoa.read()
            );
        }
    }

    @Test
    public void seek_WhenSeekingToPosition_ThenReadsFromThatPosition() 
            throws Exception {
        // Move to specific position
        rafoa.seek(SEEK_POSITION);
        
        // Verify reads start from seek position
        for (int i = SEEK_POSITION; i < data.length; i++) {
            assertEquals(
                "Byte at position " + i + " should match", 
                data[i], 
                (byte) rafoa.read()
            );
        }
    }
    
    @Test
    public void getFilePointer_AfterPushBack_ThenPositionDecreasesByOne() 
            throws Exception {
        // Move to specific position
        rafoa.seek(SEEK_POSITION);
        assertEquals("Initial position should be SEEK_POSITION", 
            SEEK_POSITION, rafoa.getFilePointer());
        
        // Push back a byte and verify position
        byte pushbackVal = 42;
        rafoa.pushBack(pushbackVal);
        assertEquals("Position should decrement after pushback", 
            SEEK_POSITION - 1, rafoa.getFilePointer());
    }
}
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
package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for GetBufferedRandomAccessSource to ensure proper handling of edge cases
 * and correct buffering behavior.
 */
public class GetBufferedRandomAccessSourceTest {

    private static final byte EXPECTED_BYTE_VALUE = 42;
    private static final int FIRST_POSITION = 0;

    /**
     * Tests that GetBufferedRandomAccessSource can handle very small data sources
     * without throwing ArrayIndexOutOfBoundsException.
     * 
     * This is a regression test for a bug where sources with less than 4 bytes
     * would cause array index out of bounds errors during get() operations.
     */
    @Test
    public void testGetFromSingleByteSource_ShouldReturnCorrectValue() throws Exception {
        // Given: A data source containing only one byte
        byte[] singleByteData = createSingleByteArray(EXPECTED_BYTE_VALUE);
        ArrayRandomAccessSource underlyingSource = new ArrayRandomAccessSource(singleByteData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);
        
        // When: Reading the byte at position 0
        int actualValue = bufferedSource.get(FIRST_POSITION);
        
        // Then: The correct byte value should be returned
        assertEquals("Should return the correct byte value from single-byte source", 
                    EXPECTED_BYTE_VALUE, actualValue);
    }

    /**
     * Tests that GetBufferedRandomAccessSource works correctly with empty data sources.
     */
    @Test
    public void testGetFromEmptySource_ShouldHandleGracefully() throws Exception {
        // Given: An empty data source
        byte[] emptyData = new byte[0];
        ArrayRandomAccessSource underlyingSource = new ArrayRandomAccessSource(emptyData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);
        
        // When/Then: Attempting to read should not crash (specific behavior depends on implementation)
        // This test ensures the buffered source can be created with empty data without exceptions
        assertEquals("Empty source should have length 0", 0, bufferedSource.length());
    }

    /**
     * Tests that GetBufferedRandomAccessSource works correctly with multi-byte sources.
     */
    @Test
    public void testGetFromMultiByteSource_ShouldReturnCorrectValues() throws Exception {
        // Given: A data source with multiple bytes
        byte[] multiByteData = {10, 20, 30, 40, 50};
        ArrayRandomAccessSource underlyingSource = new ArrayRandomAccessSource(multiByteData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);
        
        // When/Then: Reading different positions should return correct values
        assertEquals("Should return correct value at position 0", 10, bufferedSource.get(0));
        assertEquals("Should return correct value at position 2", 30, bufferedSource.get(2));
        assertEquals("Should return correct value at position 4", 50, bufferedSource.get(4));
    }

    /**
     * Helper method to create a single-byte array with the specified value.
     * 
     * @param value the byte value to store in the array
     * @return a new byte array containing only the specified value
     */
    private byte[] createSingleByteArray(byte value) {
        return new byte[]{value};
    }
}
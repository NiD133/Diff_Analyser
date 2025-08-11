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

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for GroupedRandomAccessSource which combines multiple RandomAccessSource instances
 * into a single contiguous data source.
 */
public class GroupedRandomAccessSourceTest {
    
    // Test data constants
    private static final int TEST_DATA_SIZE = 100;
    private static final int EXPECTED_TOTAL_SIZE = 300; // 3 sources * 100 bytes each
    
    private byte[] testData;
    
    @Before
    public void setUp() throws Exception {
        testData = createTestData();
    }

    @After
    public void tearDown() throws Exception {
        // No cleanup needed
    }

    /**
     * Creates test data: byte array containing values 0-99
     */
    private byte[] createTestData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            baos.write((byte) i);
        }
        return baos.toByteArray();
    }

    /**
     * Tests single byte access across multiple grouped sources.
     * 
     * Setup: 3 sources, each containing bytes 0-99
     * Expected layout:
     * - Source 1: positions 0-99
     * - Source 2: positions 100-199  
     * - Source 3: positions 200-299
     */
    @Test
    public void testSingleByteAccess() throws Exception {
        // Arrange
        GroupedRandomAccessSource groupedSource = createThreeSourceGroup();
        
        // Act & Assert - Test total length
        assertEquals("Total length should be sum of all sources", 
                     EXPECTED_TOTAL_SIZE, groupedSource.length());

        // Test reading from first source (positions 0-99)
        assertEquals("Should read last byte from first source", 
                     (byte) 99, groupedSource.get(99));
        
        // Test reading from second source (positions 100-199)
        assertEquals("Should read first byte from second source", 
                     (byte) 0, groupedSource.get(100));
        assertEquals("Should read second byte from second source", 
                     (byte) 1, groupedSource.get(101));
        
        // Test reading from third source (positions 200-299)
        assertEquals("Should read last byte from third source", 
                     (byte) 99, groupedSource.get(299));

        // Test reading beyond bounds
        assertEquals("Should return -1 for position beyond end", 
                     -1, groupedSource.get(300));
    }

    /**
     * Tests bulk byte array reading across source boundaries.
     */
    @Test
    public void testBulkByteArrayAccess() throws Exception {
        // Arrange
        GroupedRandomAccessSource groupedSource = createThreeSourceGroup();
        byte[] outputBuffer = new byte[500];

        // Act & Assert - Read entire content
        int bytesRead = groupedSource.get(0, outputBuffer, 0, 300);
        assertEquals("Should read all 300 bytes", 300, bytesRead);
        
        // Verify each source's data is correctly positioned
        assertArrayEquals("First 100 bytes should match test data", 
                         testData, copyRange(outputBuffer, 0, TEST_DATA_SIZE));
        assertArrayEquals("Second 100 bytes should match test data", 
                         testData, copyRange(outputBuffer, 100, TEST_DATA_SIZE));
        assertArrayEquals("Third 100 bytes should match test data", 
                         testData, copyRange(outputBuffer, 200, TEST_DATA_SIZE));
        
        // Test reading beyond bounds
        bytesRead = groupedSource.get(0, outputBuffer, 0, 301);
        assertEquals("Should still only read 300 bytes when requesting 301", 
                     300, bytesRead);
        
        // Test reading across source boundary (middle of second source to middle of third)
        bytesRead = groupedSource.get(150, outputBuffer, 0, 100);
        assertEquals("Should read 100 bytes starting from position 150", 
                     100, bytesRead);
        
        // Verify the cross-boundary read
        byte[] expectedFirstHalf = createRangeArray(50, 50); // bytes 50-99 from second source
        byte[] expectedSecondHalf = createRangeArray(0, 50);  // bytes 0-49 from third source
        
        assertArrayEquals("First half should be bytes 50-99", 
                         expectedFirstHalf, copyRange(outputBuffer, 0, 50));
        assertArrayEquals("Second half should be bytes 0-49", 
                         expectedSecondHalf, copyRange(outputBuffer, 50, 50));
    }

    /**
     * Tests the source management lifecycle - ensuring sources are properly
     * opened and closed as the grouped source switches between them.
     */
    @Test
    public void testSourceLifecycleManagement() throws Exception {
        // Arrange - Create a grouped source with lifecycle tracking
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(testData),
            new ArrayRandomAccessSource(testData), 
            new ArrayRandomAccessSource(testData)
        };
        
        SourceLifecycleTracker lifecycleTracker = new SourceLifecycleTracker();
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources) {
            @Override
            protected void sourceReleased(RandomAccessSource source) throws java.io.IOException {
                lifecycleTracker.onSourceReleased(source);
            }
            
            @Override
            protected void sourceInUse(RandomAccessSource source) throws java.io.IOException {
                lifecycleTracker.onSourceInUse(source);
            }
        };

        // Act & Assert - Test source switching behavior
        
        // Access third source (positions 200-299)
        groupedSource.get(250);
        groupedSource.get(251);
        assertEquals("Should have exactly one source open", 1, lifecycleTracker.getOpenCount());
        
        // Switch to second source (positions 100-199)
        groupedSource.get(150);
        groupedSource.get(151);
        assertEquals("Should still have exactly one source open after switch", 1, lifecycleTracker.getOpenCount());
        
        // Switch to first source (positions 0-99)
        groupedSource.get(50);
        groupedSource.get(51);
        assertEquals("Should still have exactly one source open", 1, lifecycleTracker.getOpenCount());
        
        // Switch back to second source
        groupedSource.get(150);
        groupedSource.get(151);
        assertEquals("Should maintain one open source", 1, lifecycleTracker.getOpenCount());
        
        // Switch back to third source
        groupedSource.get(250);
        groupedSource.get(251);
        assertEquals("Should maintain one open source", 1, lifecycleTracker.getOpenCount());

        // Cleanup
        groupedSource.close();
    }

    // Helper methods
    
    /**
     * Creates a GroupedRandomAccessSource with three identical sources.
     */
    private GroupedRandomAccessSource createThreeSourceGroup() {
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(testData), // positions 0-99
            new ArrayRandomAccessSource(testData), // positions 100-199
            new ArrayRandomAccessSource(testData)  // positions 200-299
        };
        return new GroupedRandomAccessSource(sources);
    }

    /**
     * Creates a byte array with sequential values starting from a given offset.
     */
    private byte[] createRangeArray(int startValue, int count) {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = (byte) (i + startValue);
        }
        return result;
    }
    
    /**
     * Copies a range of bytes from a source array to a new array.
     */
    private byte[] copyRange(byte[] source, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(source, offset, result, 0, length);
        return result;
    }

    /**
     * Helper class to track source lifecycle events during testing.
     */
    private static class SourceLifecycleTracker {
        private RandomAccessSource currentSource;
        private int openCount = 0;
        
        public void onSourceReleased(RandomAccessSource source) {
            openCount--;
            if (currentSource != source) {
                fail("Released source should be the current source");
            }
            currentSource = null;
        }
        
        public void onSourceInUse(RandomAccessSource source) {
            if (currentSource != null) {
                fail("Current source should be released before opening new one");
            }
            openCount++;
            currentSource = source;
        }
        
        public int getOpenCount() {
            return openCount;
        }
    }
}
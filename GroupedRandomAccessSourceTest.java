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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GroupedRandomAccessSourceTest {
    private static final int DATA_SIZE = 100;
    private byte[] data;

    @Before
    public void setUp() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < DATA_SIZE; i++) {
            baos.write((byte) i);
        }
        data = baos.toByteArray();
    }

    @Test
    public void totalLengthShouldBeSumOfAllSources() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            long expectedTotalLength = 3 * data.length;
            assertEquals(expectedTotalLength, groupedSource.length());
        }
    }

    @Test
    public void getByte_shouldReadFromFirstSource() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            // Last byte of first source (index 99)
            byte expected = data[DATA_SIZE - 1];
            assertEquals(expected, groupedSource.get(DATA_SIZE - 1));
        }
    }

    @Test
    public void getByte_shouldReadFromSecondSource() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            // First byte of second source (offset 100)
            byte expected = data[0];
            assertEquals(expected, groupedSource.get(DATA_SIZE));
        }
    }

    @Test
    public void getByte_shouldReadFromThirdSource() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            // Last byte of third source (offset 299)
            byte expected = data[DATA_SIZE - 1];
            assertEquals(expected, groupedSource.get(3 * DATA_SIZE - 1));
        }
    }

    @Test
    public void getByte_shouldReturnMinusOneAfterLastByte() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            assertEquals(-1, groupedSource.get(3 * DATA_SIZE));
        }
    }

    @Test
    public void getArray_shouldReadFullRangeAcrossSources() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            byte[] output = new byte[3 * DATA_SIZE];
            
            int bytesRead = groupedSource.get(0, output, 0, 3 * DATA_SIZE);
            
            assertEquals(3 * DATA_SIZE, bytesRead);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, 0, DATA_SIZE);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, DATA_SIZE, DATA_SIZE);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, 2 * DATA_SIZE, DATA_SIZE);
        }
    }

    @Test
    public void getArray_withLengthBeyondTotalSize_shouldReadUpToEnd() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            byte[] output = new byte[3 * DATA_SIZE + 10];
            
            int bytesRead = groupedSource.get(0, output, 0, 3 * DATA_SIZE + 10);
            
            assertEquals(3 * DATA_SIZE, bytesRead);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, 0, DATA_SIZE);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, DATA_SIZE, DATA_SIZE);
            assertArrayEquals(rangeArray(0, DATA_SIZE), 0, output, 2 * DATA_SIZE, DATA_SIZE);
        }
    }

    @Test
    public void getArray_spanningTwoSources_shouldReadCorrectly() throws Exception {
        try (GroupedRandomAccessSource groupedSource = createGroupedSource(3)) {
            byte[] output = new byte[DATA_SIZE];
            int startOffset = DATA_SIZE - 50; // Start in first source, end in second
            
            int bytesRead = groupedSource.get(startOffset, output, 0, DATA_SIZE);
            
            assertEquals(DATA_SIZE, bytesRead);
            // First 50 bytes from end of first source
            assertArrayEquals(rangeArray(50, 50), 0, output, 0, 50);
            // Next 50 bytes from beginning of second source
            assertArrayEquals(rangeArray(0, 50), 0, output, 50, 50);
        }
    }

    @Test
    public void sourceManagement_shouldOnlyKeepOneSourceOpen() throws Exception {
        // Create tracking variables
        final int[] openCount = {0};
        final RandomAccessSource[] currentSource = {null};
        
        // Create grouped source with tracking
        GroupedRandomAccessSource groupedSource = createTrackingGroupedSource(3, openCount, currentSource);
        
        try {
            // Access different sources sequentially
            groupedSource.get(250);  // Third source
            groupedSource.get(251);
            assertEquals(1, openCount[0]);
            
            groupedSource.get(150);  // Second source
            groupedSource.get(151);
            assertEquals(1, openCount[0]);
            
            groupedSource.get(50);   // First source
            groupedSource.get(51);
            assertEquals(1, openCount[0]);
            
            groupedSource.get(150);   // Back to second source
            groupedSource.get(151);
            assertEquals(1, openCount[0]);
            
            groupedSource.get(250);   // Back to third source
            groupedSource.get(251);
            assertEquals(1, openCount[0]);
        } finally {
            groupedSource.close();
        }
    }

    private GroupedRandomAccessSource createGroupedSource(int numberOfSources) throws IOException {
        RandomAccessSource[] sources = new RandomAccessSource[numberOfSources];
        for (int i = 0; i < numberOfSources; i++) {
            sources[i] = new ArrayRandomAccessSource(data);
        }
        return new GroupedRandomAccessSource(sources);
    }

    private GroupedRandomAccessSource createTrackingGroupedSource(int numberOfSources, 
            final int[] openCount, final RandomAccessSource[] currentSource) throws IOException {
        RandomAccessSource[] sources = new RandomAccessSource[numberOfSources];
        for (int i = 0; i < numberOfSources; i++) {
            sources[i] = new ArrayRandomAccessSource(data);
        }
        
        return new GroupedRandomAccessSource(sources) {
            @Override
            protected void sourceReleased(RandomAccessSource source) throws IOException {
                openCount[0]--;
                if (currentSource[0] != source) {
                    throw new AssertionError("Released source isn't the current source");
                }
                currentSource[0] = null;
            }
            
            @Override
            protected void sourceInUse(RandomAccessSource source) throws IOException {
                if (currentSource[0] != null) {
                    throw new AssertionError("Current source wasn't released properly");
                }
                openCount[0]++;
                currentSource[0] = source;
            }
        };
    }

    private byte[] rangeArray(int start, int count) {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = (byte) (i + start);
        }
        return result;
    }

    private void assertArrayEqual(byte[] expected, int expectedOffset, 
                                 byte[] actual, int actualOffset, int length) {
        byte[] expectedSegment = new byte[length];
        byte[] actualSegment = new byte[length];
        System.arraycopy(expected, expectedOffset, expectedSegment, 0, length);
        System.arraycopy(actual, actualOffset, actualSegment, 0, length);
        assertArrayEquals(expectedSegment, actualSegment);
    }
}
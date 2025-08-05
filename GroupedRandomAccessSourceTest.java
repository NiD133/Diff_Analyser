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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class GroupedRandomAccessSourceTest {

    // A 100-byte array with values from 0 to 99.
    private byte[] sourceData;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < 100; i++) {
            baos.write((byte) i);
        }
        sourceData = baos.toByteArray();
    }

    @Test
    public void get_shouldReturnCorrectByteValue_forVariousPositions() throws Exception {
        // Arrange
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(sourceData); // bytes 0-99
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(sourceData); // bytes 100-199
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(sourceData); // bytes 200-299
        RandomAccessSource[] sources = {source1, source2, source3};
        GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(sources);

        // Assert
        assertEquals(300, grouped.length());

        // Test within the first source
        assertEquals(99, grouped.get(99));

        // Test boundary between first and second source
        assertEquals(0, grouped.get(100));
        assertEquals(1, grouped.get(101));

        // Test last byte of the entire grouped source
        assertEquals(99, grouped.get(299));

        // Test out of bounds
        assertEquals(-1, grouped.get(300));
    }

    @Test
    public void getArray_shouldReadEntireContent_whenBufferIsLargeEnough() throws Exception {
        // Arrange
        GroupedRandomAccessSource grouped = createGroupedSourceFromThreeCopies();
        byte[] out = new byte[300];
        byte[] expected = new byte[300];
        System.arraycopy(sourceData, 0, expected, 0, 100);
        System.arraycopy(sourceData, 0, expected, 100, 100);
        System.arraycopy(sourceData, 0, expected, 200, 100);

        // Act
        int bytesRead = grouped.get(0, out, 0, 300);

        // Assert
        assertEquals(300, bytesRead);
        assertArrayEquals(expected, out);
    }

    @Test
    public void getArray_shouldOnlyReadAvailableBytes_whenRequestingPastTheEnd() throws Exception {
        // Arrange
        GroupedRandomAccessSource grouped = createGroupedSourceFromThreeCopies();
        byte[] out = new byte[301]; // Buffer is larger than source
        byte[] expected = new byte[300];
        System.arraycopy(sourceData, 0, expected, 0, 100);
        System.arraycopy(sourceData, 0, expected, 100, 100);
        System.arraycopy(sourceData, 0, expected, 200, 100);

        // Act
        int bytesRead = grouped.get(0, out, 0, 301);

        // Assert
        assertEquals(300, bytesRead);
        byte[] actual = Arrays.copyOf(out, 300);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getArray_shouldReadAcrossSourceBoundaries() throws Exception {
        // Arrange
        GroupedRandomAccessSource grouped = createGroupedSourceFromThreeCopies();
        // Read 100 bytes starting at offset 150. This will read:
        // - 50 bytes from the end of source 2 (values 50-99)
        // - 50 bytes from the start of source 3 (values 0-49)
        byte[] out = new byte[100];
        byte[] expected = new byte[100];
        System.arraycopy(rangeArray(50, 50), 0, expected, 0, 50);
        System.arraycopy(rangeArray(0, 50), 0, expected, 50, 50);

        // Act
        int bytesRead = grouped.get(150, out, 0, 100);

        // Assert
        assertEquals(100, bytesRead);
        assertArrayEquals(expected, out);
    }

    @Test
    public void get_shouldSwitchAndReleaseSources_whenAccessingDifferentRegions() throws Exception {
        // Arrange
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource[] sources = {source1, source2, source3};
        VerifyingGroupedRandomAccessSource grouped = new VerifyingGroupedRandomAccessSource(sources);

        // Act & Assert
        // Accessing a new source should trigger one 'inUse' and one 'release'
        grouped.get(250); // Access source 3
        assertEquals(1, grouped.getOpenCount());
        assertEquals(source3, grouped.getLastSourceInUse());

        grouped.get(150); // Access source 2
        assertEquals(1, grouped.getOpenCount());
        assertEquals(source2, grouped.getLastSourceInUse());
        assertEquals(source3, grouped.getLastSourceReleased());

        grouped.get(50); // Access source 1
        assertEquals(1, grouped.getOpenCount());
        assertEquals(source1, grouped.getLastSourceInUse());
        assertEquals(source2, grouped.getLastSourceReleased());

        // Accessing the same source again should not trigger release/inUse
        int inUseCountBefore = grouped.getSourcesInUse().size();
        int releasedCountBefore = grouped.getSourcesReleased().size();
        grouped.get(51);
        assertEquals(inUseCountBefore, grouped.getSourcesInUse().size());
        assertEquals(releasedCountBefore, grouped.getSourcesReleased().size());

        grouped.close();
        assertEquals(0, grouped.getOpenCount());
        assertEquals(source1, grouped.getLastSourceReleased());
    }

    // A helper to generate a byte array with a sequence of numbers.
    private byte[] rangeArray(int start, int count) {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = (byte) (i + start);
        }
        return result;
    }

    // A helper to create a standard test instance.
    private GroupedRandomAccessSource createGroupedSourceFromThreeCopies() throws IOException {
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource[] inputs = {source1, source2, source3};
        return new GroupedRandomAccessSource(inputs);
    }

    /**
     * A "Spy" version of GroupedRandomAccessSource that records calls to
     * sourceInUse() and sourceReleased() for verification purposes.
     */
    private static class VerifyingGroupedRandomAccessSource extends GroupedRandomAccessSource {
        private final List<RandomAccessSource> sourcesInUse = new ArrayList<>();
        private final List<RandomAccessSource> sourcesReleased = new ArrayList<>();
        private int openCount = 0;

        public VerifyingGroupedRandomAccessSource(RandomAccessSource[] sources) throws IOException {
            super(sources);
        }

        @Override
        protected void sourceInUse(RandomAccessSource source) throws IOException {
            super.sourceInUse(source);
            sourcesInUse.add(source);
            openCount++;
        }

        @Override
        protected void sourceReleased(RandomAccessSource source) throws IOException {
            super.sourceReleased(source);
            sourcesReleased.add(source);
            openCount--;
        }

        // --- Test verification methods ---
        public int getOpenCount() {
            return openCount;
        }

        public List<RandomAccessSource> getSourcesInUse() {
            return sourcesInUse;
        }

        public List<RandomAccessSource> getSourcesReleased() {
            return sourcesReleased;
        }

        public RandomAccessSource getLastSourceInUse() {
            return sourcesInUse.isEmpty() ? null : sourcesInUse.get(sourcesInUse.size() - 1);
        }

        public RandomAccessSource getLastSourceReleased() {
            return sourcesReleased.isEmpty() ? null : sourcesReleased.get(sourcesReleased.size() - 1);
        }
    }
}
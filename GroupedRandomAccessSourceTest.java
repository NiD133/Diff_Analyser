package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupedRandomAccessSourceTest {
    private static final int DATA_SIZE = 100;
    private byte[] data;

    @Before
    public void setUp() throws Exception {
        data = generateSequentialByteArray(DATA_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        // No resources to release after tests
    }

    @Test
    public void testGet() throws Exception {
        GroupedRandomAccessSource groupedSource = createGroupedRandomAccessSource(data);

        // Verify the total length of the grouped source
        int expectedLength = DATA_SIZE * 3;
        Assert.assertEquals(expectedLength, groupedSource.length());

        // Verify data retrieval from grouped source
        verifyDataRetrieval(groupedSource);
    }

    @Test
    public void testGetArray() throws Exception {
        GroupedRandomAccessSource groupedSource = createGroupedRandomAccessSource(data);
        byte[] outputBuffer = new byte[500];

        // Test reading the entire range
        Assert.assertEquals(300, groupedSource.get(0, outputBuffer, 0, 300));
        verifyArrayContent(outputBuffer, 0, 0, 100);
        verifyArrayContent(outputBuffer, 100, 0, 100);
        verifyArrayContent(outputBuffer, 200, 0, 100);

        // Test reading beyond the available range
        Assert.assertEquals(300, groupedSource.get(0, outputBuffer, 0, 301));
        verifyArrayContent(outputBuffer, 0, 0, 100);
        verifyArrayContent(outputBuffer, 100, 0, 100);
        verifyArrayContent(outputBuffer, 200, 0, 100);

        // Test partial reading
        Assert.assertEquals(100, groupedSource.get(150, outputBuffer, 0, 100));
        verifyArrayContent(outputBuffer, 0, 50, 50);
        verifyArrayContent(outputBuffer, 50, 0, 50);
    }

    @Test
    public void testRelease() throws Exception {
        GroupedRandomAccessSource groupedSource = createGroupedRandomAccessSource(data);

        // Track the current source and open count
        final RandomAccessSource[] currentSource = new RandomAccessSource[]{null};
        final int[] openCount = new int[]{0};

        // Override source management methods
        groupedSource = new GroupedRandomAccessSource(groupedSource.getSources()) {
            protected void sourceReleased(RandomAccessSource source) throws java.io.IOException {
                openCount[0]--;
                if (currentSource[0] != source) {
                    throw new AssertionFailedError("Released source isn't the current source");
                }
                currentSource[0] = null;
            }

            protected void sourceInUse(RandomAccessSource source) throws java.io.IOException {
                if (currentSource[0] != null) {
                    throw new AssertionFailedError("Current source wasn't released properly");
                }
                openCount[0]++;
                currentSource[0] = source;
            }
        };

        // Test source usage and release
        groupedSource.get(250);
        groupedSource.get(251);
        Assert.assertEquals(1, openCount[0]);
        groupedSource.get(150);
        groupedSource.get(151);
        Assert.assertEquals(1, openCount[0]);
        groupedSource.get(50);
        groupedSource.get(51);
        Assert.assertEquals(1, openCount[0]);
        groupedSource.get(150);
        groupedSource.get(151);
        Assert.assertEquals(1, openCount[0]);
        groupedSource.get(250);
        groupedSource.get(251);
        Assert.assertEquals(1, openCount[0]);

        groupedSource.close();
    }

    private byte[] generateSequentialByteArray(int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < size; i++) {
            baos.write((byte) i);
        }
        return baos.toByteArray();
    }

    private GroupedRandomAccessSource createGroupedRandomAccessSource(byte[] data) throws Exception {
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data);
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data);
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data);

        RandomAccessSource[] sources = new RandomAccessSource[]{source1, source2, source3};
        return new GroupedRandomAccessSource(sources);
    }

    private void verifyDataRetrieval(GroupedRandomAccessSource groupedSource) throws Exception {
        Assert.assertEquals(data[99], groupedSource.get(99));
        Assert.assertEquals(data[0], groupedSource.get(100));
        Assert.assertEquals(data[1], groupedSource.get(101));
        Assert.assertEquals(data[99], groupedSource.get(299));
        Assert.assertEquals(-1, groupedSource.get(300));
    }

    private void verifyArrayContent(byte[] array, int offset, int startValue, int length) {
        for (int i = 0; i < length; i++) {
            if (array[i + offset] != (byte) (i + startValue)) {
                throw new AssertionFailedError("Differ at index " + (i + offset) + " -> " + array[i + offset] + " != " + (byte) (i + startValue));
            }
        }
    }
}
package com.itextpdf.text.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for GroupedRandomAccessSource.
 * 
 * The tests operate on three concatenated in-memory sources. Each source
 * contains bytes 0..99, so the grouped source effectively exposes 300 bytes:
 * - positions 0..99   -> 0..99
 * - positions 100..199 -> 0..99
 * - positions 200..299 -> 0..99
 */
public class GroupedRandomAccessSourceTest {

    private static final int SEGMENT_SIZE = 100;
    private static final int SEGMENT_COUNT = 3;
    private static final int TOTAL_SIZE = SEGMENT_SIZE * SEGMENT_COUNT;

    private byte[] segmentData;

    @Before
    public void setUp() {
        // Build one segment of predictable data: [0, 1, 2, ... 99]
        segmentData = new byte[SEGMENT_SIZE];
        for (int i = 0; i < SEGMENT_SIZE; i++) {
            segmentData[i] = (byte) i;
        }
    }

    @After
    public void tearDown() {
        // no-op
    }

    @Test
    public void testLengthAndSingleByteReads() throws Exception {
        GroupedRandomAccessSource grouped = newGrouped(SEGMENT_COUNT);

        // Length should be the sum of all segment lengths
        assertEquals(TOTAL_SIZE, grouped.length());

        // Read the last byte of the first segment
        assertEquals(expectedValueAt(SEGMENT_SIZE - 1), grouped.get(SEGMENT_SIZE - 1));

        // Read the first two bytes of the second segment
        assertEquals(expectedValueAt(SEGMENT_SIZE), grouped.get(SEGMENT_SIZE));
        assertEquals(expectedValueAt(SEGMENT_SIZE + 1), grouped.get(SEGMENT_SIZE + 1));

        // Re-read to ensure no internal state issue
        assertEquals(expectedValueAt(SEGMENT_SIZE - 1), grouped.get(SEGMENT_SIZE - 1));

        // Read the last byte of the third segment
        assertEquals(expectedValueAt(TOTAL_SIZE - 1), grouped.get(TOTAL_SIZE - 1));

        // Out-of-bounds should return -1
        assertEquals(-1, grouped.get(TOTAL_SIZE));
    }

    @Test
    public void testBulkReadSpanningSegments() throws Exception {
        GroupedRandomAccessSource grouped = newGrouped(SEGMENT_COUNT);
        byte[] out = new byte[TOTAL_SIZE + 200]; // a bit larger than necessary

        // Read exactly TOTAL_SIZE bytes starting from 0
        assertEquals(TOTAL_SIZE, grouped.get(0, out, 0, TOTAL_SIZE));
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, 0);
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, SEGMENT_SIZE);
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, 2 * SEGMENT_SIZE);

        // Request more than available; should still read only TOTAL_SIZE
        Arrays.fill(out, (byte) 0);
        assertEquals(TOTAL_SIZE, grouped.get(0, out, 0, TOTAL_SIZE + 1));
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, 0);
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, SEGMENT_SIZE);
        assertRegionEquals(sequentialBytes(0, SEGMENT_SIZE), out, 2 * SEGMENT_SIZE);

        // Read 100 bytes starting from position 150 (crosses segment boundary)
        Arrays.fill(out, (byte) 0);
        assertEquals(100, grouped.get(150, out, 0, 100));
        assertRegionEquals(sequentialBytes(50, 50), out, 0);   // bytes 50..99 from segment 2
        assertRegionEquals(sequentialBytes(0, 50), out, 50);   // bytes 0..49 from segment 3
    }

    @Test
    public void testSourceSwitchingNotifiesHooks() throws Exception {
        // Build three segments
        RandomAccessSource[] sources = new RandomAccessSource[]{
                new ArrayRandomAccessSource(segmentData), // 0..99
                new ArrayRandomAccessSource(segmentData), // 100..199
                new ArrayRandomAccessSource(segmentData)  // 200..299
        };

        // Track source switching via overridden hooks
        TrackingGroupedRandomAccessSource grouped = new TrackingGroupedRandomAccessSource(sources);

        // All reads below should keep exactly one active source at any moment.
        readTwoBytes(grouped, 250); // segment 3
        readTwoBytes(grouped, 150); // switch to segment 2
        assertEquals(1, grouped.getActiveCount());

        readTwoBytes(grouped, 50);  // switch to segment 1
        assertEquals(1, grouped.getActiveCount());

        readTwoBytes(grouped, 150); // switch to segment 2
        assertEquals(1, grouped.getActiveCount());

        readTwoBytes(grouped, 250); // switch to segment 3
        assertEquals(1, grouped.getActiveCount());

        grouped.close();
    }

    // Helpers

    private GroupedRandomAccessSource newGrouped(int segmentCount) throws IOException {
        RandomAccessSource[] segments = new RandomAccessSource[segmentCount];
        for (int i = 0; i < segmentCount; i++) {
            segments[i] = new ArrayRandomAccessSource(segmentData);
        }
        return new GroupedRandomAccessSource(segments);
    }

    private static void readTwoBytes(RandomAccessSource source, long position) throws IOException {
        source.get(position);
        source.get(position + 1);
    }

    // Expected value for a global position in the grouped source: cycles 0..99 per segment
    private static int expectedValueAt(long position) {
        if (position < 0 || position >= TOTAL_SIZE) {
            return -1;
        }
        return (int) (position % SEGMENT_SIZE);
    }

    // Build a byte array of count bytes, starting at 'start' (values wrap naturally via byte cast)
    private static byte[] sequentialBytes(int start, int count) {
        byte[] rslt = new byte[count];
        for (int i = 0; i < count; i++) {
            rslt[i] = (byte) (start + i);
        }
        return rslt;
    }

    // Assert that a region inside 'actual' equals 'expected'
    private static void assertRegionEquals(byte[] expected, byte[] actual, int actualOffset) {
        assertArrayEquals(
                expected,
                Arrays.copyOfRange(actual, actualOffset, actualOffset + expected.length)
        );
    }

    /**
     * Test-only subclass that tracks source switch notifications.
     */
    private static class TrackingGroupedRandomAccessSource extends GroupedRandomAccessSource {
        private RandomAccessSource active;
        private int activeCount = 0;

        TrackingGroupedRandomAccessSource(RandomAccessSource[] sources) throws IOException {
            super(sources);
        }

        int getActiveCount() {
            return activeCount;
        }

        @Override
        protected void sourceReleased(RandomAccessSource source) throws IOException {
            activeCount--;
            if (active != source) {
                throw new AssertionError("Released source isn't the current source");
            }
            active = null;
        }

        @Override
        protected void sourceInUse(RandomAccessSource source) throws IOException {
            if (active != null) {
                throw new AssertionError("Current source wasn't released properly");
            }
            activeCount++;
            active = source;
        }
    }
}
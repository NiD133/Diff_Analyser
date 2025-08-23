package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupedRandomAccessSourceTestTest3 {

    byte[] data;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < 100; i++) {
            baos.write((byte) i);
        }
        data = baos.toByteArray();
    }

    @After
    public void tearDown() throws Exception {
    }

    private byte[] rangeArray(int start, int count) {
        byte[] rslt = new byte[count];
        for (int i = 0; i < count; i++) {
            rslt[i] = (byte) (i + start);
        }
        return rslt;
    }

    private void assertArrayEqual(byte[] a, int offa, byte[] b, int offb, int len) {
        for (int i = 0; i < len; i++) {
            if (a[i + offa] != b[i + offb]) {
                throw new AssertionFailedError("Differ at index " + (i + offa) + " and " + (i + offb) + " -> " + a[i + offa] + " != " + b[i + offb]);
            }
        }
    }

    @Test
    public void testRelease() throws Exception {
        // 0 - 99
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data);
        // 100 - 199
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data);
        // 200 - 299
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data);
        RandomAccessSource[] sources = new RandomAccessSource[] { source1, source2, source3 };
        final RandomAccessSource[] current = new RandomAccessSource[] { null };
        final int[] openCount = new int[] { 0 };
        GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(sources) {

            protected void sourceReleased(RandomAccessSource source) throws java.io.IOException {
                openCount[0]--;
                if (current[0] != source)
                    throw new AssertionFailedError("Released source isn't the current source");
                current[0] = null;
            }

            protected void sourceInUse(RandomAccessSource source) throws java.io.IOException {
                if (current[0] != null)
                    throw new AssertionFailedError("Current source wasn't released properly");
                openCount[0]++;
                current[0] = source;
            }
        };
        grouped.get(250);
        grouped.get(251);
        Assert.assertEquals(1, openCount[0]);
        grouped.get(150);
        grouped.get(151);
        Assert.assertEquals(1, openCount[0]);
        grouped.get(50);
        grouped.get(51);
        Assert.assertEquals(1, openCount[0]);
        grouped.get(150);
        grouped.get(151);
        Assert.assertEquals(1, openCount[0]);
        grouped.get(250);
        grouped.get(251);
        Assert.assertEquals(1, openCount[0]);
        grouped.close();
    }
}
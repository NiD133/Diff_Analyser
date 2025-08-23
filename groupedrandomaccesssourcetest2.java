package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupedRandomAccessSourceTestTest2 {

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
    public void testGetArray() throws Exception {
        // 0 - 99
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data);
        // 100 - 199
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data);
        // 200 - 299
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data);
        RandomAccessSource[] inputs = new RandomAccessSource[] { source1, source2, source3 };
        GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(inputs);
        byte[] out = new byte[500];
        Assert.assertEquals(300, grouped.get(0, out, 0, 300));
        assertArrayEqual(rangeArray(0, 100), 0, out, 0, 100);
        assertArrayEqual(rangeArray(0, 100), 0, out, 100, 100);
        assertArrayEqual(rangeArray(0, 100), 0, out, 200, 100);
        Assert.assertEquals(300, grouped.get(0, out, 0, 301));
        assertArrayEqual(rangeArray(0, 100), 0, out, 0, 100);
        assertArrayEqual(rangeArray(0, 100), 0, out, 100, 100);
        assertArrayEqual(rangeArray(0, 100), 0, out, 200, 100);
        Assert.assertEquals(100, grouped.get(150, out, 0, 100));
        assertArrayEqual(rangeArray(50, 50), 0, out, 0, 50);
        assertArrayEqual(rangeArray(0, 50), 0, out, 50, 50);
    }
}

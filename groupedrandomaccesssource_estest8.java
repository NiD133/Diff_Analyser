package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GroupedRandomAccessSource_ESTestTest8 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[4];
        byteArray0[2] = (byte) 3;
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[5];
        randomAccessSourceArray0[0] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[2] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[3] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[4] = (RandomAccessSource) getBufferedRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        int int0 = groupedRandomAccessSource0.get(2L);
        assertEquals(3, int0);
        assertEquals(20L, groupedRandomAccessSource0.length());
    }
}

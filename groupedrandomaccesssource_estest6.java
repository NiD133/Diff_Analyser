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

public class GroupedRandomAccessSource_ESTestTest6 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        byte[] byteArray0 = new byte[11];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[9];
        randomAccessSourceArray0[0] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) getBufferedRandomAccessSource0;
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource(getBufferedRandomAccessSource0, (-2024L), (-2024L));
        randomAccessSourceArray0[2] = (RandomAccessSource) windowRandomAccessSource0;
        randomAccessSourceArray0[3] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[4] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[5] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[6] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[7] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[8] = (RandomAccessSource) getBufferedRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        long long0 = groupedRandomAccessSource0.length();
        assertEquals((-1936L), long0);
    }
}

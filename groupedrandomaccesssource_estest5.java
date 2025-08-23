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

public class GroupedRandomAccessSource_ESTestTest5 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        byte[] byteArray0 = new byte[5];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[3];
        randomAccessSourceArray0[0] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) getBufferedRandomAccessSource0;
        randomAccessSourceArray0[2] = (RandomAccessSource) getBufferedRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        long long0 = groupedRandomAccessSource0.length();
        assertEquals(15L, long0);
    }
}

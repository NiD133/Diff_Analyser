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

public class GroupedRandomAccessSource_ESTestTest4 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[2];
        byte[] byteArray0 = new byte[0];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        randomAccessSourceArray0[0] = (RandomAccessSource) arrayRandomAccessSource0;
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        randomAccessSourceArray0[1] = (RandomAccessSource) getBufferedRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        long long0 = groupedRandomAccessSource0.length();
        assertEquals(0L, long0);
    }
}

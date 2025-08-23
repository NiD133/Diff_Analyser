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

public class GroupedRandomAccessSource_ESTestTest13 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[4];
        MappedChannelRandomAccessSource mappedChannelRandomAccessSource0 = new MappedChannelRandomAccessSource((FileChannel) null, 2L, 2L);
        randomAccessSourceArray0[0] = (RandomAccessSource) mappedChannelRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) mappedChannelRandomAccessSource0;
        byte[] byteArray0 = new byte[7];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        randomAccessSourceArray0[2] = (RandomAccessSource) arrayRandomAccessSource0;
        IndependentRandomAccessSource independentRandomAccessSource0 = new IndependentRandomAccessSource(randomAccessSourceArray0[1]);
        randomAccessSourceArray0[3] = (RandomAccessSource) independentRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        try {
            groupedRandomAccessSource0.get(2L, byteArray0, (int) (byte) 109, (int) (byte) 43);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // RandomAccessSource not opened
            //
            verifyException("com.itextpdf.text.io.MappedChannelRandomAccessSource", e);
        }
    }
}

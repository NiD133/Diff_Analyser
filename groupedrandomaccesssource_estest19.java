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

public class GroupedRandomAccessSource_ESTestTest19 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[2];
        byte[] byteArray0 = new byte[1];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource(arrayRandomAccessSource0, 7L, 7L);
        IndependentRandomAccessSource independentRandomAccessSource0 = new IndependentRandomAccessSource(windowRandomAccessSource0);
        randomAccessSourceArray0[0] = (RandomAccessSource) independentRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) windowRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        int int0 = groupedRandomAccessSource0.get((long) 1, byteArray0, 1, 1);
        assertEquals(14L, groupedRandomAccessSource0.length());
        assertEquals((-1), int0);
    }
}

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

public class GroupedRandomAccessSource_ESTestTest10 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[2];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[7];
        randomAccessSourceArray0[0] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[1] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[2] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[3] = (RandomAccessSource) arrayRandomAccessSource0;
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(1297);
        ByteBufferRandomAccessSource byteBufferRandomAccessSource0 = new ByteBufferRandomAccessSource(byteBuffer0);
        randomAccessSourceArray0[4] = (RandomAccessSource) byteBufferRandomAccessSource0;
        randomAccessSourceArray0[5] = (RandomAccessSource) arrayRandomAccessSource0;
        randomAccessSourceArray0[6] = (RandomAccessSource) arrayRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        // Undeclared exception!
        try {
            groupedRandomAccessSource0.get((long) 1297, byteArray0, (-1228), 1297);
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            //
            // java.nio.ByteBuffer.position(I)Ljava/nio/ByteBuffer;
            //
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }
}

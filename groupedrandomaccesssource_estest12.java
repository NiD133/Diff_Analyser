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

public class GroupedRandomAccessSource_ESTestTest12 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[1];
        byte[] byteArray0 = new byte[7];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        randomAccessSourceArray0[0] = (RandomAccessSource) arrayRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        // Undeclared exception!
        try {
            groupedRandomAccessSource0.get((long) 1, byteArray0, 5239, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
        }
    }
}

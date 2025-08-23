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

public class GroupedRandomAccessSource_ESTestTest16 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[1];
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource((RandomAccessSource) null, 328L, 328L);
        randomAccessSourceArray0[0] = (RandomAccessSource) windowRandomAccessSource0;
        GroupedRandomAccessSource groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
        // Undeclared exception!
        try {
            groupedRandomAccessSource0.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.itextpdf.text.io.WindowRandomAccessSource", e);
        }
    }
}

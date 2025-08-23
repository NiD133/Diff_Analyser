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

public class GroupedRandomAccessSource_ESTestTest18 extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        RandomAccessSource[] randomAccessSourceArray0 = new RandomAccessSource[0];
        GroupedRandomAccessSource groupedRandomAccessSource0 = null;
        try {
            groupedRandomAccessSource0 = new GroupedRandomAccessSource(randomAccessSourceArray0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -1
            //
            verifyException("com.itextpdf.text.io.GroupedRandomAccessSource", e);
        }
    }
}

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

public class GetBufferedRandomAccessSource_ESTestTest2 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource((RandomAccessSource) null, 0L, 0L);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(windowRandomAccessSource0);
        long long0 = getBufferedRandomAccessSource0.length();
        assertEquals(0L, long0);
    }
}

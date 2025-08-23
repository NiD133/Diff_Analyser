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

public class GetBufferedRandomAccessSource_ESTestTest23 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        byte[] byteArray0 = new byte[8];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        getBufferedRandomAccessSource0.get(0L);
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get((-1L));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}

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

public class GetBufferedRandomAccessSource_ESTestTest8 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[4];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(arrayRandomAccessSource0);
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get((-1780L), (byte[]) null, (-213), (-213));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}

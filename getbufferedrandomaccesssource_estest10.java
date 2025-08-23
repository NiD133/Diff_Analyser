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

public class GetBufferedRandomAccessSource_ESTestTest10 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[0];
        ArrayRandomAccessSource arrayRandomAccessSource0 = new ArrayRandomAccessSource(byteArray0);
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource(arrayRandomAccessSource0, (-782L), (-782L));
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(windowRandomAccessSource0);
        windowRandomAccessSource0.close();
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get((-2836L), byteArray0, (-1), (-1));
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Already closed
            //
            verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
        }
    }
}

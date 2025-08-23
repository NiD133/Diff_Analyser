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

public class GetBufferedRandomAccessSource_ESTestTest11 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        byte[] byteArray0 = new byte[2];
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(byteArray0);
        ByteBufferRandomAccessSource byteBufferRandomAccessSource0 = new ByteBufferRandomAccessSource(byteBuffer0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(byteBufferRandomAccessSource0);
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get(2147483651L, byteArray0, 1617, 1617);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Position must be less than Integer.MAX_VALUE
            //
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }
}
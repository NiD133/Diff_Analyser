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

public class GetBufferedRandomAccessSource_ESTestTest9 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        byte[] byteArray0 = new byte[1];
        ByteBuffer byteBuffer0 = ByteBuffer.wrap(byteArray0);
        ByteBufferRandomAccessSource byteBufferRandomAccessSource0 = new ByteBufferRandomAccessSource(byteBuffer0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(byteBufferRandomAccessSource0);
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get((long) (byte) 0, byteArray0, (int) (byte) (-89), (int) (byte) (-89));
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            //
            // java.nio.ByteBuffer.position(I)Ljava/nio/ByteBuffer;
            //
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }
}

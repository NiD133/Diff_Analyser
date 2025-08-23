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

public class GetBufferedRandomAccessSource_ESTestTest15 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(1);
        ByteBufferRandomAccessSource byteBufferRandomAccessSource0 = new ByteBufferRandomAccessSource(byteBuffer0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(byteBufferRandomAccessSource0);
        // Undeclared exception!
        try {
            getBufferedRandomAccessSource0.get((-1269L));
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            //
            // java.nio.ByteBuffer.position(I)Ljava/nio/ByteBuffer;
            //
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }
}

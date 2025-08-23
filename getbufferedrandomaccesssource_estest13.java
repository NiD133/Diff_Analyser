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

public class GetBufferedRandomAccessSource_ESTestTest13 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        MappedChannelRandomAccessSource mappedChannelRandomAccessSource0 = new MappedChannelRandomAccessSource((FileChannel) null, 2782L, 2782L);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(mappedChannelRandomAccessSource0);
        byte[] byteArray0 = new byte[1];
        try {
            getBufferedRandomAccessSource0.get(2782L, byteArray0, (int) (byte) (-27), (int) (byte) 0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // RandomAccessSource not opened
            //
            verifyException("com.itextpdf.text.io.MappedChannelRandomAccessSource", e);
        }
    }
}

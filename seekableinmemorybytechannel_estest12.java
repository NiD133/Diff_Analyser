package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SeekableInMemoryByteChannel_ESTestTest12 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        byte[] byteArray0 = new byte[8];
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(byteArray0);
        byte[] byteArray1 = seekableInMemoryByteChannel0.array();
        assertArrayEquals(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }, byteArray1);
    }
}

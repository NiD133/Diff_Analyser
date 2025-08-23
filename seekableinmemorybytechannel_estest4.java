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

public class SeekableInMemoryByteChannel_ESTestTest4 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        byte[] byteArray0 = new byte[1];
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(byteArray0);
        ByteBuffer byteBuffer0 = ByteBuffer.allocate((byte) 86);
        int int0 = seekableInMemoryByteChannel0.write(byteBuffer0);
        assertEquals("java.nio.HeapByteBuffer[pos=86 lim=86 cap=86]", byteBuffer0.toString());
        assertEquals(86, int0);
    }
}

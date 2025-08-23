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

public class SeekableInMemoryByteChannel_ESTestTest6 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        byte[] byteArray0 = new byte[5];
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(byteArray0);
        seekableInMemoryByteChannel0.position(1L);
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(0);
        int int0 = seekableInMemoryByteChannel0.read(byteBuffer0);
        assertEquals(1L, seekableInMemoryByteChannel0.position());
        assertEquals(0, int0);
    }
}

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

public class SeekableInMemoryByteChannel_ESTestTest1 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte[] byteArray0 = new byte[5];
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(byteArray0);
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(0);
        int int0 = seekableInMemoryByteChannel0.write(byteBuffer0);
        assertEquals(0, int0);
        assertEquals(0L, seekableInMemoryByteChannel0.position());
        assertEquals(5L, seekableInMemoryByteChannel0.size());
    }
}

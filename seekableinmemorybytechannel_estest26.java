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

public class SeekableInMemoryByteChannel_ESTestTest26 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(1);
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(1);
        seekableInMemoryByteChannel0.read(byteBuffer0);
        byteBuffer0.clear();
        int int0 = seekableInMemoryByteChannel0.write(byteBuffer0);
        assertEquals(0, byteBuffer0.remaining());
        assertEquals(1, int0);
    }
}

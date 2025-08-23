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

public class SeekableInMemoryByteChannel_ESTestTest5 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel();
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(1098);
        SeekableByteChannel seekableByteChannel0 = seekableInMemoryByteChannel0.position(2147483647L);
        int int0 = seekableByteChannel0.read(byteBuffer0);
        assertEquals((-1), int0);
    }
}

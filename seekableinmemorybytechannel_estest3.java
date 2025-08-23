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

public class SeekableInMemoryByteChannel_ESTestTest3 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[3];
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(byteArray0);
        SeekableByteChannel seekableByteChannel0 = seekableInMemoryByteChannel0.truncate(2147483647L);
        assertEquals(3L, seekableByteChannel0.size());
        assertEquals(0L, seekableByteChannel0.position());
    }
}

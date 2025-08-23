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

public class SeekableInMemoryByteChannel_ESTestTest10 extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        SeekableInMemoryByteChannel seekableInMemoryByteChannel0 = new SeekableInMemoryByteChannel(1);
        long long0 = seekableInMemoryByteChannel0.size();
        assertEquals(1L, long0);
    }
}

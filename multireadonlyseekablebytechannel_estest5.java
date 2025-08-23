package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

public class MultiReadOnlySeekableByteChannel_ESTestTest5 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        MockFile mockFile0 = new MockFile("");
        SeekableByteChannel[] seekableByteChannelArray0 = new SeekableByteChannel[1];
        Path path0 = mockFile0.toPath();
        OpenOption[] openOptionArray0 = new OpenOption[0];
        FileChannel fileChannel0 = FileChannel.open(path0, openOptionArray0);
        seekableByteChannelArray0[0] = (SeekableByteChannel) fileChannel0;
        SeekableByteChannel seekableByteChannel0 = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(seekableByteChannelArray0);
        assertTrue(seekableByteChannel0.isOpen());
    }
}

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

public class MultiReadOnlySeekableByteChannel_ESTestTest28 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(359);
        LinkedList<SeekableByteChannel> linkedList0 = new LinkedList<SeekableByteChannel>();
        MultiReadOnlySeekableByteChannel multiReadOnlySeekableByteChannel0 = new MultiReadOnlySeekableByteChannel(linkedList0);
        linkedList0.add((SeekableByteChannel) multiReadOnlySeekableByteChannel0);
        MultiReadOnlySeekableByteChannel multiReadOnlySeekableByteChannel1 = new MultiReadOnlySeekableByteChannel(linkedList0);
        int int0 = multiReadOnlySeekableByteChannel1.read(byteBuffer0);
        assertEquals((-1), int0);
    }
}

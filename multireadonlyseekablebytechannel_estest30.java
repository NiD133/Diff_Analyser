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

public class MultiReadOnlySeekableByteChannel_ESTestTest30 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        File[] fileArray0 = new File[2];
        MockFile mockFile0 = new MockFile("");
        fileArray0[0] = (File) mockFile0;
        fileArray0[1] = (File) mockFile0;
        SeekableByteChannel seekableByteChannel0 = MultiReadOnlySeekableByteChannel.forFiles(fileArray0);
        ((MultiReadOnlySeekableByteChannel) seekableByteChannel0).close();
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(5);
        try {
            seekableByteChannel0.read(byteBuffer0);
            fail("Expecting exception: ClosedChannelException");
        } catch (ClosedChannelException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }
}

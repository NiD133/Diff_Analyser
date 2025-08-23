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

public class MultiReadOnlySeekableByteChannel_ESTestTest1 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        File[] fileArray0 = new File[2];
        MockFile mockFile0 = new MockFile("");
        fileArray0[0] = (File) mockFile0;
        fileArray0[1] = (File) mockFile0;
        MultiReadOnlySeekableByteChannel multiReadOnlySeekableByteChannel0 = (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(fileArray0);
        // Undeclared exception!
        try {
            multiReadOnlySeekableByteChannel0.position(6L, (-1L));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Index: 2, Size: 2
            //
            verifyException("java.util.ArrayList", e);
        }
    }
}
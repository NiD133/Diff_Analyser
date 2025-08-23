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

public class MultiReadOnlySeekableByteChannel_ESTestTest36 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        LinkedList<SeekableByteChannel> linkedList0 = new LinkedList<SeekableByteChannel>();
        MultiReadOnlySeekableByteChannel multiReadOnlySeekableByteChannel0 = new MultiReadOnlySeekableByteChannel(linkedList0);
        // Undeclared exception!
        try {
            multiReadOnlySeekableByteChannel0.write((ByteBuffer) null);
            fail("Expecting exception: NonWritableChannelException");
        } catch (NonWritableChannelException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }
}

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

public class MultiReadOnlySeekableByteChannel_ESTestTest23 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        MockFile mockFile0 = new MockFile("");
        Path[] pathArray0 = new Path[8];
        Path path0 = mockFile0.toPath();
        pathArray0[0] = path0;
        pathArray0[1] = path0;
        pathArray0[2] = path0;
        pathArray0[3] = path0;
        pathArray0[4] = pathArray0[0];
        pathArray0[5] = path0;
        pathArray0[6] = pathArray0[0];
        pathArray0[7] = pathArray0[4];
        SeekableByteChannel seekableByteChannel0 = MultiReadOnlySeekableByteChannel.forPaths(pathArray0);
        assertEquals(0L, seekableByteChannel0.position());
    }
}

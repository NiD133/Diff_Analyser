package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class MultiReadOnlySeekableByteChannelTest extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testPositionOutOfBounds() throws Throwable {
        File[] files = createMockFiles(2);
        MultiReadOnlySeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files);

        try {
            channel.position(6L, -1L);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testChannelSize() throws Throwable {
        File[] files = createMockFiles(2);
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files);
        long size = channel.size();
        assertEquals(1408L, size);
    }

    @Test(timeout = 4000)
    public void testSetPosition() throws Throwable {
        File[] files = createMockFiles(2);
        MultiReadOnlySeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files);
        channel.position(1L, 1L);
        long position = channel.position();
        assertEquals(705L, position);
    }

    @Test(timeout = 4000)
    public void testCloseChannel() throws Throwable {
        File[] files = createMockFiles(2);
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files);
        channel.close();
        assertFalse(channel.isOpen());
    }

    @Test(timeout = 4000)
    public void testOpenChannelWithFileChannel() throws Throwable {
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forPaths(path);
        assertTrue(channel.isOpen());
    }

    @Test(timeout = 4000)
    public void testSizeWithNullChannel() throws Throwable {
        List<SeekableByteChannel> channels = new LinkedList<>();
        channels.add(null);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channels);

        try {
            channel.size();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.stream.MatchOps$1MatchSink", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNullBuffer() throws Throwable {
        List<SeekableByteChannel> channels = new LinkedList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channels);

        try {
            channel.read(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadFromClosedChannel() throws Throwable {
        File[] files = createMockFiles(2);
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files);
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(5);

        try {
            channel.read(buffer);
            fail("Expected ClosedChannelException");
        } catch (ClosedChannelException e) {
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testPositionNegative() throws Throwable {
        List<SeekableByteChannel> channels = new LinkedList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channels);

        try {
            channel.position(-678L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteToReadOnlyChannel() throws Throwable {
        List<SeekableByteChannel> channels = new LinkedList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channels);

        try {
            channel.write(null);
            fail("Expected NonWritableChannelException");
        } catch (NonWritableChannelException e) {
            verifyException("org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel", e);
        }
    }

    private File[] createMockFiles(int count) {
        File[] files = new File[count];
        MockFile mockFile = new MockFile("");
        for (int i = 0; i < count; i++) {
            files[i] = mockFile;
        }
        return files;
    }
}
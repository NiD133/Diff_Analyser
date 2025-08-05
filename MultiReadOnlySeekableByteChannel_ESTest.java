package org.apache.commons.compress.utils;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
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
import java.util.ArrayList;
import java.util.List;
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Test suite for MultiReadOnlySeekableByteChannel functionality.
 * Tests cover channel creation, positioning, reading, and error handling.
 */
public class MultiReadOnlySeekableByteChannelTest {

    private static final int MOCK_FILE_SIZE = 704; // Size of empty MockFile
    private static final int BUFFER_SIZE = 5;
    
    private File[] twoMockFiles;
    private MockFile mockFile;

    @Before
    public void setUp() {
        mockFile = new MockFile("");
        twoMockFiles = new File[]{mockFile, mockFile};
    }

    // === Factory Method Tests ===

    @Test
    public void testForFiles_WithValidFiles_CreatesChannel() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        assertTrue("Channel should be open", channel.isOpen());
        assertEquals("Total size should be sum of both files", 
                     MOCK_FILE_SIZE * 2, channel.size());
    }

    @Test(expected = NoSuchFileException.class)
    public void testForFiles_WithNonExistentFile_ThrowsException() throws IOException {
        File nonExistentFile = new MockFile("nonexistent");
        MultiReadOnlySeekableByteChannel.forFiles(nonExistentFile);
    }

    @Test(expected = NullPointerException.class)
    public void testForFiles_WithNullFileInArray_ThrowsException() throws IOException {
        File[] filesWithNull = new File[2];
        MultiReadOnlySeekableByteChannel.forFiles(filesWithNull);
    }

    @Test
    public void testForPaths_WithValidPaths_CreatesChannel() throws IOException {
        Path[] paths = {mockFile.toPath()};
        
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forPaths(paths);
        
        assertTrue("Channel should be open", channel.isOpen());
        assertEquals("Position should start at 0", 0L, channel.position());
    }

    @Test
    public void testForPaths_WithMultiplePaths_CreatesChannel() throws IOException {
        Path mockPath = mockFile.toPath();
        Path[] multiplePaths = {mockPath, mockPath, mockPath, mockPath, 
                               mockPath, mockPath, mockPath, mockPath};
        
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forPaths(multiplePaths);
        
        assertEquals("Position should start at 0", 0L, channel.position());
    }

    @Test(expected = NoSuchFileException.class)
    public void testForPaths_WithNonExistentPath_ThrowsException() throws IOException {
        Path nonExistentPath = new MockFile("nonexistent").toPath();
        MultiReadOnlySeekableByteChannel.forPaths(nonExistentPath);
    }

    @Test(expected = NullPointerException.class)
    public void testForPaths_WithNullPathInArray_ThrowsException() throws IOException {
        Path[] pathsWithNull = new Path[1];
        MultiReadOnlySeekableByteChannel.forPaths(pathsWithNull);
    }

    @Test
    public void testForSeekableByteChannels_WithValidChannel_CreatesChannel() throws IOException {
        FileChannel fileChannel = createFileChannel();
        SeekableByteChannel[] channels = {fileChannel};
        
        SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
        
        assertTrue("Multi-channel should be open", multiChannel.isOpen());
    }

    @Test
    public void testForSeekableByteChannels_WithEmptyArray_CreatesEmptyChannel() {
        SeekableByteChannel[] emptyChannels = {};
        
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(emptyChannels);
        
        assertEquals("Empty channel position should be 0", 0L, channel.position());
    }

    @Test
    public void testForSeekableByteChannels_WithNullChannel_ReturnsNull() {
        SeekableByteChannel[] channelsWithNull = new SeekableByteChannel[1];
        
        SeekableByteChannel result = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channelsWithNull);
        
        assertNull("Should return null for array with null channel", result);
    }

    @Test(expected = NullPointerException.class)
    public void testForSeekableByteChannels_WithNullArray_ThrowsException() {
        MultiReadOnlySeekableByteChannel.forSeekableByteChannels(null);
    }

    // === Constructor Tests ===

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullChannelList_ThrowsException() {
        new MultiReadOnlySeekableByteChannel(null);
    }

    @Test
    public void testConstructor_WithEmptyChannelList_CreatesChannel() {
        List<SeekableByteChannel> emptyList = new ArrayList<>();
        
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyList);
        
        assertTrue("Empty channel should be open", channel.isOpen());
    }

    // === Position Tests ===

    @Test
    public void testPosition_InitialPosition_IsZero() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        assertEquals("Initial position should be 0", 0L, channel.position());
    }

    @Test
    public void testPosition_SetValidPosition_UpdatesPosition() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        long targetPosition = 704L; // End of first file
        
        channel.position(targetPosition);
        
        assertEquals("Position should be updated", targetPosition, channel.position());
    }

    @Test
    public void testPosition_SetLargePosition_UpdatesPosition() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        long largePosition = 2631L;
        
        channel.position(largePosition);
        
        assertEquals("Large position should be set", largePosition, channel.position());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPosition_WithNegativePosition_ThrowsException() throws IOException {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        
        channel.position(-1L);
    }

    @Test(expected = ClosedChannelException.class)
    public void testPosition_OnClosedChannel_ThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        channel.close();
        
        channel.position(0L);
    }

    @Test
    public void testPositionWithChannelAndOffset_ValidParameters_SetsPosition() throws IOException {
        MultiReadOnlySeekableByteChannel channel = 
            (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        channel.position(1L, 1L); // Channel 1, offset 1
        
        assertEquals("Position should be calculated correctly", 705L, channel.position());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPositionWithChannelAndOffset_InvalidChannelIndex_ThrowsException() throws IOException {
        MultiReadOnlySeekableByteChannel channel = 
            (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        channel.position(6L, -1L); // Invalid channel index
    }

    @Test(expected = ClosedChannelException.class)
    public void testPositionWithChannelAndOffset_OnClosedChannel_ThrowsException() throws IOException {
        MultiReadOnlySeekableByteChannel channel = 
            (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        channel.close();
        
        channel.position(-1456L, -1456L);
    }

    // === Size Tests ===

    @Test
    public void testSize_WithMultipleFiles_ReturnsTotalSize() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        long totalSize = channel.size();
        
        assertEquals("Size should be sum of all files", MOCK_FILE_SIZE * 2, totalSize);
    }

    @Test
    public void testSize_WithEmptyChannelList_ReturnsZero() throws IOException {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel emptyChannel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        List<SeekableByteChannel> channelsWithEmpty = new ArrayList<>();
        channelsWithEmpty.add(emptyChannel);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithEmpty);
        
        long size = channel.size();
        
        assertEquals("Empty channel should have size 0", 0L, size);
    }

    @Test(expected = ClosedChannelException.class)
    public void testSize_OnClosedChannel_ThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        channel.close();
        
        channel.size();
    }

    // === Read Tests ===

    @Test(expected = NullPointerException.class)
    public void testRead_WithNullBuffer_ThrowsException() throws IOException {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        
        channel.read(null);
    }

    @Test
    public void testRead_WithZeroCapacityBuffer_ReturnsZero() throws IOException {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        ByteBuffer zeroBuffer = ByteBuffer.allocate(0);
        
        int bytesRead = channel.read(zeroBuffer);
        
        assertEquals("Reading into zero-capacity buffer should return 0", 0, bytesRead);
    }

    @Test
    public void testRead_FromEmptyChannel_ReturnsEndOfStream() throws IOException {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel emptyChannel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        List<SeekableByteChannel> channelsWithEmpty = new ArrayList<>();
        channelsWithEmpty.add(emptyChannel);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithEmpty);
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(359);
        int bytesRead = channel.read(buffer);
        
        assertEquals("Reading from empty channel should return -1 (EOF)", -1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testRead_FromFileChannel_ThrowsIOException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        
        channel.read(buffer); // This should throw IOException due to mock file behavior
    }

    @Test(expected = ClosedChannelException.class)
    public void testRead_OnClosedChannel_ThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        
        channel.read(buffer);
    }

    // === Channel State Tests ===

    @Test
    public void testIsOpen_OnNewChannel_ReturnsTrue() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        assertTrue("New channel should be open", channel.isOpen());
    }

    @Test
    public void testIsOpen_AfterClose_ReturnsFalse() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(twoMockFiles);
        
        channel.close();
        
        assertFalse("Closed channel should not be open", channel.isOpen());
    }

    @Test
    public void testIsOpen_OnEmptyChannel_ReturnsTrue() {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        
        assertTrue("Empty channel should be open", channel.isOpen());
    }

    // === Write Operation Tests (Should Always Fail) ===

    @Test(expected = NonWritableChannelException.class)
    public void testWrite_AlwaysThrowsException() {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        
        channel.write(null); // Should throw NonWritableChannelException
    }

    @Test(expected = NonWritableChannelException.class)
    public void testTruncate_AlwaysThrowsException() {
        List<SeekableByteChannel> emptyChannels = new ArrayList<>();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannels);
        
        channel.truncate(0L); // Should throw NonWritableChannelException
    }

    // === Error Handling Tests ===

    @Test(expected = NullPointerException.class)
    public void testWithNullChannelInList_ThrowsException() throws IOException {
        List<SeekableByteChannel> channelsWithNull = new ArrayList<>();
        channelsWithNull.add(null);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithNull);
        
        channel.size(); // Any operation should trigger the NPE
    }

    @Test(expected = NullPointerException.class)
    public void testPositionOnChannelWithNull_ThrowsException() throws IOException {
        List<SeekableByteChannel> channelsWithNull = new ArrayList<>();
        channelsWithNull.add(null);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithNull);
        
        channel.position(1L);
    }

    @Test(expected = NullPointerException.class)
    public void testIsOpenOnChannelWithNull_ThrowsException() {
        List<SeekableByteChannel> channelsWithNull = new ArrayList<>();
        channelsWithNull.add(null);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithNull);
        
        channel.isOpen();
    }

    @Test(expected = NullPointerException.class)
    public void testCloseOnChannelWithNull_ThrowsException() throws IOException {
        List<SeekableByteChannel> channelsWithNull = new ArrayList<>();
        channelsWithNull.add(null);
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(channelsWithNull);
        
        channel.close();
    }

    // === Helper Methods ===

    private FileChannel createFileChannel() throws IOException {
        Path path = mockFile.toPath();
        OpenOption[] options = new OpenOption[0];
        return FileChannel.open(path, options);
    }
}
package com.itextpdf.text.io;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link GroupedRandomAccessSource}.
 *
 * Note: The original class name 'GroupedRandomAccessSourceTestTest2' was likely a typo
 * and has been corrected to 'GroupedRandomAccessSourceTest'.
 */
public class GroupedRandomAccessSourceTest {

    private static final int SOURCE_CONTENT_SIZE = 100;
    private static final int NUM_SOURCES = 3;
    private static final int TOTAL_SIZE = SOURCE_CONTENT_SIZE * NUM_SOURCES;

    private GroupedRandomAccessSource groupedSource;
    private byte[] singleSourceContent;

    @Before
    public void setUp() throws IOException {
        singleSourceContent = createSequentialBytes(0, SOURCE_CONTENT_SIZE);

        RandomAccessSource[] sources = new RandomAccessSource[NUM_SOURCES];
        for (int i = 0; i < NUM_SOURCES; i++) {
            // Each source contains the same byte sequence: 0, 1, 2, ..., 99
            sources[i] = new ArrayRandomAccessSource(singleSourceContent);
        }
        groupedSource = new GroupedRandomAccessSource(sources);
    }

    /**
     * Verifies that a read operation spanning the entire grouped source correctly
     * concatenates the content from all underlying sources.
     */
    @Test
    public void get_whenReadingFullSource_readsAllBytesCorrectly() throws IOException {
        // Arrange
        byte[] buffer = new byte[TOTAL_SIZE];

        // Act
        int bytesRead = groupedSource.get(0, buffer, 0, TOTAL_SIZE);

        // Assert
        assertEquals("Should read all bytes from the grouped source", TOTAL_SIZE, bytesRead);

        // Verify content from each of the 3 underlying sources
        for (int i = 0; i < NUM_SOURCES; i++) {
            int bufferOffset = i * SOURCE_CONTENT_SIZE;
            assertArrayRegionEquals("Content from source " + (i + 1) + " should be correct",
                    singleSourceContent, 0, buffer, bufferOffset, SOURCE_CONTENT_SIZE);
        }
    }

    /**
     * Verifies that attempting to read beyond the end of the grouped source
     * only returns the available bytes up to the end.
     */
    @Test
    public void get_whenReadingPastEndOfSource_readsOnlyAvailableBytes() throws IOException {
        // Arrange
        byte[] buffer = new byte[TOTAL_SIZE + 50]; // Buffer is larger than the source
        int readLength = TOTAL_SIZE + 1; // Attempt to read one byte past the end

        // Act
        int bytesRead = groupedSource.get(0, buffer, 0, readLength);

        // Assert
        assertEquals("Should only read bytes up to the total length of the source", TOTAL_SIZE, bytesRead);
    }

    /**
     * Verifies that a read operation starting in one source and ending in another
     * correctly combines the data from both.
     */
    @Test
    public void get_whenReadingAcrossSourceBoundary_readsCorrectCombinedBytes() throws IOException {
        // Arrange
        // Read 100 bytes starting at offset 150. This spans two sources:
        // - 50 bytes from the end of source 2 (global offset 150-199)
        // - 50 bytes from the start of source 3 (global offset 200-249)
        int readOffset = 150;
        int readLength = 100;
        byte[] buffer = new byte[readLength];

        // Act
        int bytesRead = groupedSource.get(readOffset, buffer, 0, readLength);

        // Assert
        assertEquals("Should read the requested number of bytes", readLength, bytesRead);

        // Verify the first part of the read (from source 2)
        byte[] expectedFromSource2 = createSequentialBytes(50, 50); // Bytes 50..99
        assertArrayRegionEquals("First 50 bytes should come from the end of source 2",
                expectedFromSource2, 0, buffer, 0, 50);

        // Verify the second part of the read (from source 3)
        byte[] expectedFromSource3 = createSequentialBytes(0, 50); // Bytes 0..49
        assertArrayRegionEquals("Next 50 bytes should come from the start of source 3",
                expectedFromSource3, 0, buffer, 50, 50);
    }


    // --- Helper Methods ---

    /**
     * Creates a byte array of a given size, filled with sequential byte values.
     *
     * @param startValue the starting byte value.
     * @param count      the number of bytes to generate.
     * @return a new byte array.
     */
    private byte[] createSequentialBytes(int startValue, int count) {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = (byte) (i + startValue);
        }
        return result;
    }

    /**
     * Asserts that a region of one byte array is equal to a region of another.
     *
     * @param message        The message to display on failure.
     * @param expected       The array containing the expected byte sequence.
     * @param expectedOffset The starting offset in the expected array.
     * @param actual         The array containing the actual byte sequence.
     * @param actualOffset   The starting offset in the actual array.
     * @param length         The number of bytes to compare.
     */
    private void assertArrayRegionEquals(String message, byte[] expected, int expectedOffset, byte[] actual, int actualOffset, int length) {
        for (int i = 0; i < length; i++) {
            if (expected[i + expectedOffset] != actual[i + actualOffset]) {
                String failureMessage = String.format("%s: Byte arrays differ at region index %d. Expected <%d> but was <%d>.",
                        message, i, expected[i + expectedOffset], actual[i + actualOffset]);
                fail(failureMessage);
            }
        }
    }
}
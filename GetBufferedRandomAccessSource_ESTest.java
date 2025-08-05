import com.itextpdf.text.io.ArrayRandomAccessSource;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link GetBufferedRandomAccessSource} class.
 * These tests verify its role as a buffered decorator for a RandomAccessSource.
 */
@DisplayName("GetBufferedRandomAccessSource")
class GetBufferedRandomAccessSourceTest {

    private byte[] sourceData;
    private RandomAccessSource arraySource;

    @BeforeEach
    void setUp() {
        sourceData = new byte[]{10, 20, 30, 40, 50, 60, 70, 80};
        arraySource = new ArrayRandomAccessSource(sourceData);
    }

    @Test
    @DisplayName("constructor throws NullPointerException if source is null")
    void constructor_withNullSource_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GetBufferedRandomAccessSource(null),
                "Constructor should not accept a null source.");
    }

    @Nested
    @DisplayName("length() method")
    class LengthTests {

        @Test
        @DisplayName("returns the correct length of the underlying source")
        void length_onNonEmptySource_returnsCorrectLength() {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            assertEquals(sourceData.length, bufferedSource.length());
        }

        @Test
        @DisplayName("returns 0 for an empty underlying source")
        void length_onEmptySource_returnsZero() {
            RandomAccessSource emptySource = new ArrayRandomAccessSource(new byte[0]);
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(emptySource);
            assertEquals(0L, bufferedSource.length());
        }

        @Test
        @DisplayName("propagates exceptions from underlying source when closed")
        void length_afterClose_throwsException() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            bufferedSource.close(); // Closes the underlying ArrayRandomAccessSource

            // ArrayRandomAccessSource throws IllegalStateException after being closed
            assertThrows(IllegalStateException.class, bufferedSource::length,
                    "Should propagate exception from closed underlying source.");
        }
    }

    @Nested
    @DisplayName("get(long position) method")
    class GetSingleByteTests {

        @Test
        @DisplayName("returns the correct unsigned byte value at a valid position")
        void get_atValidPosition_returnsCorrectUnsignedByte() throws IOException {
            byte[] data = {(byte) 200}; // -56 signed
            RandomAccessSource source = new ArrayRandomAccessSource(data);
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

            assertEquals(200, bufferedSource.get(0));
        }

        @Test
        @DisplayName("returns -1 for a position at or beyond the source length")
        void get_atPositionBeyondLength_returnsMinusOne() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            assertEquals(-1, bufferedSource.get(sourceData.length), "Position at length should be EOF.");
            assertEquals(-1, bufferedSource.get(sourceData.length + 100), "Position beyond length should be EOF.");
        }

        @Test
        @DisplayName("returns the correct value when called multiple times, testing the buffer")
        void get_multipleTimesAtSamePosition_usesBuffer() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            
            // First read should populate the buffer
            assertEquals(sourceData[1], bufferedSource.get(1));
            
            // Second read should hit the buffer and return the same value
            assertEquals(sourceData[1], bufferedSource.get(1));
        }

        @Test
        @DisplayName("throws exception for a negative position if underlying source does")
        void get_withNegativePosition_throwsException() {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            
            // ArrayRandomAccessSource throws ArrayIndexOutOfBoundsException for negative positions
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> bufferedSource.get(-1L),
                    "Should propagate exception for invalid negative position.");
        }

        @Test
        @DisplayName("throws exception when called on a closed source")
        void get_onClosedSource_throwsException() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            bufferedSource.close();

            assertThrows(IllegalStateException.class, () -> bufferedSource.get(0),
                    "Should not be able to read from a closed source.");
        }
    }

    @Nested
    @DisplayName("get(long position, byte[] bytes, int off, int len) method")
    class GetByteArrayTests {

        @Test
        @DisplayName("reads the requested number of bytes into the destination array")
        void get_readsBytesIntoDestination() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            byte[] destination = new byte[4];
            int bytesRead = bufferedSource.get(2, destination, 0, 4);

            assertEquals(4, bytesRead);
            assertArrayEquals(new byte[]{30, 40, 50, 60}, destination);
        }

        @Test
        @DisplayName("reads only available bytes if length is greater than remaining source")
        void get_readingMoreBytesThanAvailable_returnsAvailableLength() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            byte[] destination = new byte[10];
            int bytesRead = bufferedSource.get(5, destination, 0, 10);

            assertEquals(3, bytesRead, "Should only read the 3 remaining bytes.");
            assertArrayEquals(new byte[]{60, 70, 80}, java.util.Arrays.copyOf(destination, 3));
        }

        @Test
        @DisplayName("returns 0 when a read length of 0 is requested")
        void get_withZeroLength_returnsZero() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            byte[] destination = new byte[10];
            int bytesRead = bufferedSource.get(0, destination, 0, 0);

            assertEquals(0, bytesRead);
        }

        @Test
        @DisplayName("returns -1 when a negative read length is requested")
        void get_withNegativeLength_returnsMinusOne() throws IOException {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            byte[] destination = new byte[10];
            int bytesRead = bufferedSource.get(0, destination, 0, -5);

            assertEquals(-1, bytesRead);
        }

        @Test
        @DisplayName("throws exception for invalid buffer parameters")
        void get_withInvalidBufferParameters_throwsException() {
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
            byte[] destination = new byte[5];

            assertThrows(NullPointerException.class, () -> bufferedSource.get(0, null, 0, 1), "Null buffer should throw.");
            assertThrows(IndexOutOfBoundsException.class, () -> bufferedSource.get(0, destination, -1, 1), "Negative offset should throw.");
            assertThrows(IndexOutOfBoundsException.class, () -> bufferedSource.get(0, destination, 0, 6), "Length too large for buffer should throw.");
            assertThrows(IndexOutOfBoundsException.class, () -> bufferedSource.get(0, destination, 3, 3), "Offset + length too large for buffer should throw.");
        }
    }

    @Nested
    @DisplayName("close() method")
    class CloseTests {

        @Test
        @DisplayName("delegates the close call to the underlying source")
        void close_delegatesToUnderlyingSource() throws IOException {
            // Using a mock to verify interaction
            RandomAccessSource mockSource = mock(RandomAccessSource.class);
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(mockSource);

            bufferedSource.close();

            verify(mockSource, times(1)).close();
        }

        @Test
        @DisplayName("can be called multiple times without error")
        void close_canBeCalledMultipleTimes() throws IOException {
            RandomAccessSource mockSource = mock(RandomAccessSource.class);
            GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(mockSource);

            assertDoesNotThrow(() -> {
                bufferedSource.close();
                bufferedSource.close();
            });

            // The underlying source should still only be closed once.
            verify(mockSource, times(1)).close();
        }
    }
}
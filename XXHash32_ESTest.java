package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XXHash32} class.
 *
 * The tests cover basic hashing functionality, state management (reset),
 * and handling of various edge cases and invalid inputs.
 */
public class XXHash32Test {

    // This is the known hash value for an empty input with the default seed (0).
    private static final long EMPTY_INPUT_HASH_DEFAULT_SEED = 46947589L;

    // --- Constructor and State Initialization Tests ---

    @Test
    public void testDefaultConstructorInitializesToDefaultSeed() {
        // Arrange & Act
        final XXHash32 hash = new XXHash32();

        // Assert
        // The initial value should be the pre-calculated hash for an empty stream with seed 0.
        assertEquals(EMPTY_INPUT_HASH_DEFAULT_SEED, hash.getValue());
    }

    @Test
    public void testConstructorWithSeedInitializesValueCorrectly() {
        // Arrange
        final int seed = 97;
        // This is the known hash value for an empty stream with the given seed.
        final long expectedHash = 3659767818L;

        // Act
        final XXHash32 hash = new XXHash32(seed);

        // Assert
        assertEquals(expectedHash, hash.getValue());
    }

    // --- Hashing Functionality Tests ---

    @Test
    public void testUpdateWithSingleByte() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        // The update(int) method uses the lower 8 bits of the integer.
        // 2026 is 0x7EA, so the byte value is 0xEA.
        final int inputByte = 2026;
        final long expectedHash = 968812856L; // Known hash for input byte 0xEA

        // Act
        hash.update(inputByte);
        final long actualHash = hash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testUpdateWithMultipleSingleByteCalls() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        // Known hash for the byte sequence {2, 2, 0, 8}.
        final long expectedHash = 1429036944L;

        // Act
        hash.update(2);
        hash.update(2);
        hash.update(0);
        hash.update(8);
        final long actualHash = hash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testUpdateWithDataLargerThanInternalBuffer() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        // The internal buffer is 16 bytes. We update with 24 bytes to test buffer processing.
        final byte[] data = new byte[25];
        data[3] = 16; // The input is {0, 0, 0, 16, 0, ...}
        final long expectedHash = 281612550L; // Known hash for this 24-byte input.

        // Act
        hash.update(data, 0, 24);
        final long actualHash = hash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testUpdateWithDataLargerThanBufferLeavingRemainder() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        // The internal buffer is 16 bytes. An update with 21 bytes will process
        // one full 16-byte chunk and leave 5 bytes in the buffer.
        final byte[] data = new byte[42]; // All zeros
        final long expectedHash = 86206869L; // Known hash for 21 zero bytes.

        // Act
        hash.update(data, 21, 21);
        final long actualHash = hash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testUpdateWithMultipleChunks() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] data = new byte[25];
        data[1] = 16;
        // Known hash for the concatenated data from the three update calls.
        final long expectedHash = 1465785993L;

        // Act
        // The final hash is of the concatenation of these chunks.
        // 1. Hash 4 bytes from offset 16: {0, 0, 0, 0}
        hash.update(data, 16, 4);
        // 2. Hash 4 bytes from offset 1: {16, 0, 0, 0}
        hash.update(data, 1, 4);
        // 3. Hash 16 bytes from offset 0: {0, 16, 0, ...}
        hash.update(data, 0, 16);
        final long actualHash = hash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }

    // --- State Management Tests ---

    @Test
    public void testResetRestoresInitialState() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final long initialHash = hash.getValue();

        // Act: Update with some data, which changes the internal state.
        hash.update(new byte[]{1, 2, 3, 4}, 0, 4);
        final long hashAfterUpdate = hash.getValue();

        // Act: Reset the hasher.
        hash.reset();
        final long hashAfterReset = hash.getValue();

        // Assert
        assertNotEquals("Hash should change after an update", initialHash, hashAfterUpdate);
        assertEquals("Hash after reset should be the same as the initial hash", initialHash, hashAfterReset);
    }

    // --- Edge Case and Invalid Input Tests ---

    @Test
    public void testUpdateWithZeroLengthIsNoOp() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] input = new byte[8];
        final long initialHash = hash.getValue();

        // Act: Update with a zero length. This should not change the state.
        hash.update(input, 0, 0);

        // Assert
        assertEquals("Hash value should be unchanged for a zero-length update", initialHash, hash.getValue());
    }

    @Test
    public void testUpdateWithNegativeLengthIsNoOp() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] input = new byte[8];
        final long initialHash = hash.getValue();

        // Act: Update with a negative length. This should not change the state.
        hash.update(input, 1, -5);

        // Assert
        assertEquals("Hash value should be unchanged for a negative-length update", initialHash, hash.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullBufferThrowsException() {
        // Arrange
        final XXHash32 hash = new XXHash32();

        // Act & Assert
        hash.update(null, 0, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateWithOffsetOutOfBoundsThrowsException() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] data = new byte[10];
        final int invalidOffset = 11;

        // Act & Assert
        hash.update(data, invalidOffset, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateWithLengthOutOfBoundsThrowsException() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] data = new byte[10];
        final int offset = 5;
        final int invalidLength = 6; // offset + length (11) > data.length (10)

        // Act & Assert
        hash.update(data, offset, invalidLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithOffsetAndLengthIntegerOverflowThrowsException() {
        // Arrange
        final XXHash32 hash = new XXHash32();
        final byte[] input = new byte[10];
        // Choose offset and length such that their sum overflows an integer.
        final int offset = Integer.MAX_VALUE - 5;
        final int length = 10;

        // Act & Assert: off + len overflows, which should be detected.
        hash.update(input, offset, length);
    }
}
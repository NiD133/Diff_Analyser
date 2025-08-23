package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the round-trip consistency of the Base16 codec (encode -> decode).
 */
public class Base16Test {

    private final Base16 base16 = new Base16();
    private final Random random = new Random();

    /**
     * Tests that encoding a byte array and then decoding it returns the original data.
     * This test is parameterized to run for several small array sizes (0 to 11),
     * covering edge cases like empty and single-byte arrays.
     *
     * @param arraySize the size of the random byte array to generate and test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    void encodeAndDecodeShouldBeReversibleForRandomArrays(final int arraySize) {
        // Arrange: Create a random byte array of the specified size.
        final byte[] originalData = new byte[arraySize];
        random.nextBytes(originalData);

        // Act: Encode the data and then decode it back.
        final byte[] encodedData = base16.encode(originalData);
        final byte[] decodedData = base16.decode(encodedData);

        // Assert: The decoded data should be identical to the original data.
        assertArrayEquals(originalData, decodedData,
            "Round-trip encoding/decoding failed for a byte array of size " + arraySize);
    }
}
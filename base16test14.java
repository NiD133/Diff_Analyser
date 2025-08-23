package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

/**
 * Tests the Base16 class to ensure that encoding and decoding operations are
 * correctly implemented and reversible, particularly for random binary data.
 */
public class Base16Test {

    private static final int NUMBER_OF_RANDOM_TESTS = 4;
    private static final int MAX_RANDOM_DATA_LENGTH = 10000;

    private final Base16 base16 = new Base16();
    private final Random random = new Random();

    @RepeatedTest(value = NUMBER_OF_RANDOM_TESTS, name = "Random Test {currentRepetition}/{totalRepetitions}")
    @DisplayName("Encoding and then decoding random byte arrays should result in the original data")
    void encodeAndDecodeOfRandomDataShouldBeReversible() {
        // GIVEN: a random byte array of a random length
        final int dataLength = random.nextInt(MAX_RANDOM_DATA_LENGTH) + 1; // +1 to ensure length is at least 1
        final byte[] originalData = new byte[dataLength];
        random.nextBytes(originalData);

        // WHEN: the data is encoded and then decoded
        final byte[] encodedData = base16.encode(originalData);
        final byte[] decodedData = base16.decode(encodedData);

        // THEN: the decoded data should be identical to the original data
        assertArrayEquals(originalData, decodedData, "Decoded data should match original data");
    }
}
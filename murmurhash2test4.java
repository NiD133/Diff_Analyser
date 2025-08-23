package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link MurmurHash2} class.
 * This test suite validates the 32-bit and 64-bit hash implementations against a set of
 * known-good "golden" values for various inputs and seeds.
 */
// Renamed for clarity and adherence to standard naming conventions.
// The original name "MurmurHash2TestTest4" was redundant and confusing.
public class MurmurHash2Test {

    // The special seed is not defined in the original MurmurHash2 C++ code,
    // so this is an arbitrary number used for testing the seeded hash variants.
    private static final int SPECIAL_TEST_SEED_32 = 0x12345678;
    private static final int SPECIAL_TEST_SEED_64 = 0x87654321;

    // --- Test Data ---

    /**
     * Test input data of various lengths, including an empty array.
     * Sourced from the original test suite.
     */
    private static final byte[][] INPUT_DATA = {
        bytes(0xed, 0x53, 0xc4, 0xa5, 0x3b, 0x1b, 0xbd, 0xc2, 0x52, 0x7d, 0xc3, 0xef, 0x53, 0x5f, 0xae, 0x3b),
        bytes(0x21, 0x65, 0x59, 0x4e, 0xd8, 0x12, 0xf9, 0x05, 0x80, 0xe9, 0x1e, 0xed, 0xe4, 0x56, 0xbb),
        bytes(0x2b, 0x02, 0xb1, 0xd0, 0x3d, 0xce, 0x31, 0x3d, 0x97, 0xc4, 0x91, 0x0d, 0xf7, 0x17),
        bytes(0x8e, 0xa7, 0x9a, 0x02, 0xe8, 0xb9, 0x6a, 0xda, 0x92, 0xad, 0xe9, 0x2d, 0x21),
        bytes(0xa9, 0x6d, 0xea, 0x77, 0x06, 0xce, 0x1b, 0x85, 0x48, 0x27, 0x4c, 0xfe),
        bytes(0xec, 0x93, 0xa0, 0x12, 0x60, 0xee, 0xc8, 0x0a, 0xc5, 0x90, 0x62),
        bytes(0x55, 0x6d, 0x93, 0x66, 0x14, 0x6d, 0xdf, 0x00, 0x58, 0x99),
        bytes(0x3c, 0x72, 0x20, 0x1f, 0xd2, 0x59, 0x19, 0xdb, 0xa1),
        bytes(0x23, 0xa8, 0xb1, 0x87, 0x55, 0xf7, 0x8a, 0x4b),
        bytes(0xe2, 0x42, 0x1c, 0x2d, 0xc1, 0xe4, 0x3e),
        bytes(0x66, 0xa6, 0xb5, 0x5a, 0x74, 0xd9),
        bytes(0xe8, 0x76, 0xa8, 0x90, 0x76),
        bytes(0xeb, 0x25, 0x3f, 0x87),
        bytes(0x37, 0xa0, 0xa9),
        bytes(0x5b, 0x5d),
        bytes(0x7e),
        bytes() // Empty data
    };

    /** Expected 32-bit hash results for INPUT_DATA using the default library seed. */
    private static final int[] EXPECTED_HASH32_STANDARD = {
        0x96814fb3, 0x485dcaba, 0x331dc4ae, 0xc6a7bf2f, 0xcdf35de0, 0xd9dec7cc,
        0x63a7318a, 0xd0d3c2de, 0x90923aef, 0xaf35c1e2, 0x735377b2, 0x366c98f3,
        0x9c48ee29, 0x0b615790, 0xb4308ac1, 0xec98125a, 0x106e08d9
    };

    /** Expected 32-bit hash results for INPUT_DATA using SPECIAL_TEST_SEED_32. */
    private static final int[] EXPECTED_HASH32_SEEDED = {
        0xd92e493e, 0x8b50903b, 0xc3372a7b, 0x48f07e9e, 0x8a5e4a6e, 0x57916df4,
        0xa346171f, 0x1e319c86, 0x9e1a03cd, 0x9f973e6c, 0x2d8c77f5, 0xabed8751,
        0x296708b6, 0x24f8078b, 0x111b1553, 0xa7da1996, 0xfe776c70
    };

    /** Expected 64-bit hash results for INPUT_DATA using the default library seed. */
    private static final long[] EXPECTED_HASH64_STANDARD = {
        0x4987cb15118a83d9L, 0x28e2a79e3f0394d9L, 0x8f4600d786fc5c05L, 0xa09b27fea4b54af3L,
        0x25f34447525bfd1eL, 0x32fad4c21379c7bfL, 0x4b30b99a9d931921L, 0x4e5dab004f936cdbL,
        0x06825c27bc96cf40L, 0xff4bf2f8a4823905L, 0x7f7e950c064e6367L, 0x821ade90caaa5889L,
        0x6d28c915d791686aL, 0x9c32649372163ba2L, 0xd66ae956c14d5212L, 0x38ed30ee5161200fL,
        0x9bfae0a4e613fc3cL
    };

    /** Expected 64-bit hash results for INPUT_DATA using SPECIAL_TEST_SEED_64. */
    private static final long[] EXPECTED_HASH64_SEEDED = {
        0x0822b1481a92e97bL, 0xf8a9223fef0822ddL, 0x4b49e56affae3a89L, 0xc970296e32e1d1c1L,
        0xe2f9f88789f1b08fL, 0x2b0459d9b4c10c61L, 0x377e97ea9197ee89L, 0xd2ccad460751e0e7L,
        0xff162ca8d6da8c47L, 0xf12e051405769857L, 0xdabba41293d5b035L, 0xacf326b0bb690d0eL,
        0x0617f431bc1a8e04L, 0x15b81f28d576e1b2L, 0x28c1fe59e4f8e5baL, 0x694dd315c9354ca9L,
        0xa97052a8f088ae6cL
    };

    // --- Parameterized Test Data Providers ---

    static Stream<Arguments> standard32BitSource() {
        return IntStream.range(0, INPUT_DATA.length).mapToObj(i -> Arguments.of(
            INPUT_DATA[i], EXPECTED_HASH32_STANDARD[i], String.format("input #%d, length %d", i, INPUT_DATA[i].length)));
    }

    static Stream<Arguments> seeded32BitSource() {
        return IntStream.range(0, INPUT_DATA.length).mapToObj(i -> Arguments.of(
            INPUT_DATA[i], EXPECTED_HASH32_SEEDED[i], String.format("input #%d, length %d", i, INPUT_DATA[i].length)));
    }

    static Stream<Arguments> standard64BitSource() {
        return IntStream.range(0, INPUT_DATA.length).mapToObj(i -> Arguments.of(
            INPUT_DATA[i], EXPECTED_HASH64_STANDARD[i], String.format("input #%d, length %d", i, INPUT_DATA[i].length)));
    }

    static Stream<Arguments> seeded64BitSource() {
        return IntStream.range(0, INPUT_DATA.length).mapToObj(i -> Arguments.of(
            INPUT_DATA[i], EXPECTED_HASH64_SEEDED[i], String.format("input #%d, length %d", i, INPUT_DATA[i].length)));
    }

    // --- Test Cases ---

    @DisplayName("Test MurmurHash2 32-bit with default seed against known values")
    @ParameterizedTest(name = "{2}")
    @MethodSource("standard32BitSource")
    void testHash32_withDefaultSeed(final byte[] data, final int expectedHash, final String testName) {
        final int actualHash = MurmurHash2.hash32(data, data.length);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("Test MurmurHash2 32-bit with a special seed against known values")
    @ParameterizedTest(name = "{2}")
    @MethodSource("seeded32BitSource")
    void testHash32_withSpecialSeed(final byte[] data, final int expectedHash, final String testName) {
        final int actualHash = MurmurHash2.hash32(data, data.length, SPECIAL_TEST_SEED_32);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("Test MurmurHash2 64-bit with default seed against known values")
    @ParameterizedTest(name = "{2}")
    @MethodSource("standard64BitSource")
    void testHash64_withDefaultSeed(final byte[] data, final long expectedHash, final String testName) {
        final long actualHash = MurmurHash2.hash64(data, data.length);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("Test MurmurHash2 64-bit with a special seed against known values")
    @ParameterizedTest(name = "{2}")
    @MethodSource("seeded64BitSource")
    void testHash64_withSpecialSeed(final byte[] data, final long expectedHash, final String testName) {
        final long actualHash = MurmurHash2.hash64(data, data.length, SPECIAL_TEST_SEED_64);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    @DisplayName("hash32(String, from, length) should generate correct hash for a substring")
    void testHash32_forSubstring() {
        final String text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";
        // This test case validates hashing a substring of a larger string.
        // The substring is "rem ipsum dolor sit amet, consectetur adipisicing e".
        final int offset = 2;
        final int length = text.length() - 4;

        // The expected hash is a "golden value" established from a trusted implementation.
        final int expectedHash = 0x4d666d90;
        final int actualHash = MurmurHash2.hash32(text, offset, length);

        assertEquals(expectedHash, actualHash);
    }

    /**
     * Helper method to make byte array initialization cleaner by removing the need for explicit casting.
     * @param values A sequence of integer values that will be cast to bytes.
     * @return A byte array.
     */
    private static byte[] bytes(final int... values) {
        final byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = (byte) values[i];
        }
        return result;
    }
}
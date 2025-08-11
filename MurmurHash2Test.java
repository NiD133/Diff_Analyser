package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("MurmurHash2: 32-bit and 64-bit test vectors and string tests")
class MurmurHash2Test {

    // Seeds used in the test vectors (do not assume these are Murmur defaults)
    private static final int SPECIAL_SEED_32 = 0x71b4954d;
    private static final int SPECIAL_SEED_64 = 0x344d1f5c;

    /** Random input data with various length (including empty). */
    private static final byte[][] INPUTS = {
            { (byte) 0xed, (byte) 0x53, (byte) 0xc4, (byte) 0xa5, (byte) 0x3b, (byte) 0x1b, (byte) 0xbd, (byte) 0xc2,
                    (byte) 0x52, (byte) 0x7d, (byte) 0xc3, (byte) 0xef, (byte) 0x53, (byte) 0x5f, (byte) 0xae,
                    (byte) 0x3b },
            { (byte) 0x21, (byte) 0x65, (byte) 0x59, (byte) 0x4e, (byte) 0xd8, (byte) 0x12, (byte) 0xf9, (byte) 0x05,
                    (byte) 0x80, (byte) 0xe9, (byte) 0x1e, (byte) 0xed, (byte) 0xe4, (byte) 0x56, (byte) 0xbb },
            { (byte) 0x2b, (byte) 0x02, (byte) 0xb1, (byte) 0xd0, (byte) 0x3d, (byte) 0xce, (byte) 0x31, (byte) 0x3d,
                    (byte) 0x97, (byte) 0xc4, (byte) 0x91, (byte) 0x0d, (byte) 0xf7, (byte) 0x17 },
            { (byte) 0x8e, (byte) 0xa7, (byte) 0x9a, (byte) 0x02, (byte) 0xe8, (byte) 0xb9, (byte) 0x6a, (byte) 0xda,
                    (byte) 0x92, (byte) 0xad, (byte) 0xe9, (byte) 0x2d, (byte) 0x21 },
            { (byte) 0xa9, (byte) 0x6d, (byte) 0xea, (byte) 0x77, (byte) 0x06, (byte) 0xce, (byte) 0x1b, (byte) 0x85,
                    (byte) 0x48, (byte) 0x27, (byte) 0x4c, (byte) 0xfe },
            { (byte) 0xec, (byte) 0x93, (byte) 0xa0, (byte) 0x12, (byte) 0x60, (byte) 0xee, (byte) 0xc8, (byte) 0x0a,
                    (byte) 0xc5, (byte) 0x90, (byte) 0x62 },
            { (byte) 0x55, (byte) 0x6d, (byte) 0x93, (byte) 0x66, (byte) 0x14, (byte) 0x6d, (byte) 0xdf, (byte) 0x00,
                    (byte) 0x58, (byte) 0x99 },
            { (byte) 0x3c, (byte) 0x72, (byte) 0x20, (byte) 0x1f, (byte) 0xd2, (byte) 0x59, (byte) 0x19, (byte) 0xdb,
                    (byte) 0xa1 },
            { (byte) 0x23, (byte) 0xa8, (byte) 0xb1, (byte) 0x87, (byte) 0x55, (byte) 0xf7, (byte) 0x8a, (byte) 0x4b, },
            { (byte) 0xe2, (byte) 0x42, (byte) 0x1c, (byte) 0x2d, (byte) 0xc1, (byte) 0xe4, (byte) 0x3e },
            { (byte) 0x66, (byte) 0xa6, (byte) 0xb5, (byte) 0x5a, (byte) 0x74, (byte) 0xd9 },
            { (byte) 0xe8, (byte) 0x76, (byte) 0xa8, (byte) 0x90, (byte) 0x76 },
            { (byte) 0xeb, (byte) 0x25, (byte) 0x3f, (byte) 0x87 },
            { (byte) 0x37, (byte) 0xa0, (byte) 0xa9 },
            { (byte) 0x5b, (byte) 0x5d },
            { (byte) 0x7e },
            {}
    };

    /*
     * Expected results - from the original C implementation.
     */

    /** Murmur 32-bit hash results, default library seed. */
    private static final int[] EXPECTED_32_DEFAULT = {
            0x96814fb3, 0x485dcaba, 0x331dc4ae, 0xc6a7bf2f, 0xcdf35de0, 0xd9dec7cc,
            0x63a7318a, 0xd0d3c2de, 0x90923aef, 0xaf35c1e2, 0x735377b2, 0x366c98f3,
            0x9c48ee29, 0x0b615790, 0xb4308ac1, 0xec98125a, 0x106e08d9
    };

    /** Murmur 32-bit hash results, SPECIAL_SEED_32. */
    private static final int[] EXPECTED_32_SEEDED = {
            0xd92e493e, 0x8b50903b, 0xc3372a7b, 0x48f07e9e, 0x8a5e4a6e, 0x57916df4,
            0xa346171f, 0x1e319c86, 0x9e1a03cd, 0x9f973e6c, 0x2d8c77f5, 0xabed8751,
            0x296708b6, 0x24f8078b, 0x111b1553, 0xa7da1996, 0xfe776c70
    };

    /** Murmur 64-bit hash results, default library seed. */
    private static final long[] EXPECTED_64_DEFAULT = {
            0x4987cb15118a83d9L, 0x28e2a79e3f0394d9L, 0x8f4600d786fc5c05L, 0xa09b27fea4b54af3L,
            0x25f34447525bfd1eL, 0x32fad4c21379c7bfL, 0x4b30b99a9d931921L, 0x4e5dab004f936cdbL,
            0x06825c27bc96cf40L, 0xff4bf2f8a4823905L, 0x7f7e950c064e6367L, 0x821ade90caaa5889L,
            0x6d28c915d791686aL, 0x9c32649372163ba2L, 0xd66ae956c14d5212L, 0x38ed30ee5161200fL,
            0x9bfae0a4e613fc3cL
    };

    /** Murmur 64-bit hash results, SPECIAL_SEED_64. */
    private static final long[] EXPECTED_64_SEEDED = {
            0x0822b1481a92e97bL, 0xf8a9223fef0822ddL, 0x4b49e56affae3a89L, 0xc970296e32e1d1c1L,
            0xe2f9f88789f1b08fL, 0x2b0459d9b4c10c61L, 0x377e97ea9197ee89L, 0xd2ccad460751e0e7L,
            0xff162ca8d6da8c47L, 0xf12e051405769857L, 0xdabba41293d5b035L, 0xacf326b0bb690d0eL,
            0x0617f431bc1a8e04L, 0x15b81f28d576e1b2L, 0x28c1fe59e4f8e5baL, 0x694dd315c9354ca9L,
            0xa97052a8f088ae6cL
    };

    /** Dummy test text. */
    private static final String SAMPLE_TEXT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";

    // --------------------------
    // Parameter sources
    // --------------------------

    private static Stream<Arguments> vectors32Default() {
        return IntStream.range(0, INPUTS.length)
                .mapToObj(i -> Arguments.of(i, INPUTS[i], INPUTS[i].length, EXPECTED_32_DEFAULT[i]));
    }

    private static Stream<Arguments> vectors32Seeded() {
        return IntStream.range(0, INPUTS.length)
                .mapToObj(i -> Arguments.of(i, INPUTS[i], INPUTS[i].length, EXPECTED_32_SEEDED[i]));
    }

    private static Stream<Arguments> vectors64Default() {
        return IntStream.range(0, INPUTS.length)
                .mapToObj(i -> Arguments.of(i, INPUTS[i], INPUTS[i].length, EXPECTED_64_DEFAULT[i]));
    }

    private static Stream<Arguments> vectors64Seeded() {
        return IntStream.range(0, INPUTS.length)
                .mapToObj(i -> Arguments.of(i, INPUTS[i], INPUTS[i].length, EXPECTED_64_SEEDED[i]));
    }

    // --------------------------
    // 32-bit tests
    // --------------------------

    @ParameterizedTest(name = "32-bit default seed: example {0}, len={2}")
    @MethodSource("vectors32Default")
    void hash32_withDefaultSeed_matchesReferenceVectors(final int index, final byte[] data, final int length,
            final int expected) {
        final int actual = MurmurHash2.hash32(data, length);
        assertEquals(expected, actual, () -> String.format(
                "Unexpected 32-bit hash for example %d (len=%d): got 0x%08x, expected 0x%08x",
                index, length, actual, expected));
    }

    @ParameterizedTest(name = "32-bit seed 0x{3}: example {0}, len={2}")
    @MethodSource("vectors32Seeded")
    void hash32_withCustomSeed_matchesReferenceVectors(final int index, final byte[] data, final int length,
            final int expected) {
        final int actual = MurmurHash2.hash32(data, length, SPECIAL_SEED_32);
        assertEquals(expected, actual, () -> String.format(
                "Unexpected 32-bit hash (seed=0x%08x) for example %d (len=%d): got 0x%08x, expected 0x%08x",
                SPECIAL_SEED_32, index, length, actual, expected));
    }

    @Test
    void hash32_string_defaultSeed() {
        final int hash = MurmurHash2.hash32(SAMPLE_TEXT);
        assertEquals(0xb3bf597e, hash, () -> String.format(
                "Unexpected 32-bit hash for string: got 0x%08x", hash));
    }

    @Test
    void hash32_string_slice_defaultSeed() {
        final int from = 2;
        final int len = SAMPLE_TEXT.length() - 4;
        final int hash = MurmurHash2.hash32(SAMPLE_TEXT, from, len);
        assertEquals(0x4d666d90, hash, () -> String.format(
                "Unexpected 32-bit hash for substring [from=%d,len=%d]: got 0x%08x", from, len, hash));
    }

    // --------------------------
    // 64-bit tests
    // --------------------------

    @ParameterizedTest(name = "64-bit default seed: example {0}, len={2}")
    @MethodSource("vectors64Default")
    void hash64_withDefaultSeed_matchesReferenceVectors(final int index, final byte[] data, final int length,
            final long expected) {
        final long actual = MurmurHash2.hash64(data, length);
        assertEquals(expected, actual, () -> String.format(
                "Unexpected 64-bit hash for example %d (len=%d): got 0x%016x, expected 0x%016x",
                index, length, actual, expected));
    }

    @ParameterizedTest(name = "64-bit seed 0x{3}: example {0}, len={2}")
    @MethodSource("vectors64Seeded")
    void hash64_withCustomSeed_matchesReferenceVectors(final int index, final byte[] data, final int length,
            final long expected) {
        final long actual = MurmurHash2.hash64(data, length, SPECIAL_SEED_64);
        assertEquals(expected, actual, () -> String.format(
                "Unexpected 64-bit hash (seed=0x%08x) for example %d (len=%d): got 0x%016x, expected 0x%016x",
                SPECIAL_SEED_64, index, length, actual, expected));
    }

    @Test
    void hash64_string_defaultSeed() {
        final long hash = MurmurHash2.hash64(SAMPLE_TEXT);
        assertEquals(0x0920e0c1b7eeb261L, hash, () -> String.format(
                "Unexpected 64-bit hash for string: got 0x%016x", hash));
    }

    @Test
    void hash64_string_slice_defaultSeed() {
        final int from = 2;
        final int len = SAMPLE_TEXT.length() - 4;
        final long hash = MurmurHash2.hash64(SAMPLE_TEXT, from, len);
        assertEquals(0xa8b33145194985a2L, hash, () -> String.format(
                "Unexpected 64-bit hash for substring [from=%d,len=%d]: got 0x%016x", from, len, hash));
    }
}
package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests MurmurHash2 by verifying its output against a set of known hash values for various inputs.
 */
public class MurmurHash2Test {

    /**
     * A specific seed used to generate one set of the expected hash values.
     * This allows testing the hash function's seeded variant.
     */
    private static final int TEST_SEED = 0x12345678;

    /**
     * A record to hold a complete test case, bundling an input byte array with all its
     * expected hash results (32/64 bit, with/without a custom seed). This approach is more
     * understandable than parallel arrays, as it keeps related data together.
     */
    private record Murmur2TestVector(byte[] input, int expected32Standard, int expected32Seeded,
                                      long expected64Standard, long expected64Seeded) {
    }

    /**
     * A comprehensive list of test vectors. Each vector contains an input and the pre-calculated
     * expected hash values for different MurmurHash2 function variants.
     */
    private static final Murmur2TestVector[] TEST_VECTORS = {
        new Murmur2TestVector(new byte[]{(byte) 0xed, (byte) 0x53, (byte) 0xc4, (byte) 0xa5, (byte) 0x3b, (byte) 0x1b, (byte) 0xbd, (byte) 0xc2, (byte) 0x52, (byte) 0x7d, (byte) 0xc3, (byte) 0xef, (byte) 0x53, (byte) 0x5f, (byte) 0xae, (byte) 0x3b}, 0x96814fb3, 0xd92e493e, 0x4987cb15118a83d9L, 0x0822b1481a92e97bL),
        new Murmur2TestVector(new byte[]{(byte) 0x21, (byte) 0x65, (byte) 0x59, (byte) 0x4e, (byte) 0xd8, (byte) 0x12, (byte) 0xf9, (byte) 0x05, (byte) 0x80, (byte) 0xe9, (byte) 0x1e, (byte) 0xed, (byte) 0xe4, (byte) 0x56, (byte) 0xbb}, 0x485dcaba, 0x8b50903b, 0x28e2a79e3f0394d9L, 0xf8a9223fef0822ddL),
        new Murmur2TestVector(new byte[]{(byte) 0x2b, (byte) 0x02, (byte) 0xb1, (byte) 0xd0, (byte) 0x3d, (byte) 0xce, (byte) 0x31, (byte) 0x3d, (byte) 0x97, (byte) 0xc4, (byte) 0x91, (byte) 0x0d, (byte) 0xf7, (byte) 0x17}, 0x331dc4ae, 0xc3372a7b, 0x8f4600d786fc5c05L, 0x4b49e56affae3a89L),
        new Murmur2TestVector(new byte[]{(byte) 0x8e, (byte) 0xa7, (byte) 0x9a, (byte) 0x02, (byte) 0xe8, (byte) 0xb9, (byte) 0x6a, (byte) 0xda, (byte) 0x92, (byte) 0xad, (byte) 0xe9, (byte) 0x2d, (byte) 0x21}, 0xc6a7bf2f, 0x48f07e9e, 0xa09b27fea4b54af3L, 0xc970296e32e1d1c1L),
        new Murmur2TestVector(new byte[]{(byte) 0xa9, (byte) 0x6d, (byte) 0xea, (byte) 0x77, (byte) 0x06, (byte) 0xce, (byte) 0x1b, (byte) 0x85, (byte) 0x48, (byte) 0x27, (byte) 0x4c, (byte) 0xfe}, 0xcdf35de0, 0x8a5e4a6e, 0x25f34447525bfd1eL, 0xe2f9f88789f1b08fL),
        new Murmur2TestVector(new byte[]{(byte) 0xec, (byte) 0x93, (byte) 0xa0, (byte) 0x12, (byte) 0x60, (byte) 0xee, (byte) 0xc8, (byte) 0x0a, (byte) 0xc5, (byte) 0x90, (byte) 0x62}, 0xd9dec7cc, 0x57916df4, 0x32fad4c21379c7bfL, 0x2b0459d9b4c10c61L),
        new Murmur2TestVector(new byte[]{(byte) 0x55, (byte) 0x6d, (byte) 0x93, (byte) 0x66, (byte) 0x14, (byte) 0x6d, (byte) 0xdf, (byte) 0x00, (byte) 0x58, (byte) 0x99}, 0x63a7318a, 0xa346171f, 0x4b30b99a9d931921L, 0x377e97ea9197ee89L),
        new Murmur2TestVector(new byte[]{(byte) 0x3c, (byte) 0x72, (byte) 0x20, (byte) 0x1f, (byte) 0xd2, (byte) 0x59, (byte) 0x19, (byte) 0xdb, (byte) 0xa1}, 0xd0d3c2de, 0x1e319c86, 0x4e5dab004f936cdbL, 0xd2ccad460751e0e7L),
        new Murmur2TestVector(new byte[]{(byte) 0x23, (byte) 0xa8, (byte) 0xb1, (byte) 0x87, (byte) 0x55, (byte) 0xf7, (byte) 0x8a, (byte) 0x4b}, 0x90923aef, 0x9e1a03cd, 0x06825c27bc96cf40L, 0xff162ca8d6da8c47L),
        new Murmur2TestVector(new byte[]{(byte) 0xe2, (byte) 0x42, (byte) 0x1c, (byte) 0x2d, (byte) 0xc1, (byte) 0xe4, (byte) 0x3e}, 0xaf35c1e2, 0x9f973e6c, 0xff4bf2f8a4823905L, 0xf12e051405769857L),
        new Murmur2TestVector(new byte[]{(byte) 0x66, (byte) 0xa6, (byte) 0xb5, (byte) 0x5a, (byte) 0x74, (byte) 0xd9}, 0x735377b2, 0x2d8c77f5, 0x7f7e950c064e6367L, 0xdabba41293d5b035L),
        new Murmur2TestVector(new byte[]{(byte) 0xe8, (byte) 0x76, (byte) 0xa8, (byte) 0x90, (byte) 0x76}, 0x366c98f3, 0xabed8751, 0x821ade90caaa5889L, 0xacf326b0bb690d0eL),
        new Murmur2TestVector(new byte[]{(byte) 0xeb, (byte) 0x25, (byte) 0x3f, (byte) 0x87}, 0x9c48ee29, 0x296708b6, 0x6d28c915d791686aL, 0x0617f431bc1a8e04L),
        new Murmur2TestVector(new byte[]{(byte) 0x37, (byte) 0xa0, (byte) 0xa9}, 0x0b615790, 0x24f8078b, 0x9c32649372163ba2L, 0x15b81f28d576e1b2L),
        new Murmur2TestVector(new byte[]{(byte) 0x5b, (byte) 0x5d}, 0xb4308ac1, 0x111b1553, 0xd66ae956c14d5212L, 0x28c1fe59e4f8e5baL),
        new Murmur2TestVector(new byte[]{(byte) 0x7e}, 0xec98125a, 0xa7da1996, 0x38ed30ee5161200fL, 0x694dd315c9354ca9L),
        new Murmur2TestVector(new byte[]{}, 0x106e08d9, 0xfe776c70, 0x9bfae0a4e613fc3cL, 0xa97052a8f088ae6cL)
    };

    static Stream<Arguments> provide32BitStandardHashCases() {
        return IntStream.range(0, TEST_VECTORS.length).mapToObj(
            i -> Arguments.of(TEST_VECTORS[i].input(), TEST_VECTORS[i].expected32Standard(), "Test Vector #" + i));
    }

    static Stream<Arguments> provide32BitSeededHashCases() {
        return IntStream.range(0, TEST_VECTORS.length).mapToObj(
            i -> Arguments.of(TEST_VECTORS[i].input(), TEST_VECTORS[i].expected32Seeded(), "Test Vector #" + i));
    }

    static Stream<Arguments> provide64BitStandardHashCases() {
        return IntStream.range(0, TEST_VECTORS.length).mapToObj(
            i -> Arguments.of(TEST_VECTORS[i].input(), TEST_VECTORS[i].expected64Standard(), "Test Vector #" + i));
    }

    static Stream<Arguments> provide64BitSeededHashCases() {
        return IntStream.range(0, TEST_VECTORS.length).mapToObj(
            i -> Arguments.of(TEST_VECTORS[i].input(), TEST_VECTORS[i].expected64Seeded(), "Test Vector #" + i));
    }

    @DisplayName("hash32(byte[], length) should produce known results")
    @ParameterizedTest(name = "{2} with input length {0}.length")
    @MethodSource("provide32BitStandardHashCases")
    void testHash32WithDefaultSeed(final byte[] data, final int expectedHash, final String testName) {
        final int actualHash = MurmurHash2.hash32(data, data.length);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("hash32(byte[], length, seed) should produce known results")
    @ParameterizedTest(name = "{2} with input length {0}.length")
    @MethodSource("provide32BitSeededHashCases")
    void testHash32WithCustomSeed(final byte[] data, final int expectedHash, final String testName) {
        final int actualHash = MurmurHash2.hash32(data, data.length, TEST_SEED);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("hash64(byte[], length) should produce known results")
    @ParameterizedTest(name = "{2} with input length {0}.length")
    @MethodSource("provide64BitStandardHashCases")
    void testHash64WithDefaultSeed(final byte[] data, final long expectedHash, final String testName) {
        final long actualHash = MurmurHash2.hash64(data, data.length);
        assertEquals(expectedHash, actualHash);
    }

    @DisplayName("hash64(byte[], length, seed) should produce known results")
    @ParameterizedTest(name = "{2} with input length {0}.length")
    @MethodSource("provide64BitSeededHashCases")
    void testHash64WithCustomSeed(final byte[] data, final long expectedHash, final String testName) {
        final long actualHash = MurmurHash2.hash64(data, data.length, TEST_SEED);
        assertEquals(expectedHash, actualHash);
    }
}
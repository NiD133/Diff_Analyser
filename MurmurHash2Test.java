/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test suite for MurmurHash2 implementation.
 * 
 * Tests verify hash computation correctness against expected values from the original C implementation.
 * The test data includes various byte arrays of different lengths and string inputs.
 */
class MurmurHash2Test {

    // Test constants
    private static final int CUSTOM_SEED_32_BIT = 0x71b4954d;
    private static final int CUSTOM_SEED_64_BIT = 0x344d1f5c;
    private static final String SAMPLE_TEXT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";
    
    // Substring test parameters
    private static final int SUBSTRING_START_INDEX = 2;
    private static final int SUBSTRING_END_OFFSET = 4; // characters to exclude from end

    /**
     * Test input data: Random byte arrays with various lengths (16 bytes down to 0 bytes).
     * These arrays are used to verify hash computation across different input sizes.
     */
    private static final byte[][] TEST_INPUT_DATA = {
        // 16 bytes
        { (byte) 0xed, (byte) 0x53, (byte) 0xc4, (byte) 0xa5, (byte) 0x3b, (byte) 0x1b, (byte) 0xbd, (byte) 0xc2,
          (byte) 0x52, (byte) 0x7d, (byte) 0xc3, (byte) 0xef, (byte) 0x53, (byte) 0x5f, (byte) 0xae, (byte) 0x3b },
        // 15 bytes
        { (byte) 0x21, (byte) 0x65, (byte) 0x59, (byte) 0x4e, (byte) 0xd8, (byte) 0x12, (byte) 0xf9, (byte) 0x05,
          (byte) 0x80, (byte) 0xe9, (byte) 0x1e, (byte) 0xed, (byte) 0xe4, (byte) 0x56, (byte) 0xbb },
        // 14 bytes
        { (byte) 0x2b, (byte) 0x02, (byte) 0xb1, (byte) 0xd0, (byte) 0x3d, (byte) 0xce, (byte) 0x31, (byte) 0x3d,
          (byte) 0x97, (byte) 0xc4, (byte) 0x91, (byte) 0x0d, (byte) 0xf7, (byte) 0x17 },
        // 13 bytes
        { (byte) 0x8e, (byte) 0xa7, (byte) 0x9a, (byte) 0x02, (byte) 0xe8, (byte) 0xb9, (byte) 0x6a, (byte) 0xda,
          (byte) 0x92, (byte) 0xad, (byte) 0xe9, (byte) 0x2d, (byte) 0x21 },
        // 12 bytes
        { (byte) 0xa9, (byte) 0x6d, (byte) 0xea, (byte) 0x77, (byte) 0x06, (byte) 0xce, (byte) 0x1b, (byte) 0x85,
          (byte) 0x48, (byte) 0x27, (byte) 0x4c, (byte) 0xfe },
        // 11 bytes
        { (byte) 0xec, (byte) 0x93, (byte) 0xa0, (byte) 0x12, (byte) 0x60, (byte) 0xee, (byte) 0xc8, (byte) 0x0a,
          (byte) 0xc5, (byte) 0x90, (byte) 0x62 },
        // 10 bytes
        { (byte) 0x55, (byte) 0x6d, (byte) 0x93, (byte) 0x66, (byte) 0x14, (byte) 0x6d, (byte) 0xdf, (byte) 0x00,
          (byte) 0x58, (byte) 0x99 },
        // 9 bytes
        { (byte) 0x3c, (byte) 0x72, (byte) 0x20, (byte) 0x1f, (byte) 0xd2, (byte) 0x59, (byte) 0x19, (byte) 0xdb,
          (byte) 0xa1 },
        // 8 bytes
        { (byte) 0x23, (byte) 0xa8, (byte) 0xb1, (byte) 0x87, (byte) 0x55, (byte) 0xf7, (byte) 0x8a, (byte) 0x4b },
        // 7 bytes
        { (byte) 0xe2, (byte) 0x42, (byte) 0x1c, (byte) 0x2d, (byte) 0xc1, (byte) 0xe4, (byte) 0x3e },
        // 6 bytes
        { (byte) 0x66, (byte) 0xa6, (byte) 0xb5, (byte) 0x5a, (byte) 0x74, (byte) 0xd9 },
        // 5 bytes
        { (byte) 0xe8, (byte) 0x76, (byte) 0xa8, (byte) 0x90, (byte) 0x76 },
        // 4 bytes
        { (byte) 0xeb, (byte) 0x25, (byte) 0x3f, (byte) 0x87 },
        // 3 bytes
        { (byte) 0x37, (byte) 0xa0, (byte) 0xa9 },
        // 2 bytes
        { (byte) 0x5b, (byte) 0x5d },
        // 1 byte
        { (byte) 0x7e },
        // 0 bytes (empty array)
        {}
    };

    /**
     * Expected 32-bit hash results using default seed.
     * These values were generated from the original C implementation.
     */
    private static final int[] EXPECTED_32BIT_HASHES_DEFAULT_SEED = {
        0x96814fb3, 0x485dcaba, 0x331dc4ae, 0xc6a7bf2f, 0xcdf35de0, 0xd9dec7cc,
        0x63a7318a, 0xd0d3c2de, 0x90923aef, 0xaf35c1e2, 0x735377b2, 0x366c98f3,
        0x9c48ee29, 0x0b615790, 0xb4308ac1, 0xec98125a, 0x106e08d9
    };

    /**
     * Expected 32-bit hash results using custom seed (0x71b4954d).
     * These values were generated from the original C implementation.
     */
    private static final int[] EXPECTED_32BIT_HASHES_CUSTOM_SEED = {
        0xd92e493e, 0x8b50903b, 0xc3372a7b, 0x48f07e9e, 0x8a5e4a6e, 0x57916df4,
        0xa346171f, 0x1e319c86, 0x9e1a03cd, 0x9f973e6c, 0x2d8c77f5, 0xabed8751,
        0x296708b6, 0x24f8078b, 0x111b1553, 0xa7da1996, 0xfe776c70
    };

    /**
     * Expected 64-bit hash results using default seed.
     * These values were generated from the original C implementation.
     */
    private static final long[] EXPECTED_64BIT_HASHES_DEFAULT_SEED = {
        0x4987cb15118a83d9L, 0x28e2a79e3f0394d9L, 0x8f4600d786fc5c05L,
        0xa09b27fea4b54af3L, 0x25f34447525bfd1eL, 0x32fad4c21379c7bfL,
        0x4b30b99a9d931921L, 0x4e5dab004f936cdbL, 0x06825c27bc96cf40L,
        0xff4bf2f8a4823905L, 0x7f7e950c064e6367L, 0x821ade90caaa5889L,
        0x6d28c915d791686aL, 0x9c32649372163ba2L, 0xd66ae956c14d5212L,
        0x38ed30ee5161200fL, 0x9bfae0a4e613fc3cL
    };

    /**
     * Expected 64-bit hash results using custom seed (0x344d1f5c).
     * These values were generated from the original C implementation.
     */
    private static final long[] EXPECTED_64BIT_HASHES_CUSTOM_SEED = {
        0x0822b1481a92e97bL, 0xf8a9223fef0822ddL, 0x4b49e56affae3a89L,
        0xc970296e32e1d1c1L, 0xe2f9f88789f1b08fL, 0x2b0459d9b4c10c61L,
        0x377e97ea9197ee89L, 0xd2ccad460751e0e7L, 0xff162ca8d6da8c47L,
        0xf12e051405769857L, 0xdabba41293d5b035L, 0xacf326b0bb690d0eL,
        0x0617f431bc1a8e04L, 0x15b81f28d576e1b2L, 0x28c1fe59e4f8e5baL,
        0x694dd315c9354ca9L, 0xa97052a8f088ae6cL
    };

    @Test
    void shouldCompute32BitHashWithDefaultSeed() {
        for (int i = 0; i < TEST_INPUT_DATA.length; i++) {
            byte[] inputData = TEST_INPUT_DATA[i];
            int expectedHash = EXPECTED_32BIT_HASHES_DEFAULT_SEED[i];
            
            int actualHash = MurmurHash2.hash32(inputData, inputData.length);
            
            assertEquals(expectedHash, actualHash,
                String.format("32-bit hash mismatch for input %d (length=%d): expected=0x%08x, actual=0x%08x",
                    i, inputData.length, expectedHash, actualHash));
        }
    }

    @Test
    void shouldCompute32BitHashWithCustomSeed() {
        for (int i = 0; i < TEST_INPUT_DATA.length; i++) {
            byte[] inputData = TEST_INPUT_DATA[i];
            int expectedHash = EXPECTED_32BIT_HASHES_CUSTOM_SEED[i];
            
            int actualHash = MurmurHash2.hash32(inputData, inputData.length, CUSTOM_SEED_32_BIT);
            
            assertEquals(expectedHash, actualHash,
                String.format("32-bit hash with custom seed mismatch for input %d (length=%d): expected=0x%08x, actual=0x%08x",
                    i, inputData.length, expectedHash, actualHash));
        }
    }

    @Test
    void shouldCompute32BitHashForString() {
        int expectedHash = 0xb3bf597e;
        
        int actualHash = MurmurHash2.hash32(SAMPLE_TEXT);
        
        assertEquals(expectedHash, actualHash,
            String.format("32-bit string hash mismatch: expected=0x%08x, actual=0x%08x", expectedHash, actualHash));
    }

    @Test
    void shouldCompute32BitHashForSubstring() {
        int expectedHash = 0x4d666d90;
        int substringLength = SAMPLE_TEXT.length() - SUBSTRING_END_OFFSET;
        
        int actualHash = MurmurHash2.hash32(SAMPLE_TEXT, SUBSTRING_START_INDEX, substringLength);
        
        assertEquals(expectedHash, actualHash,
            String.format("32-bit substring hash mismatch (start=%d, length=%d): expected=0x%08x, actual=0x%08x",
                SUBSTRING_START_INDEX, substringLength, expectedHash, actualHash));
    }

    @Test
    void shouldCompute64BitHashWithDefaultSeed() {
        for (int i = 0; i < TEST_INPUT_DATA.length; i++) {
            byte[] inputData = TEST_INPUT_DATA[i];
            long expectedHash = EXPECTED_64BIT_HASHES_DEFAULT_SEED[i];
            
            long actualHash = MurmurHash2.hash64(inputData, inputData.length);
            
            assertEquals(expectedHash, actualHash,
                String.format("64-bit hash mismatch for input %d (length=%d): expected=0x%016x, actual=0x%016x",
                    i, inputData.length, expectedHash, actualHash));
        }
    }

    @Test
    void shouldCompute64BitHashWithCustomSeed() {
        for (int i = 0; i < TEST_INPUT_DATA.length; i++) {
            byte[] inputData = TEST_INPUT_DATA[i];
            long expectedHash = EXPECTED_64BIT_HASHES_CUSTOM_SEED[i];
            
            long actualHash = MurmurHash2.hash64(inputData, inputData.length, CUSTOM_SEED_64_BIT);
            
            assertEquals(expectedHash, actualHash,
                String.format("64-bit hash with custom seed mismatch for input %d (length=%d): expected=0x%016x, actual=0x%016x",
                    i, inputData.length, expectedHash, actualHash));
        }
    }

    @Test
    void shouldCompute64BitHashForString() {
        long expectedHash = 0x0920e0c1b7eeb261L;
        
        long actualHash = MurmurHash2.hash64(SAMPLE_TEXT);
        
        assertEquals(expectedHash, actualHash,
            String.format("64-bit string hash mismatch: expected=0x%016x, actual=0x%016x", expectedHash, actualHash));
    }

    @Test
    void shouldCompute64BitHashForSubstring() {
        long expectedHash = 0xa8b33145194985a2L;
        int substringLength = SAMPLE_TEXT.length() - SUBSTRING_END_OFFSET;
        
        long actualHash = MurmurHash2.hash64(SAMPLE_TEXT, SUBSTRING_START_INDEX, substringLength);
        
        assertEquals(expectedHash, actualHash,
            String.format("64-bit substring hash mismatch (start=%d, length=%d): expected=0x%016x, actual=0x%016x",
                SUBSTRING_START_INDEX, substringLength, expectedHash, actualHash));
    }
}
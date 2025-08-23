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
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class MurmurHash2Test {

    // Test input data: byte arrays of varying lengths
    private static final byte[][] TEST_INPUTS = {
        {(byte) 0xed, (byte) 0x53, (byte) 0xc4, (byte) 0xa5, (byte) 0x3b, (byte) 0x1b, (byte) 0xbd, (byte) 0xc2,
         (byte) 0x52, (byte) 0x7d, (byte) 0xc3, (byte) 0xef, (byte) 0x53, (byte) 0x5f, (byte) 0xae, (byte) 0x3b},
        {(byte) 0x21, (byte) 0x65, (byte) 0x59, (byte) 0x4e, (byte) 0xd8, (byte) 0x12, (byte) 0xf9, (byte) 0x05,
         (byte) 0x80, (byte) 0xe9, (byte) 0x1e, (byte) 0xed, (byte) 0xe4, (byte) 0x56, (byte) 0xbb},
        // Additional test inputs...
    };

    // Expected 32-bit hash results with default seed
    private static final int[] EXPECTED_HASH32_DEFAULT_SEED = {
        0x96814fb3, 0x485dcaba, // Additional expected results...
    };

    // Expected 32-bit hash results with custom seed
    private static final int[] EXPECTED_HASH32_CUSTOM_SEED = {
        0xd92e493e, 0x8b50903b, // Additional expected results...
    };

    // Expected 64-bit hash results with default seed
    private static final long[] EXPECTED_HASH64_DEFAULT_SEED = {
        0x4987cb15118a83d9L, 0x28e2a79e3f0394d9L, // Additional expected results...
    };

    // Expected 64-bit hash results with custom seed
    private static final long[] EXPECTED_HASH64_CUSTOM_SEED = {
        0x0822b1481a92e97bL, 0xf8a9223fef0822ddL, // Additional expected results...
    };

    // Test string for hash functions
    private static final String TEST_STRING = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";

    @Test
    void testHash32WithDefaultSeed() {
        for (int i = 0; i < TEST_INPUTS.length; i++) {
            int hash = MurmurHash2.hash32(TEST_INPUTS[i], TEST_INPUTS[i].length);
            assertEquals(EXPECTED_HASH32_DEFAULT_SEED[i], hash, 
                String.format("Unexpected 32-bit hash for input %d with default seed", i));
        }
    }

    @Test
    void testHash32WithCustomSeed() {
        final int customSeed = 0x71b4954d;
        for (int i = 0; i < TEST_INPUTS.length; i++) {
            int hash = MurmurHash2.hash32(TEST_INPUTS[i], TEST_INPUTS[i].length, customSeed);
            assertEquals(EXPECTED_HASH32_CUSTOM_SEED[i], hash, 
                String.format("Unexpected 32-bit hash for input %d with custom seed", i));
        }
    }

    @Test
    void testHash32FromString() {
        int hash = MurmurHash2.hash32(TEST_STRING);
        assertEquals(0xb3bf597e, hash, "Unexpected 32-bit hash for test string");
    }

    @Test
    void testHash32FromSubstring() {
        int hash = MurmurHash2.hash32(TEST_STRING, 2, TEST_STRING.length() - 4);
        assertEquals(0x4d666d90, hash, "Unexpected 32-bit hash for substring of test string");
    }

    @Test
    void testHash64WithDefaultSeed() {
        for (int i = 0; i < TEST_INPUTS.length; i++) {
            long hash = MurmurHash2.hash64(TEST_INPUTS[i], TEST_INPUTS[i].length);
            assertEquals(EXPECTED_HASH64_DEFAULT_SEED[i], hash, 
                String.format("Unexpected 64-bit hash for input %d with default seed", i));
        }
    }

    @Test
    void testHash64WithCustomSeed() {
        final int customSeed = 0x344d1f5c;
        for (int i = 0; i < TEST_INPUTS.length; i++) {
            long hash = MurmurHash2.hash64(TEST_INPUTS[i], TEST_INPUTS[i].length, customSeed);
            assertEquals(EXPECTED_HASH64_CUSTOM_SEED[i], hash, 
                String.format("Unexpected 64-bit hash for input %d with custom seed", i));
        }
    }

    @Test
    void testHash64FromString() {
        long hash = MurmurHash2.hash64(TEST_STRING);
        assertEquals(0x0920e0c1b7eeb261L, hash, "Unexpected 64-bit hash for test string");
    }

    @Test
    void testHash64FromSubstring() {
        long hash = MurmurHash2.hash64(TEST_STRING, 2, TEST_STRING.length() - 4);
        assertEquals(0xa8b33145194985a2L, hash, "Unexpected 64-bit hash for substring of test string");
    }
}
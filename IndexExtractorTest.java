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
package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link IndexExtractor}.
 */
class IndexExtractorTest {

    /**
     * A test implementation of BitMapExtractor that uses a long array as the source of bitmaps.
     */
    private static final class TestingBitMapExtractor implements BitMapExtractor {
        private final long[] bitMaps;

        TestingBitMapExtractor(final long[] bitMaps) {
            this.bitMaps = bitMaps;
        }

        @Override
        public boolean processBitMaps(final LongPredicate consumer) {
            for (final long bitMap : bitMaps) {
                if (!consumer.test(bitMap)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Tests that the default {@code asIndexArray()} implementation correctly captures all
     * indices passed to the consumer, including duplicates. The parameterized values
     * test behavior around potential collection resizing boundaries.
     *
     * @param numberOfDuplicates The number of times to report the same index.
     */
    @ParameterizedTest
    @ValueSource(ints = {32, 33})
    void asIndexArray_shouldReturnAllProcessedIndicesIncludingDuplicates(final int numberOfDuplicates) {
        // An extractor that reports the index '0' a specific number of times.
        final IndexExtractor extractorWithDuplicates = indexConsumer -> {
            for (int j = 0; j < numberOfDuplicates; j++) {
                // Stop if the consumer returns false, per the contract.
                if (!indexConsumer.test(0)) {
                    return false;
                }
            }
            return true;
        };

        final int[] result = extractorWithDuplicates.asIndexArray();

        // The resulting array should contain '0' repeated 'numberOfDuplicates' times.
        final int[] expected = new int[numberOfDuplicates]; // An array of zeros.
        assertArrayEquals(expected, result);
    }

    @Test
    void fromBitMapExtractor_shouldCorrectlyConvertSparseBitMapsToIndices() {
        // Each long in the array is a 64-bit map. The index of the long in the
        // array determines the offset (in multiples of 64) for the indices.
        // - 1L (binary ...001) at array index 0 -> on-bit at position 0 -> final index 0
        // - 2L (binary ...010) at array index 1 -> on-bit at position 1 -> final index 1 + 64*1 = 65
        // - 3L (binary ...011) at array index 2 -> on-bits at pos 0, 1 -> final indices 0 + 64*2 = 128, 1 + 64*2 = 129
        final long[] bitMaps = {1L, 2L, 3L};
        final BitMapExtractor bitMapExtractor = new TestingBitMapExtractor(bitMaps);

        final IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        final List<Integer> actualIndices = new ArrayList<>();
        indexExtractor.processIndices(actualIndices::add);

        final List<Integer> expectedIndices = Arrays.asList(0, 65, 128, 129);

        assertEquals(expectedIndices, actualIndices);
    }

    @Test
    void fromBitMapExtractor_shouldCorrectlyConvertDenseBitMapToIndices() {
        // A bitmap with all 64 bits set.
        final long[] bitMaps = {0xFFFFFFFFFFFFFFFFL};
        final BitMapExtractor bitMapExtractor = new TestingBitMapExtractor(bitMaps);

        final IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        final List<Integer> actualIndices = new ArrayList<>();
        indexExtractor.processIndices(actualIndices::add);

        // We expect all indices from 0 to 63.
        final List<Integer> expectedIndices = IntStream.range(0, 64)
            .boxed()
            .collect(Collectors.toList());

        assertEquals(expectedIndices, actualIndices);
    }
}
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IndexExtractorTest {

    private static final class TestingBitMapExtractor implements BitMapExtractor {
        long[] values;

        TestingBitMapExtractor(final long[] values) {
            this.values = values;
        }

        @Override
        public boolean processBitMaps(final LongPredicate consumer) {
            for (final long l : values) {
                if (!consumer.test(l)) {
                    return false;
                }
            }
            return true;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {32, 33})
    void asIndexArray_WhenAllIndicesAreZero_ReturnsArrayOfZeros(final int n) {
        // Create an IndexExtractor that returns index 0 exactly n times
        final IndexExtractor indexExtractor = consumer -> {
            for (int i = 0; i < n; i++) {
                consumer.test(0);
            }
            return true;
        };

        final int[] expected = new int[n]; // Array of n zeros
        assertArrayEquals(expected, indexExtractor.asIndexArray());
    }

    @Test
    void fromBitMapExtractor_WithMultipleLongs_ProducesCorrectIndices() {
        // BitMaps: [1L, 2L, 3L]
        // Expected indices:
        //  1L (binary: ...001) -> index 0
        //  2L (binary: ...010) -> index 1 + 64 = 65
        //  3L (binary: ...011) -> indices 0+128 and 1+128 (128, 129)
        final int[] expectedIndices = {0, 65, 128, 129};
        
        // Create extractor from bitmaps
        final BitMapExtractor bitMapExtractor = new TestingBitMapExtractor(new long[] {1L, 2L, 3L});
        final IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        
        // Collect indices
        final List<Integer> collectedIndices = new ArrayList<>();
        indexExtractor.processIndices(collectedIndices::add);
        
        // Verify collected indices
        assertEquals(expectedIndices.length, collectedIndices.size());
        for (int i = 0; i < expectedIndices.length; i++) {
            assertEquals(expectedIndices[i], collectedIndices.get(i));
        }
    }

    @Test
    void fromBitMapExtractor_WithFullLong_ProducesAllIndicesFrom0To63() {
        // A long with all 64 bits set should produce indices 0-63
        final BitMapExtractor bitMapExtractor = new TestingBitMapExtractor(new long[] {0xFFFFFFFFFFFFFFFFL});
        final IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        
        final List<Integer> collectedIndices = new ArrayList<>();
        indexExtractor.processIndices(collectedIndices::add);
        
        // Verify we have all 64 indices
        assertEquals(64, collectedIndices.size());
        
        // Verify each index from 0 to 63 exists
        for (int expectedIndex = 0; expectedIndex < 64; expectedIndex++) {
            assertEquals(expectedIndex, collectedIndices.get(expectedIndex));
        }
    }
}
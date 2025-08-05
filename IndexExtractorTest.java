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
 * distributed under the License is distributed on the "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongPredicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IndexExtractorTest {

    /**
     * Test implementation of BitMapExtractor that holds an array of long values
     * representing bitmap data for testing IndexExtractor.fromBitMapExtractor().
     */
    private static final class TestBitMapExtractor implements BitMapExtractor {
        private final long[] bitmapValues;

        TestBitMapExtractor(final long[] bitmapValues) {
            this.bitmapValues = bitmapValues;
        }

        @Override
        public boolean processBitMaps(final LongPredicate consumer) {
            for (final long bitmapValue : bitmapValues) {
                if (!consumer.test(bitmapValue)) {
                    return false;
                }
            }
            return true;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {32, 33})
    void testAsIndexArray_withDuplicateIndices_returnsArrayWithDuplicates(final int numberOfDuplicates) {
        // Create an IndexExtractor that produces the same index (0) multiple times
        final IndexExtractor extractorWithDuplicates = indexConsumer -> {
            for (int i = 0; i < numberOfDuplicates; i++) {
                indexConsumer.test(0); // Always use index 0
            }
            return true;
        };
        
        // Verify that asIndexArray() preserves duplicates
        int[] expectedArray = new int[numberOfDuplicates]; // All zeros by default
        Assertions.assertArrayEquals(expectedArray, extractorWithDuplicates.asIndexArray());
    }

    @Test
    void testFromBitMapExtractor_withSparseBitmaps_extractsCorrectIndices() {
        // Test with sparse bitmaps: 1L (bit 0), 2L (bit 1), 3L (bits 0 and 1)
        TestBitMapExtractor sparseBitmapExtractor = new TestBitMapExtractor(new long[] {1L, 2L, 3L});
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(sparseBitmapExtractor);
        
        List<Integer> extractedIndices = extractAllIndices(indexExtractor);
        
        // Expected indices calculation:
        // First bitmap (1L = 0x1): bit 0 set -> index 0 (0 + 0*64)
        // Second bitmap (2L = 0x2): bit 1 set -> index 65 (1 + 1*64) 
        // Third bitmap (3L = 0x3): bits 0,1 set -> indices 128,129 (0+2*64, 1+2*64)
        assertEquals(4, extractedIndices.size());
        assertEquals(Integer.valueOf(0), extractedIndices.get(0));    // First bitmap, bit 0
        assertEquals(Integer.valueOf(65), extractedIndices.get(1));   // Second bitmap, bit 1
        assertEquals(Integer.valueOf(128), extractedIndices.get(2));  // Third bitmap, bit 0
        assertEquals(Integer.valueOf(129), extractedIndices.get(3));  // Third bitmap, bit 1
    }

    @Test
    void testFromBitMapExtractor_withAllBitsSet_extractsAllIndices() {
        // Test with all 64 bits set in a single long value
        long allBitsSet = 0xFFFFFFFFFFFFFFFFL;
        TestBitMapExtractor fullBitmapExtractor = new TestBitMapExtractor(new long[] {allBitsSet});
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(fullBitmapExtractor);
        
        List<Integer> extractedIndices = extractAllIndices(indexExtractor);
        
        // Should extract indices 0 through 63 (all bits in the first 64-bit segment)
        assertEquals(64, extractedIndices.size());
        for (int expectedIndex = 0; expectedIndex < 64; expectedIndex++) {
            assertEquals(Integer.valueOf(expectedIndex), extractedIndices.get(expectedIndex));
        }
    }

    /**
     * Helper method to extract all indices from an IndexExtractor into a list.
     * 
     * @param extractor the IndexExtractor to process
     * @return List containing all extracted indices in order
     */
    private List<Integer> extractAllIndices(IndexExtractor extractor) {
        List<Integer> indices = new ArrayList<>();
        extractor.processIndices(indices::add);
        return indices;
    }
}
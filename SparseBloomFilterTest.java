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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 */
class SparseBloomFilterTest extends AbstractBloomFilterTest<SparseBloomFilter> {
    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    @Test
    void testProcessBitMapsExitsEarlyBeforeBitmapBoundary() {
        // Create filter with multiple indices across bitmaps
        final int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};
        final BloomFilter bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        
        // Track predicate invocations
        final int[] predicateCalls = {0};
        final boolean shouldContinue = bf.processBitMaps(l -> {
            predicateCalls[0]++;
            // Return false immediately to test early exit
            return false;
        });
        
        // Verify exit after first bitmap
        assertFalse(shouldContinue);
        assertEquals(1, predicateCalls[0]);
    }

    @Test
    void testProcessBitMapsExitsEarlyAtBitmapBoundary() {
        // Create filter with indices spanning multiple bitmaps
        final int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};
        final BloomFilter bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        
        // Track predicate invocations
        final int[] predicateCalls = {0};
        final boolean shouldContinue = bf.processBitMaps(l -> {
            predicateCalls[0]++;
            // Only allow processing of first bitmap
            return predicateCalls[0] < 1;
        });
        
        // Verify exit after first bitmap
        assertFalse(shouldContinue);
        assertEquals(1, predicateCalls[0]);
    }

    @Test
    void testProcessBitMapsProcessesExtraBlockWhenAllValuesInFirstBitmap() {
        // Create filter with indices only in first bitmap
        final int[] values = {1, 2, 3, 4};
        final BloomFilter bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        
        // Track predicate invocations
        final int[] predicateCalls = {0};
        final boolean shouldContinue = bf.processBitMaps(l -> {
            predicateCalls[0]++;
            // Process all bitmaps
            return true;
        });
        
        // Verify both bitmaps processed
        assertTrue(shouldContinue);
        assertEquals(2, predicateCalls[0]);
    }

    @Test
    void testProcessBitMapsExitsEarlyWhenAllValuesInFirstBitmap() {
        // Create filter with indices only in first bitmap
        final int[] values = {1, 2, 3, 4};
        final BloomFilter bf = createFilter(getTestShape(), IndexExtractor.fromIndexArray(values));
        
        // Track predicate invocations
        final int[] predicateCalls = {0};
        final boolean shouldContinue = bf.processBitMaps(l -> {
            predicateCalls[0]++;
            // Break after processing first bitmap
            return predicateCalls[0] < 1;
        });
        
        // Verify exit after first bitmap
        assertFalse(shouldContinue);
        assertEquals(1, predicateCalls[0]);
    }

    @Test
    void testMergeWithSimpleBloomFilterProducesEqualBitMaps() {
        // Create empty filter and populated SimpleBloomFilter
        final BloomFilter sparse = createEmptyFilter(getTestShape());
        final BloomFilter simple = new SimpleBloomFilter(getTestShape());
        simple.merge(TestingHashers.FROM1);
        
        // Merge data into sparse filter
        sparse.merge(simple);
        
        // Verify both filters have identical bitmap representations
        assertTrue(simple.processBitMapPairs(sparse, (x, y) -> x == y));
    }
}
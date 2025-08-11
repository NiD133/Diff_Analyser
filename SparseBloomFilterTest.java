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

    /**
     * Tests edge cases for bitmap processing in SparseBloomFilter.
     * Verifies early exit behavior and bitmap boundary handling.
     */
    @Test
    void testBitMapProcessingEdgeCases() {
        testEarlyExitBeforeBitmapBoundary();
        testEarlyExitAtBitmapBoundary();
        testProcessingWhenAllValuesInFirstBitmap();
        testEarlyExitWhenAllValuesInFirstBitmapAndPredicateReturnsFalse();
    }

    /**
     * Tests that bitmap processing exits early when predicate returns false
     * before reaching a bitmap boundary (64-bit boundary).
     */
    private void testEarlyExitBeforeBitmapBoundary() {
        // Indices span multiple bitmaps: first bitmap (1-8), second bitmap (65-71)
        int[] indicesSpanningMultipleBitmaps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};
        BloomFilter bloomFilter = createFilter(getTestShape(), 
            IndexExtractor.fromIndexArray(indicesSpanningMultipleBitmaps));

        ProcessingCounter counter = new ProcessingCounter();
        
        // Predicate always returns false, should exit after first bitmap
        boolean processingCompleted = bloomFilter.processBitMaps(bitmap -> {
            counter.increment();
            return false; // Force early exit
        });

        assertFalse(processingCompleted, "Processing should exit early when predicate returns false");
        assertEquals(1, counter.getCount(), "Should process exactly one bitmap before exiting");
    }

    /**
     * Tests that bitmap processing exits early exactly at a bitmap boundary.
     */
    private void testEarlyExitAtBitmapBoundary() {
        int[] indicesSpanningMultipleBitmaps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};
        BloomFilter bloomFilter = createFilter(getTestShape(), 
            IndexExtractor.fromIndexArray(indicesSpanningMultipleBitmaps));

        ProcessingCounter counter = new ProcessingCounter();
        
        // Predicate returns true for first bitmap, false for subsequent ones
        boolean processingCompleted = bloomFilter.processBitMaps(bitmap -> {
            boolean shouldContinue = counter.getCount() == 0;
            if (shouldContinue) {
                counter.increment();
            }
            return shouldContinue;
        });

        assertFalse(processingCompleted, "Processing should exit early at bitmap boundary");
        assertEquals(1, counter.getCount(), "Should process exactly one bitmap at boundary");
    }

    /**
     * Tests bitmap processing when all indices fit within the first bitmap.
     * Should still process a second (empty) bitmap to maintain consistency.
     */
    private void testProcessingWhenAllValuesInFirstBitmap() {
        int[] indicesInFirstBitmapOnly = {1, 2, 3, 4}; // All within first 64 bits
        BloomFilter bloomFilter = createFilter(getTestShape(), 
            IndexExtractor.fromIndexArray(indicesInFirstBitmapOnly));

        ProcessingCounter counter = new ProcessingCounter();
        
        // Predicate always returns true
        boolean processingCompleted = bloomFilter.processBitMaps(bitmap -> {
            counter.increment();
            return true; // Continue processing
        });

        assertTrue(processingCompleted, "Processing should complete successfully");
        assertEquals(2, counter.getCount(), "Should process two bitmaps (one with data, one empty)");
    }

    /**
     * Tests early exit when all values are in first bitmap but predicate returns false
     * on the second (empty) bitmap.
     */
    private void testEarlyExitWhenAllValuesInFirstBitmapAndPredicateReturnsFalse() {
        int[] indicesInFirstBitmapOnly = {1, 2, 3, 4}; // All within first 64 bits
        BloomFilter bloomFilter = createFilter(getTestShape(), 
            IndexExtractor.fromIndexArray(indicesInFirstBitmapOnly));

        ProcessingCounter counter = new ProcessingCounter();
        
        // Predicate returns true for first bitmap, false for second
        boolean processingCompleted = bloomFilter.processBitMaps(bitmap -> {
            boolean shouldContinue = counter.getCount() == 0;
            if (shouldContinue) {
                counter.increment();
            }
            return shouldContinue;
        });

        assertFalse(processingCompleted, "Processing should exit early on second bitmap");
        assertEquals(1, counter.getCount(), "Should process exactly one bitmap before exiting");
    }

    /**
     * Tests merging between SparseBloomFilter and SimpleBloomFilter to ensure
     * compatibility across different BloomFilter implementations.
     */
    @Test
    void testMergingBetweenDifferentBloomFilterImplementations() {
        // Create empty SparseBloomFilter
        BloomFilter sparseFilter = createEmptyFilter(getTestShape());
        
        // Create SimpleBloomFilter with some data
        BloomFilter simpleFilter = new SimpleBloomFilter(getTestShape());
        simpleFilter.merge(TestingHashers.FROM1);
        
        // Merge SimpleBloomFilter into SparseBloomFilter
        sparseFilter.merge(simpleFilter);
        
        // Verify that both filters now contain the same data
        boolean filtersAreEqual = simpleFilter.processBitMapPairs(sparseFilter, 
            (simpleBitmap, sparseBitmap) -> simpleBitmap == sparseBitmap);
        
        assertTrue(filtersAreEqual, "Merged filters should contain identical bitmap data");
    }

    /**
     * Helper class to track how many times a predicate has been called.
     */
    private static class ProcessingCounter {
        private int count = 0;
        
        void increment() {
            count++;
        }
        
        int getCount() {
            return count;
        }
    }
}
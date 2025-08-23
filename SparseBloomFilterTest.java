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
 * Unit tests for the {@link SparseBloomFilter}.
 */
class SparseBloomFilterTest extends AbstractBloomFilterTest<SparseBloomFilter> {

    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    /**
     * Tests the behavior of the processBitMaps method with edge cases.
     */
    @Test
    void testBitMapExtractorEdgeCases() {
        // Test case: Verify early exit before bitmap boundary
        int[] indicesBeforeBoundary = {1, 2, 3, 4, 5, 6, 7, 8, 9, 65, 66, 67, 68, 69, 70, 71};
        BloomFilter bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indicesBeforeBoundary));

        final int[] earlyExitCounter = new int[1];
        assertFalse(bloomFilter.processBitMaps(bitmap -> {
            earlyExitCounter[0]++;
            return false;
        }));
        assertEquals(1, earlyExitCounter[0]);

        // Test case: Verify early exit at bitmap boundary
        bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indicesBeforeBoundary));
        earlyExitCounter[0] = 0;
        assertFalse(bloomFilter.processBitMaps(bitmap -> {
            boolean shouldContinue = earlyExitCounter[0] == 0;
            if (shouldContinue) {
                earlyExitCounter[0]++;
            }
            return shouldContinue;
        }));
        assertEquals(1, earlyExitCounter[0]);

        // Test case: Verify additional processing if all values are in the first bitmap
        int[] indicesInFirstBitmap = {1, 2, 3, 4};
        bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indicesInFirstBitmap));
        final int[] processingCounter = new int[1];
        assertTrue(bloomFilter.processBitMaps(bitmap -> {
            processingCounter[0]++;
            return true;
        }));
        assertEquals(2, processingCounter[0]);

        // Test case: Verify early exit if predicate returns false on the second block
        bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indicesInFirstBitmap));
        processingCounter[0] = 0;
        assertFalse(bloomFilter.processBitMaps(bitmap -> {
            boolean shouldContinue = processingCounter[0] == 0;
            if (shouldContinue) {
                processingCounter[0]++;
            }
            return shouldContinue;
        }));
        assertEquals(1, processingCounter[0]);
    }

    /**
     * Tests the merge functionality of Bloom filters with edge cases.
     */
    @Test
    void testBloomFilterBasedMergeEdgeCases() {
        // Create two Bloom filters
        final BloomFilter emptyFilter = createEmptyFilter(getTestShape());
        final BloomFilter simpleFilter = new SimpleBloomFilter(getTestShape());
        
        // Merge a hasher into the simple filter
        simpleFilter.merge(TestingHashers.FROM1);
        
        // Merge the simple filter into the empty filter
        emptyFilter.merge(simpleFilter);
        
        // Verify that the merged filters have identical bitmaps
        assertTrue(simpleFilter.processBitMapPairs(emptyFilter, (bitmap1, bitmap2) -> bitmap1 == bitmap2));
    }
}
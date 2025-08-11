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

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Nested;
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
     * Tests the edge cases of the {@code processBitMaps} method.
     */
    @Nested
    class ProcessBitMapsTest {

        /**
         * Tests that processing stops immediately if the predicate returns false on the first bitmap.
         */
        @Test
        void shouldShortCircuitOnFirstCallIfPredicateIsFalse() {
            // Arrange
            // Create a filter with indices that span multiple 64-bit bitmap blocks.
            final int[] indices = {1, 2, Long.SIZE + 1, Long.SIZE + 2};
            final BloomFilter filter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
            final AtomicInteger callCounter = new AtomicInteger(0);

            // Act
            final boolean result = filter.processBitMaps(bitmap -> {
                callCounter.incrementAndGet();
                return false; // Immediately request to stop processing
            });

            // Assert
            assertFalse(result, "Processing should return false when short-circuited");
            assertEquals(1, callCounter.get(), "Predicate should have been called only once");
        }

        /**
         * Tests that processing stops if the predicate returns false on a subsequent bitmap.
         */
        @Test
        void shouldShortCircuitOnSecondCallIfPredicateIsFalse() {
            // Arrange
            // Create a filter with indices that span multiple 64-bit bitmap blocks.
            final int[] indices = {1, 2, Long.SIZE + 1, Long.SIZE + 2};
            final BloomFilter filter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
            final AtomicInteger callCounter = new AtomicInteger(0);

            // Act
            final boolean result = filter.processBitMaps(bitmap -> {
                // Return true for the first call, false for the second.
                return callCounter.incrementAndGet() == 1;
            });

            // Assert
            assertFalse(result, "Processing should return false when short-circuited");
            assertEquals(2, callCounter.get(), "Predicate should have been called twice");
        }

        /**
         * The SparseBloomFilter implementation of processBitMaps has a specific behavior:
         * it processes one final empty bitmap after the last non-empty one if the predicate
         * continues to return true. This test verifies that behavior.
         */
        @Test
        void shouldProcessOneExtraEmptyBitmapWhenPredicateIsTrue() {
            // Arrange
            // Create a filter where all indices fall into the first bitmap block.
            final int[] indices = {1, 2, 3, 4};
            final BloomFilter filter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
            final AtomicInteger callCounter = new AtomicInteger(0);

            // Act
            final boolean result = filter.processBitMaps(bitmap -> {
                callCounter.incrementAndGet();
                return true; // Always continue processing
            });

            // Assert
            assertTrue(result, "Processing should return true when fully completed");
            // Expect 2 calls: one for the block with data, and one extra empty block.
            assertEquals(2, callCounter.get());
        }

        /**
         * This test verifies that short-circuiting works correctly on the "extra" empty
         * bitmap that SparseBloomFilter processes.
         */
        @Test
        void shouldShortCircuitOnExtraEmptyBitmap() {
            // Arrange
            // Create a filter where all indices fall into the first bitmap block.
            final int[] indices = {1, 2, 3, 4};
            final BloomFilter filter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
            final AtomicInteger callCounter = new AtomicInteger(0);

            // Act
            final boolean result = filter.processBitMaps(bitmap -> {
                // Return true for the first call (the non-empty block), false for the second.
                return callCounter.incrementAndGet() == 1;
            });

            // Assert
            assertFalse(result, "Processing should return false when short-circuited");
            assertEquals(2, callCounter.get(), "Predicate should have been called twice");
        }
    }

    /**
     * Tests that a SparseBloomFilter can be correctly merged from a different
     * BloomFilter implementation (SimpleBloomFilter), resulting in an identical filter.
     */
    @Test
    void testMergeFromSimpleBloomFilter() {
        // Arrange
        final SparseBloomFilter destinationFilter = createEmptyFilter(getTestShape());
        final BloomFilter sourceFilter = new SimpleBloomFilter(getTestShape());
        sourceFilter.merge(TestingHashers.FROM1);

        // Act
        destinationFilter.merge(sourceFilter);

        // Assert
        // Verify that the destination filter is now identical to the source.
        final boolean areIdentical = sourceFilter.processBitMapPairs(destinationFilter, (x, y) -> x == y);
        assertTrue(areIdentical, "Filters should be identical after merge");
    }
}
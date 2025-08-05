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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link SimpleBloomFilter}.
 */
class SimpleBloomFilterTest extends AbstractBloomFilterTest<SimpleBloomFilter> {

    @Override
    protected SimpleBloomFilter createEmptyFilter(final Shape shape) {
        return new SimpleBloomFilter(shape);
    }

    /**
     * Tests that merging a BitMapExtractor that provides fewer bitmaps (longs)
     * than the filter's shape requires still works correctly. The filter should
     * merge the bitmaps that are provided.
     */
    @Test
    void testMergeWithBitMapExtractorShorterThanShape() {
        // Arrange
        // The test shape from the abstract class is configured to require 2 longs for its bitmap.
        final SimpleBloomFilter filter = createEmptyFilter(getTestShape());

        // Create a BitMapExtractor that provides only one long, which is fewer than the shape requires.
        // The value 2L (binary ...0010) has a single bit set, resulting in a cardinality of 1.
        final long bitmapWithOneBit = 2L;
        final BitMapExtractor extractorWithFewerBitMaps = consumer -> consumer.test(bitmapWithOneBit);

        // Act
        final boolean hasChanged = filter.merge(extractorWithFewerBitMaps);

        // Assert
        assertTrue(hasChanged, "merge() should return true as the filter was modified.");

        final int expectedCardinality = 1;
        assertEquals(expectedCardinality, filter.cardinality(),
                "Cardinality should reflect the single bit set in the provided bitmap.");
    }
}
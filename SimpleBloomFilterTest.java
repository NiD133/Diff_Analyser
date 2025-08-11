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
     * Tests that merging a BitMapExtractor with fewer values than expected by the shape
     * still works correctly. This verifies the filter's robustness when handling
     * incomplete bitmap data.
     */
    @Test
    void testMergeWithIncompleteBitMapExtractor() {
        // Given: An empty bloom filter with a shape that expects 2 longs
        final SimpleBloomFilter bloomFilter = createEmptyFilter(getTestShape());
        
        // And: A BitMapExtractor that provides only 1 long value (2L) instead of the expected 2
        final BitMapExtractor incompleteBitMapExtractor = predicate -> predicate.test(2L);
        
        // When: We merge the incomplete BitMapExtractor into the bloom filter
        final boolean mergeSuccessful = bloomFilter.merge(incompleteBitMapExtractor);
        
        // Then: The merge operation should succeed
        assertTrue(mergeSuccessful, "Merge operation should succeed even with incomplete bitmap data");
        
        // And: The bloom filter should have exactly 1 bit set (cardinality = 1)
        final int expectedCardinality = 1;
        assertEquals(expectedCardinality, bloomFilter.cardinality(), 
            "Bloom filter should have cardinality of 1 after merging single bit value");
    }
}
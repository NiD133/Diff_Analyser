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
 * Unit tests for the {@link SimpleBloomFilter} class.
 */
class SimpleBloomFilterTest extends AbstractBloomFilterTest<SimpleBloomFilter> {

    /**
     * Creates an empty {@link SimpleBloomFilter} with the specified shape.
     *
     * @param shape The shape of the Bloom filter.
     * @return A new instance of {@link SimpleBloomFilter}.
     */
    @Override
    protected SimpleBloomFilter createEmptyFilter(final Shape shape) {
        return new SimpleBloomFilter(shape);
    }

    /**
     * Tests the merge operation with a {@link BitMapExtractor} that provides fewer
     * values than expected by the filter's shape.
     */
    @Test
    void testMergeWithShortBitMapExtractor() {
        // Create an empty filter with the test shape
        final SimpleBloomFilter filter = createEmptyFilter(getTestShape());

        // Create a BitMapExtractor that returns only one long value,
        // while the shape expects two long values.
        final BitMapExtractor shortBitMapExtractor = bitMap -> bitMap.test(2L);

        // Perform the merge operation and verify the result
        boolean mergeResult = filter.merge(shortBitMapExtractor);
        assertTrue(mergeResult, "Merge operation should return true");

        // Verify that the cardinality of the filter is as expected
        int expectedCardinality = 1;
        assertEquals(expectedCardinality, filter.cardinality(), "Cardinality should be 1 after merge");
    }
}
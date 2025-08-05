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

    @Test
    void testMergeWhenBitMapExtractorHasFewerLongsThanShape() {
        // Given: 
        // - A filter with shape requiring 2 longs
        // - An extractor providing only 1 long (value 2L = binary '10')
        final SimpleBloomFilter filter = createEmptyFilter(getTestShape());
        final BitMapExtractor extractorWithOneLong = consumer -> consumer.test(2L);

        // When: Merging the extractor into the filter
        final boolean changed = filter.merge(extractorWithOneLong);

        // Then:
        // - Merge should report the filter was modified
        // - Exactly one bit should be set in the filter
        assertTrue(changed, "Merge should return true indicating filter changed");
        assertEquals(1, filter.cardinality(), 
            "Cardinality should reflect single bit set from extractor's 2L value");
    }
}
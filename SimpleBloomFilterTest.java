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
     * Verifies that merging from a BitMapExtractor that emits fewer longs than the Shape requires
     * still succeeds and only affects the bits represented by the provided longs.
     *
     * The test shape used by the base class expects multiple longs (e.g. 2). Here we provide just one.
     */
    @Test
    void mergeHandlesExtractorWithFewerBitMapsThanShape() {
        // Given: an empty filter
        final SimpleBloomFilter sut = createEmptyFilter(getTestShape());

        // And: a BitMapExtractor that emits only a single long with a single bit set
        // (cardinality contribution should be exactly 1)
        final long providedBitmap = 1L; // 0b...0001
        final BitMapExtractor singleLongExtractor = consumer -> consumer.test(providedBitmap);

        // When: merging the short extractor
        final boolean changed = sut.merge(singleLongExtractor);

        // Then: the merge reports a change and only one bit is set
        assertTrue(changed, "merge should return true when any bit is set");
        assertEquals(1, sut.cardinality(), "Only the single provided bit should be set");

        // And: the first long in the internal bit map reflects the provided value
        assertEquals(providedBitmap, sut.asBitMapArray()[0],
                "The first bitmap long should equal the value provided by the extractor");
    }
}
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

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SparseBloomFilter_ESTest extends SparseBloomFilter_ESTest_scaffolding {

    // ===================== Constructor Tests =====================

    @Test(timeout = 4000)
    public void testConstructorWithNullShapeThrowsNPE() {
        try {
            new SparseBloomFilter(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("shape", e.getMessage());
        }
    }

    // ===================== Characteristics Tests =====================

    @Test(timeout = 4000)
    public void testCharacteristics() {
        Shape shape = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertEquals(1, filter.characteristics());
    }

    // ===================== State Tests =====================

    @Test(timeout = 4000)
    public void testIsEmptyWhenEmpty() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIsEmptyAfterMerge() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] bytes = new byte[6];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(bytes);
        filter.merge(hasher);
        assertFalse(filter.isEmpty());
    }

    @Test(timeout = 4000)
    public void testClear() {
        Shape shape = Shape.fromKM(1045, 1045);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        filter.clear();
        assertEquals(1, filter.characteristics());
    }

    // ===================== Cardinality Tests =====================

    @Test(timeout = 4000)
    public void testCardinalityWhenEmpty() {
        Shape shape = Shape.fromKM(3281, 3281);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertEquals(0, filter.cardinality());
    }

    @Test(timeout = 4000)
    public void testCardinalityAfterMerge() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] bytes = new byte[6];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(bytes);
        filter.merge(hasher);
        assertEquals(1, filter.cardinality());
    }

    // ===================== Copy Tests =====================

    @Test(timeout = 4000)
    public void testCopy() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter original = new SparseBloomFilter(shape);
        SparseBloomFilter copy = original.copy();
        assertEquals(1, copy.characteristics());
    }

    // ===================== Shape Tests =====================

    @Test(timeout = 4000)
    public void testGetShape() {
        Shape shape = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        Shape resultShape = filter.getShape();
        assertEquals(0.6931471805599453, resultShape.estimateMaxN(), 0.01);
    }

    // ===================== Merge Tests =====================

    @Test(timeout = 4000)
    public void testMergeHasherSuccessfully() {
        Shape shape = Shape.fromNM(22, 22);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-394L, -735L);
        assertTrue(filter.merge(hasher));
    }

    @Test(timeout = 4000)
    public void testMergeIndexExtractorSuccessfully() {
        int[] indices = new int[2];
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        Shape shape = Shape.fromNM(989, 2268);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.merge(extractor));
    }

    @Test(timeout = 4000)
    public void testMergeItself() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.merge(filter));
    }

    @Test(timeout = 4000)
    public void testMergeBloomFilterSuccessfully() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.merge(filter));
        assertEquals(1, filter.characteristics());
    }

    @Test(timeout = 4000)
    public void testMergeItselfAsBitMapExtractor() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] bytes = new byte[4];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(bytes);
        filter.merge(hasher);
        assertTrue(filter.merge(filter));
    }

    // ===================== Merge Exception Tests =====================

    @Test(timeout = 4000)
    public void testMergeNullHasherThrowsNPE() {
        Shape shape = Shape.fromNM(10, 10);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.merge((Hasher) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("hasher", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeNullBloomFilterThrowsNPE() {
        Shape shape = Shape.fromKM(18, 18);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.merge((BloomFilter<?>) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("other", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeNullBitMapExtractorThrowsNPE() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.merge((BitMapExtractor) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeNullIndexExtractorThrowsNPE() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.merge((IndexExtractor) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("indexExtractor", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithNegativeIndexThrowsException() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = new int[4];
        indices[3] = -1;
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        try {
            filter.merge(extractor);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value in list -1 is less than 0", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithIndexTooLargeThrowsException() {
        Shape shape = Shape.fromNMK(12, 12, 12);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = {12};
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        try {
            filter.merge(extractor);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value in list 12 is greater than maximum value (11)", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMergeWithDifferentShapeThrowsException() {
        Shape shape1 = Shape.fromNM(1, 408);
        Shape shape2 = Shape.fromKM(994, 994);
        SparseBloomFilter filter1 = new SparseBloomFilter(shape1);
        SparseBloomFilter filter2 = new SparseBloomFilter(shape2);
        
        byte[] bytes = new byte[4];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(bytes);
        filter2.merge(hasher);
        
        try {
            filter1.merge(filter2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value in list 990 is greater than maximum value (407)", e.getMessage());
        }
    }

    // ===================== Contains Tests =====================

    @Test(timeout = 4000)
    public void testContainsItselfWhenEmpty() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.contains(filter));
    }

    @Test(timeout = 4000)
    public void testContainsItself() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue(filter.contains(filter));
    }

    @Test(timeout = 4000)
    public void testContainsEmptyIndexExtractor() {
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        int[] indices = new int[4];
        IndexExtractor extractor = IndexExtractor.fromIndexArray(indices);
        assertFalse(filter.contains(extractor));
    }

    @Test(timeout = 4000)
    public void testContainsAfterMerge() {
        Shape shape = Shape.fromKM(1045, 1045);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(997L, -1591L);
        SparseBloomFilter filter1 = new SparseBloomFilter(shape);
        filter1.merge(hasher);
        
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape);
        assertFalse(filter2.contains(filter1));
    }

    // ===================== Contains Exception Tests =====================

    @Test(timeout = 4000)
    public void testContainsNullBitMapExtractorThrowsNPE() {
        Shape shape = Shape.fromKM(989, 989);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.contains((BitMapExtractor) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testContainsNullIndexExtractorThrowsNPE() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.contains((IndexExtractor) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    // ===================== Processing Tests =====================

    @Test(timeout = 4000)
    public void testProcessIndicesWithBitMapTracker() {
        Shape shape = Shape.fromKM(2268, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        IndexFilter.BitMapTracker tracker = new IndexFilter.BitMapTracker(shape);
        assertTrue(filter.processIndices(tracker));
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsAfterMerge() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        byte[] bytes = new byte[4];
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(bytes);
        filter.merge(hasher);
        
        long[] bitMaps = new long[4];
        LongBiPredicate predicate = mock(LongBiPredicate.class);
        doReturn(false).when(predicate).test(anyLong(), anyLong());
        CountingLongPredicate countingPredicate = new CountingLongPredicate(bitMaps, predicate);
        LongPredicate negatedPredicate = countingPredicate.negate();
        assertTrue(filter.processBitMaps(negatedPredicate));
    }

    // ===================== Processing Exception Tests =====================

    @Test(timeout = 4000)
    public void testProcessIndicesWithNullThrowsNPE() {
        Shape shape = Shape.fromKM(994, 994);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.processIndices((IntPredicate) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("consumer", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testProcessBitMapsWithNullThrowsNPE() {
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        try {
            filter.processBitMaps((LongPredicate) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("consumer", e.getMessage());
        }
    }

    // ===================== Edge Case Tests =====================

    @Test(timeout = 4000)
    public void testAsBitMapArrayAfterMerge() {
        Shape shape = Shape.fromNM(1618, 1618);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(1618, 1618);
        filter.merge(hasher);
        long[] bitMapArray = filter.asBitMapArray();
        assertEquals(26, bitMapArray.length);
    }

    @Test(timeout = 4000)
    public void testGetMaxInsertAfterMerge() {
        Shape shape = Shape.fromNM(9, 9);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(0L, 9);
        filter.merge(hasher);
        
        ArrayCountingBloomFilter countingFilter = new ArrayCountingBloomFilter(shape);
        int maxInsert = countingFilter.getMaxInsert(filter);
        assertEquals(0, maxInsert);
    }
}
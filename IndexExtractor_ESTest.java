package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.collections4.bloomfilter.ArrayCountingBloomFilter;
import org.apache.commons.collections4.bloomfilter.BitMapExtractor;
import org.apache.commons.collections4.bloomfilter.EnhancedDoubleHasher;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;
import org.apache.commons.collections4.bloomfilter.SparseBloomFilter;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class IndexExtractor_ESTest extends IndexExtractor_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int LARGE_NUMBER = 2147352576;
    private static final int NEGATIVE_ONE = -1;

    @Test(timeout = TIMEOUT)
    public void testEmptySimpleBloomFilterReturnsEmptyIndexArray() {
        Shape shape = Shape.fromKM(1856, 1856);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        int[] indexArray = simpleBloomFilter.asIndexArray();
        assertEquals(0, indexArray.length);
    }

    @Test(timeout = TIMEOUT)
    public void testEnhancedDoubleHasherWithNegativeValues() {
        EnhancedDoubleHasher hasher = new EnhancedDoubleHasher(-2285L, -2285L);
        Shape shape = Shape.fromNM(6, LARGE_NUMBER);
        IndexExtractor indexExtractor = hasher.indices(shape);
        // Expect an exception when calling uniqueIndices on invalid data
        indexExtractor.uniqueIndices();
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromNullIndexArrayThrowsNullPointerException() {
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray((int[]) null);
        try {
            indexExtractor.uniqueIndices();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.IndexExtractor$2", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromNegativeIndexThrowsIndexOutOfBoundsException() {
        int[] intArray = {NEGATIVE_ONE};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        try {
            indexExtractor.uniqueIndices();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.BitSet", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromNullBitMapExtractorThrowsNullPointerException() {
        try {
            IndexExtractor.fromBitMapExtractor((BitMapExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testLargeShapeSimpleBloomFilterThrowsException() {
        Shape shape = Shape.fromNM(LARGE_NUMBER, LARGE_NUMBER);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        // Expect an exception when calling asIndexArray on invalid data
        simpleBloomFilter.asIndexArray();
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromNullIndexArrayAsIndexArrayThrowsNullPointerException() {
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray((int[]) null);
        try {
            indexExtractor.asIndexArray();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bloomfilter.IndexExtractor$2", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorWithMaxIntValueThrowsIndexOutOfBoundsException() {
        int[] intArray = {Integer.MAX_VALUE};
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        IndexExtractor uniqueIndexExtractor = indexExtractor.uniqueIndices();
        try {
            uniqueIndexExtractor.asIndexArray();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.BitSet", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testSimpleBloomFilterDoesNotContainUniqueIndices() {
        int[] intArray = new int[4];
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        IndexExtractor uniqueIndexExtractor = indexExtractor.uniqueIndices();
        Shape shape = Shape.fromKM(1867, 31);
        SimpleBloomFilter simpleBloomFilter = new SimpleBloomFilter(shape);
        boolean contains = simpleBloomFilter.contains(uniqueIndexExtractor);
        assertFalse(contains);
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromBitMapArrayReturnsCorrectLength() {
        long[] longArray = new long[3];
        longArray[2] = -1L;
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(longArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        int[] indexArray = indexExtractor.asIndexArray();
        assertEquals(64, indexArray.length);
    }

    @Test(timeout = TIMEOUT)
    public void testArrayCountingBloomFilterDoesNotContainIndices() {
        int[] intArray = new int[1];
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        Shape shape = Shape.fromNM(1624, 1624);
        ArrayCountingBloomFilter arrayCountingBloomFilter = new ArrayCountingBloomFilter(shape);
        boolean contains = arrayCountingBloomFilter.contains(indexExtractor);
        assertFalse(contains);
    }

    @Test(timeout = TIMEOUT)
    public void testIndexExtractorFromNegativeBitMapArrayReturnsCorrectLength() {
        long[] longArray = new long[1];
        longArray[0] = -2401L;
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(longArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        int[] indexArray = indexExtractor.asIndexArray();
        assertEquals(60, indexArray.length);
    }

    @Test(timeout = TIMEOUT)
    public void testSparseBloomFilterDoesNotContainIndices() {
        long[] longArray = new long[18];
        longArray[2] = -1L;
        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(longArray);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);
        Shape shape = Shape.fromNM(2147479552, 2147479552);
        SparseBloomFilter sparseBloomFilter = new SparseBloomFilter(shape);
        boolean contains = sparseBloomFilter.contains(indexExtractor);
        assertFalse(contains);
    }

    @Test(timeout = TIMEOUT)
    public void testUniqueIndicesReturnsSingleZeroIndex() {
        int[] intArray = new int[4];
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        IndexExtractor uniqueIndexExtractor = indexExtractor.uniqueIndices();
        int[] uniqueIndexArray = uniqueIndexExtractor.asIndexArray();
        assertEquals(1, uniqueIndexArray.length);
        assertArrayEquals(new int[]{0}, uniqueIndexArray);
    }

    @Test(timeout = TIMEOUT)
    public void testUniqueIndicesReturnsSameInstance() {
        int[] intArray = new int[4];
        IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(intArray);
        IndexExtractor uniqueIndexExtractor = indexExtractor.uniqueIndices();
        IndexExtractor secondUniqueIndexExtractor = uniqueIndexExtractor.uniqueIndices();
        assertSame(uniqueIndexExtractor, secondUniqueIndexExtractor);
    }
}
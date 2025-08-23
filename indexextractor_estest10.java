package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Tests for {@link IndexExtractor}.
 */
public class IndexExtractorTest {

    /**
     * Tests that {@code IndexExtractor.asIndexArray()} correctly extracts all 64 bit indices
     * from a single {@code long} in a bitmap where all bits are enabled.
     *
     * <p>This test also verifies that the indices are correctly offset based on the long's
     * position in the source bitmap array.</p>
     */
    @Test
    public void asIndexArrayFromBitMapExtractorReturnsCorrectIndicesForOffsetFullLong() {
        // Arrange
        // Create a bitmap represented by a long array. The first two longs are empty (0),
        // and the third long has all 64 bits set to 1.
        // -1L is the long representation for a 64-bit value with all bits set to 1.
        long[] bitMap = new long[3];
        bitMap[2] = -1L; // Represents bits from index 128 to 191.

        BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(bitMap);
        IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);

        // The expected indices are 128, 129, ..., 191, as the third long (index 2)
        // covers this range (2 * 64 = 128).
        int[] expectedIndices = IntStream.range(128, 192).toArray();

        // Act
        int[] actualIndices = indexExtractor.asIndexArray();

        // Assert
        // The order of indices is not guaranteed by the API, so we sort the array before comparison
        // to ensure the test is deterministic.
        Arrays.sort(actualIndices);

        assertArrayEquals("The extracted indices should match the expected range for the third long.",
                expectedIndices, actualIndices);
    }
}
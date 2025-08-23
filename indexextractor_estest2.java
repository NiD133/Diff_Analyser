package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertThrows;
import org.junit.Test;

/**
 * Tests for the default methods of the {@link IndexExtractor} interface.
 */
public class IndexExtractorTest {

    /**
     * Tests that the default implementation of {@code uniqueIndices()} throws an
     * OutOfMemoryError when the shape specifies a number of bits that is too large
     * to be reasonably handled by an in-memory data structure.
     *
     * <p>The default implementation of {@code uniqueIndices()} may use a data
     * structure like a BitSet to identify unique indices. When the number of bits
     * in the shape is very large (e.g., close to Integer.MAX_VALUE), allocating
     * such a structure is expected to exhaust available heap memory.</p>
     */
    @Test
    public void testUniqueIndicesWithVeryLargeShapeThrowsOutOfMemoryError() {
        // Arrange
        // A hasher to generate indices. The seed values are arbitrary.
        Hasher hasher = new EnhancedDoubleHasher(-2285L, -2285L);

        // Define a shape with a very large number of bits (m). This value is
        // chosen to be close to Integer.MAX_VALUE to force the default
        // uniqueIndices() implementation to attempt a huge memory allocation.
        int numberOfHashFunctions = 6;
        int numberOfBits = 2_147_352_576; // Approx. 2.147 billion
        Shape largeShape = Shape.fromNM(numberOfHashFunctions, numberOfBits);

        IndexExtractor indexExtractor = hasher.indices(largeShape);

        // Act & Assert
        // Calling uniqueIndices() should fail by throwing an OutOfMemoryError
        // because its default implementation tries to allocate a large internal
        // buffer (e.g., a BitSet) to track the unique indices.
        assertThrows("Expected an OutOfMemoryError due to the extremely large shape",
                OutOfMemoryError.class,
                indexExtractor::uniqueIndices);
    }
}
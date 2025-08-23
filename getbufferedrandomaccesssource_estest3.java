package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Tests that the length() method correctly delegates to the underlying source,
     * even when the source reports a negative length. This is an edge case test
     * to ensure proper behavior with unusual source configurations.
     */
    @Test
    public void length_whenSourceReportsNegativeLength_returnsSourcesNegativeLength() {
        // Arrange
        final long expectedNegativeLength = -782L;

        // Create a source that reports a negative length.
        // We use a WindowRandomAccessSource with a negative length parameter to achieve this.
        RandomAccessSource emptyBaseSource = new ArrayRandomAccessSource(new byte[0]);
        RandomAccessSource sourceWithNegativeLength = new WindowRandomAccessSource(emptyBaseSource, expectedNegativeLength, expectedNegativeLength);

        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(sourceWithNegativeLength);

        // Act
        long actualLength = bufferedSource.length();

        // Assert
        assertEquals("The length should be delegated from the underlying source, even if negative.",
                expectedNegativeLength, actualLength);
    }
}
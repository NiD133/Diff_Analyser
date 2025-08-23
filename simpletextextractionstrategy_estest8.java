package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import javax.swing.text.Segment;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that appendTextChunk throws an ArrayIndexOutOfBoundsException when
     * given a Segment that was constructed with an invalid range (offset and count).
     *
     * This test ensures that the strategy correctly propagates exceptions that occur
     * when accessing the contents of an invalid CharSequence.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendTextChunk_withInvalidSegment_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Create a strategy and an invalid text segment.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // A Segment is invalid if its offset and count point to a range outside
        // the bounds of its underlying character array. Here, the offset of 1
        // is out of bounds for an empty array.
        char[] emptyCharArray = new char[0];
        Segment invalidSegment = new Segment(emptyCharArray, 1, 1);

        // Act & Assert: Calling appendTextChunk with the invalid segment should
        // immediately throw an exception when the segment's data is accessed.
        strategy.appendTextChunk(invalidSegment);
    }
}
package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import javax.swing.text.Segment;

/**
 * Unit tests for the {@link SimpleTextExtractionStrategy} class.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Verifies that appendTextChunk throws an IndexOutOfBoundsException when provided
     * with a Segment configured with an invalid (negative) offset and count.
     *
     * This test ensures that the method correctly handles malformed CharSequence inputs
     * by propagating the expected exception from the underlying string manipulation logic.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void appendTextChunk_withInvalidSegment_throwsIndexOutOfBoundsException() {
        // Arrange: Create a strategy instance and a Segment with invalid parameters.
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] dummyCharacterArray = new char[10];
        
        // A Segment with a negative offset and count is an invalid argument.
        Segment invalidSegment = new Segment(dummyCharacterArray, -1, -1);

        // Act: Attempt to append the invalid text chunk.
        // This is expected to throw an IndexOutOfBoundsException because the Segment's
        // parameters are invalid for the underlying append operation.
        strategy.appendTextChunk(invalidSegment);

        // Assert: The test passes if the expected IndexOutOfBoundsException is thrown,
        // as specified by the @Test annotation.
    }
}
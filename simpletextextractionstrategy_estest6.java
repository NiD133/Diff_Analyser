package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import javax.swing.text.Segment;

/**
 * Contains tests for the {@link SimpleTextExtractionStrategy} class,
 * focusing on its handling of invalid or edge-case inputs.
 */
public class SimpleTextExtractionStrategy_ESTestTest6 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Verifies that appendTextChunk throws a NullPointerException when passed a Segment
     * that was initialized with a null character array.
     *
     * This is the expected behavior because the method will attempt to read from the
     * CharSequence, and the Segment's implementation will try to dereference its
     * null internal array, causing the exception.
     */
    @Test(expected = NullPointerException.class)
    public void appendTextChunkWithNullBackedSegmentShouldThrowNullPointerException() {
        // Arrange: Create a strategy instance and a problematic Segment
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        Segment segmentWithNullArray = new Segment(null, 0, 0);

        // Act: Attempt to append the segment. This action is expected to throw.
        strategy.appendTextChunk(segmentWithNullArray);

        // Assert: The test succeeds if a NullPointerException is thrown, as specified
        // by the @Test(expected=...) annotation. No further assertions are needed.
    }
}
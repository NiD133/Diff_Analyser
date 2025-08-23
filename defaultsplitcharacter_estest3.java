package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test class appears to be misnamed. While its name suggests it tests DefaultSplitCharacter,
 * the test case itself focuses on the behavior of the PdfLine.add() method, which internally
 * uses a SplitCharacter implementation.
 */
public class DefaultSplitCharacter_ESTestTest3 extends DefaultSplitCharacter_ESTest_scaffolding {

    /**
     * Tests that adding a chunk to a PdfLine with negative available width results in a null overflow chunk.
     *
     * <p>In the context of PdfLine.add(), a null return value typically signifies that the entire chunk
     * was successfully added to the line. This test verifies a specific, counter-intuitive edge case
     * where the chunk is considered "fully added" even when there is negative space available.
     * This likely points to special handling for chunks that cannot be split under these conditions.
     */
    @Test
    public void addChunkToLineWithNegativeWidthReturnsNull() {
        // ARRANGE
        // 1. Create a PdfLine with arbitrary dimensions.
        final float lineLeft = 2f;
        final float lineRight = 981.5f;
        final int alignment = 4; // Corresponds to Element.ALIGN_JUSTIFIED_ALL
        final float lineHeight = 1.0E-6f;
        PdfLine line = new PdfLine(lineLeft, lineRight, alignment, lineHeight);

        // 2. Set up a PdfChunk with specific content and properties.
        // The base chunk and action are used to create a valid PdfChunk instance.
        Chunk baseChunkForProperties = new GreekList().getSymbol();
        PdfAction dummyAction = new PdfAction("[]~N<", true);
        PdfChunk chunkWithProperties = new PdfChunk(baseChunkForProperties, dummyAction);

        // The content string "(d{2,4}-d{2}-d{2,4})" is chosen deliberately. It contains a hyphen,
        // which is normally a split character. However, the default split character logic in iText
        // avoids splitting within strings that look like dates. This specific string mimics a date
        // regex pattern, likely to test that complex splitting logic.
        PdfChunk chunkToAdd = new PdfChunk("(d{2,4}-d{2}-d{2,4})", chunkWithProperties);

        // 3. Define a negative remaining width. This ensures the chunk cannot fit on the line,
        // which should normally force the add() method to attempt a split.
        float negativeRemainingWidth = -1366.4f;

        // ACT
        // Attempt to add the chunk to the line. The method is expected to return the portion
        // of the chunk that did not fit (the overflow).
        // Note: The signature `add(PdfChunk, float)` may be specific to the iText version
        // or build environment for which this test was generated.
        PdfChunk overflowChunk = line.add(chunkToAdd, negativeRemainingWidth);

        // ASSERT
        // The test asserts that no overflow chunk is returned. This implies that the entire
        // chunk was consumed by the line, despite the negative available width.
        assertNull("Expected no overflow chunk, even when adding to a line with negative width.", overflowChunk);
    }
}
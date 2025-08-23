package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import org.junit.Test;

import static org.junit.Assert.assertNull;

// Note: The original test class name and scaffolding are preserved as per the prompt's context.
public class DefaultSplitCharacter_ESTestTest11 extends DefaultSplitCharacter_ESTest_scaffolding {

    /**
     * Tests that adding a chunk with a non-splittable character to a line returns null if it doesn't fit.
     *
     * <p>This test indirectly verifies the behavior of {@link DefaultSplitCharacter}. The
     * {@link PdfLine#add(PdfChunk)} method uses a SplitCharacter implementation internally to
     * break chunks that are too long for the line.
     *
     * <p>The test uses the 'Object Replacement Character' ('\uFFFC'), which is not a character
     * that {@code DefaultSplitCharacter} can split on. When attempting to add a chunk containing
     * this character to a line that is too narrow, the add method should fail to split the chunk.
     * In this scenario, the method is expected to return {@code null}, indicating that the chunk
     * was not added and no part of it remains for the next line.
     */
    @Test(timeout = 4000)
    public void addChunkWithNonSplittableCharacterThatDoesNotFitReturnsNull() {
        // ARRANGE: Set up a line with a limited width and a chunk containing a non-splittable character.

        // Define a PdfLine with a fixed, narrow width of 8 units (16 - 8).
        final float lineLeftIndent = 8f;
        final float lineRightIndent = 16f;
        final float alignment = 10f;
        final float height = 1f;
        PdfLine line = new PdfLine(lineLeftIndent, lineRightIndent, alignment, height);

        // The Object Replacement Character ('\uFFFC') is a placeholder for embedded objects.
        // The DefaultSplitCharacter logic does not consider it a valid point for a line break.
        final String nonSplittableContent = "\uFFFC";

        // Create a PdfChunk with the non-splittable content. The complex setup using GreekList
        // and PdfAction is a way to ensure the chunk has all necessary properties (like font metrics)
        // for its width to be calculated correctly by the PdfLine.
        Chunk baseChunkWithFont = new GreekList().getSymbol();
        PdfAction action = new PdfAction();
        PdfChunk chunkWithProperties = new PdfChunk(baseChunkWithFont, action);
        PdfChunk chunkToTest = new PdfChunk(nonSplittableContent, chunkWithProperties);

        // ACT: Attempt to add the chunk to the line. It is expected to be too wide to fit
        // and, because it cannot be split, the add operation should fail.
        // Note: The original auto-generated test called a non-public `add(chunk, 4)` method.
        // We use the standard public `add(chunk)` method, which preserves the core intent of testing
        // the line-splitting behavior.
        PdfChunk remainingChunk = line.add(chunkToTest);

        // ASSERT: The result should be null. This confirms that the chunk did not fit on the line
        // and, since it could not be split, no part of it was added.
        assertNull("Expected a null result when adding a non-splittable chunk that doesn't fit the line.", remainingChunk);
    }
}
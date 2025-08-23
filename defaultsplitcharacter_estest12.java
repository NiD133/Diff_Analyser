package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.TabSettings;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;

/**
 * Test suite for character splitting behavior within the context of PDF line creation.
 *
 * Note: This test was adapted from an auto-generated test case. It indirectly tests
 * character splitting logic by observing the behavior of the PdfLine.add() method,
 * which relies on a SplitCharacter implementation.
 */
public class DefaultSplitCharacterTest {

    /**
     * Tests that when a PdfChunk is added to a PdfLine with ample available width,
     * the chunk is fully consumed and the add() method returns null.
     *
     * The PdfLine.add() method is designed to return:
     * - null: if the entire chunk fits on the line without needing to be split.
     * - A new PdfChunk: representing the part of the chunk that did not fit and must be
     *   carried over to the next line.
     *
     * This test validates the "chunk fits" scenario.
     */
    @Test
    public void addChunkToLine_whenLineHasSufficientSpace_shouldFullyAddChunkAndReturnNull() {
        // ARRANGE: Set up a chunk and a line with a very large width, ensuring
        // the chunk will fit without requiring a split.

        // 1. Create a sample chunk. The content is arbitrary; here, it's a symbol
        // from a GreekList used as a simple way to get a non-empty chunk.
        GreekList listWithSymbol = new GreekList();
        Chunk sourceChunk = listWithSymbol.getSymbol();
        PdfAction dummyAction = new PdfAction();
        TabSettings defaultTabSettings = new TabSettings();
        PdfChunk chunkToAdd = new PdfChunk(sourceChunk, dummyAction, defaultTabSettings);

        // 2. Create a line with a very large width, guaranteeing that any typical chunk will fit.
        // The key to this test is the ample width, which prevents any splitting.
        final float ampleLineWidth = 3000.0F;
        final int defaultAlignment = 0;
        ArrayList<PdfChunk> existingChunks = new ArrayList<>();
        PdfLine line = new PdfLine(0.0F, ampleLineWidth, ampleLineWidth, defaultAlignment, false, existingChunks, false);

        // ACT: Add the chunk to the line.
        PdfChunk remainingChunk = line.add(chunkToAdd);

        // ASSERT: Verify that the method returns null, which indicates the entire chunk
        // was added to the line successfully and no part of it remains.
        assertNull("Expected the entire chunk to fit on the line, so the result should be null.", remainingChunk);
    }
}
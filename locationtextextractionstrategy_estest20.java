package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationTextExtractionStrategy_ESTestTest20 {

    /**
     * Tests that getResultantText throws a NullPointerException if the strategy
     * is populated with TextChunks that have a null location.
     * <p>
     * This scenario is simulated by using a mock TextChunkLocationStrategy that
     * always returns null. This causes {@link LocationTextExtractionStrategy#renderText}
     * to create invalid TextChunks. The subsequent call to getResultantText fails
     * when it tries to sort these chunks, as the sorting logic in
     * {@link LocationTextExtractionStrategy.TextChunk#compareTo} dereferences the null location.
     */
    @Test
    public void getResultantText_whenChunksHaveNullLocation_throwsNullPointerException() {
        // ARRANGE: Create a mock location strategy that returns null for any location creation.
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationCreationStrategy =
                mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class);
        when(mockLocationCreationStrategy.createLocation(any(TextRenderInfo.class), any(LineSegment.class)))
                .thenReturn(null);

        // ARRANGE: Instantiate the extraction strategy with the misbehaving mock.
        LocationTextExtractionStrategy extractionStrategy =
                new LocationTextExtractionStrategy(mockLocationCreationStrategy);

        // ARRANGE: Create dummy render info and process it twice. This populates the
        // strategy's internal list with two TextChunks, both having null locations.
        TextRenderInfo dummyRenderInfo = createDummyTextRenderInfo();
        extractionStrategy.renderText(dummyRenderInfo);
        extractionStrategy.renderText(dummyRenderInfo);

        // ACT & ASSERT: Expect a NullPointerException when getResultantText tries to sort the chunks.
        try {
            extractionStrategy.getResultantText(null); // The filter is not relevant to this test.
            fail("Expected a NullPointerException because TextChunk locations are null, causing a failure during sorting.");
        } catch (NullPointerException expected) {
            // This is the expected outcome. The exception is thrown from TextChunk.compareTo
            // when it attempts to access the 'location' field, which is null.
        }
    }

    /**
     * Helper method to create a valid TextRenderInfo object with minimal boilerplate.
     *
     * @return A dummy TextRenderInfo instance for testing.
     */
    private TextRenderInfo createDummyTextRenderInfo() {
        GraphicsState graphicsState = new GraphicsState();
        // A font must be set in the graphics state, otherwise renderText will do nothing.
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        return new TextRenderInfo(
                new PdfString("dummy text"),
                graphicsState,
                new Matrix(),
                Collections.emptyList()
        );
    }
}
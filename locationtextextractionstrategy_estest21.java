package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/*
 * Note: The class name "LocationTextExtractionStrategy_ESTestTest21" is an artifact of
 * a test generation tool. In a real-world scenario, it would be renamed to something
 * more descriptive, like "LocationTextExtractionStrategyTest".
 */
public class LocationTextExtractionStrategy_ESTestTest21 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Verifies that getResultantText() throws a NullPointerException if the TextChunkLocationStrategy
     * returns a null location.
     *
     * The getResultantText() method sorts the collected text chunks. This sorting relies on the
     * TextChunk's location object. If that location is null, the comparison logic inside
     * TextChunk.compareTo() will fail with an NPE. This test ensures that this failure mode is
     * handled as expected.
     */
    @Test
    public void getResultantText_whenLocationStrategyReturnsNull_throwsNullPointerException() {
        // ARRANGE
        // 1. Create a mock location strategy that always returns null. This simulates a scenario
        // where a location cannot be determined for a piece of text.
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy =
                mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class);
        doReturn(null).when(mockLocationStrategy).createLocation(
                any(TextRenderInfo.class),
                any(LineSegment.class)
        );

        // 2. Instantiate the strategy under test with our misbehaving mock.
        LocationTextExtractionStrategy extractionStrategy = new LocationTextExtractionStrategy(mockLocationStrategy);

        // 3. Create dummy render info. The specific content is not important, but a font object
        // must be present to avoid an earlier NPE.
        GraphicsState dummyGraphicsState = new GraphicsState();
        dummyGraphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());
        TextRenderInfo dummyRenderInfo = new TextRenderInfo(
                new PdfString("dummy text"),
                dummyGraphicsState,
                new Matrix(),
                Collections.emptyList()
        );

        // ACT
        // Process text twice. This adds two TextChunk objects to the strategy's internal list.
        // Because of our mock, both chunks will have a null 'location' field.
        extractionStrategy.renderText(dummyRenderInfo);
        extractionStrategy.renderText(dummyRenderInfo);

        // ASSERT
        // Attempting to get the result will trigger a sort, causing the NPE.
        try {
            extractionStrategy.getResultantText();
            fail("Expected a NullPointerException because the TextChunk's location was null.");
        } catch (NullPointerException e) {
            // Verify the exception originates from the expected place. The TextChunk class
            // attempts to delegate the comparison to its null location object.
            StackTraceElement topOfStack = e.getStackTrace()[0];
            assertEquals(
                "The NullPointerException should originate from the TextChunk class during comparison.",
                "com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy$TextChunk",
                topOfStack.getClassName()
            );
        }
    }
}
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The original test class name is preserved for context.
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class LocationTextExtractionStrategy_ESTestTest22 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that a space is inserted between two consecutive, identical text chunks.
     *
     * This test verifies that when the strategy processes two text chunks that are assigned
     * the exact same location, it correctly identifies them as separate words and inserts a
     * space. This ensures that even in edge cases (like overlapping or zero-distance text),
     * the strategy produces readable output rather than merging the text.
     */
    @Test(timeout = 4000)
    public void getResultantText_withTwoIdenticalChunks_insertsSpaceSeparator() {
        // ARRANGE

        // 1. Mock the strategy for creating text chunk locations. We will force it to
        //    return the same location object for both text chunks processed.
        LocationTextExtractionStrategy.TextChunkLocationStrategy mockLocationStrategy =
                mock(LocationTextExtractionStrategy.TextChunkLocationStrategy.class);

        // 2. Define the location that the mock will return.
        //    Start and end vectors are identical, representing a zero-length chunk.
        Vector startAndEndVector = new Vector(1, 17, 19);
        // A negative character space width is used, mirroring the original test's setup.
        // This value ensures the word boundary condition `dist > spaceWidth / 2.0f` is met
        // when the distance between chunks is zero.
        float charSpaceWidth = -2928.7646F;
        LocationTextExtractionStrategy.TextChunkLocation identicalLocation =
                new LocationTextExtractionStrategy.TextChunkLocationDefaultImp(startAndEndVector, startAndEndVector, charSpaceWidth);

        // Configure the mock to always return our predefined location.
        when(mockLocationStrategy.createLocation(any(TextRenderInfo.class), any(LineSegment.class)))
                .thenReturn(identicalLocation);

        // 3. Create a minimal TextRenderInfo object. The actual text content is empty,
        //    as the focus is on how the strategy handles the locations.
        TextRenderInfo textRenderInfo = createEmptyTextRenderInfo();

        // 4. Instantiate the strategy-under-test with our mocked dependency.
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy(mockLocationStrategy);


        // ACT

        // Render the same text info twice. This creates two text chunks,
        // both of which will be assigned the 'identicalLocation' by our mock.
        strategy.renderText(textRenderInfo);
        strategy.renderText(textRenderInfo);

        // Retrieve the final text without applying any filters.
        String resultantText = strategy.getResultantText((LocationTextExtractionStrategy.TextChunkFilter) null);


        // ASSERT

        // The strategy should place a space between the two (empty) chunks because it
        // detects a word boundary, resulting in a single space character.
        assertEquals(" ", resultantText);
    }

    /**
     * Helper method to create a minimal but valid TextRenderInfo object
     * representing an empty text string. This encapsulates boilerplate setup.
     *
     * @return A new TextRenderInfo instance.
     */
    private TextRenderInfo createEmptyTextRenderInfo() {
        GraphicsState graphicsState = new GraphicsState();
        // A font must be set for the strategy to work correctly.
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        // The original test used a default PdfDate, which acts as an empty PdfString.
        PdfString emptyText = new PdfString("");
        Matrix identityMatrix = new Matrix();

        return new TextRenderInfo(emptyText, graphicsState, identityMatrix, new LinkedList<>());
    }
}
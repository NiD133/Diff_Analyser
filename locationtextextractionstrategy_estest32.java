package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

// Note: The test class name and inheritance from a scaffolding class are preserved
// to maintain consistency with the original test suite structure.
public class LocationTextExtractionStrategy_ESTestTest32 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    /**
     * Tests that the strategy inserts a newline character between two text chunks
     * that are determined to be on different lines based on their transformation matrices.
     *
     * This test simulates rendering two text chunks. Both have effectively empty text content,
     * but they are given different transformation matrices. The second matrix is designed to
     * alter the position and orientation of its text chunk enough for the strategy to
     * classify it as starting on a new line relative to the first.
     */
    @Test
    public void getResultantText_whenChunksAreOnDifferentLines_separatesWithNewline() {
        // Arrange
        LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();

        // Setup a minimal graphics state. The font is created from an empty dictionary,
        // which results in any rendered text being empty. This is acceptable as this
        // test focuses on the spatial separation logic, not the text content itself.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfDictionary());

        // The first text chunk is positioned using the identity matrix.
        // An empty PdfString makes it explicit that the text content is not relevant.
        Matrix identityMatrix = new Matrix();
        TextRenderInfo firstChunkInfo = new TextRenderInfo(
                new PdfString(""),
                graphicsState,
                identityMatrix,
                Collections.<MarkedContentInfo>emptyList()
        );

        // The second text chunk is positioned using a different matrix that alters its
        // location and orientation, causing the strategy to place it on a new line.
        Matrix transformedMatrix = new Matrix(341.6828F, -9.18953F, 5f, 23f, 6f, 3f);
        TextRenderInfo secondChunkInfo = new TextRenderInfo(
                new PdfString(""),
                graphicsState,
                transformedMatrix,
                Collections.<MarkedContentInfo>emptyList()
        );

        // Act
        strategy.renderText(firstChunkInfo);
        strategy.renderText(secondChunkInfo);
        String resultantText = strategy.getResultantText();

        // Assert
        // The strategy should detect that the two (empty) text chunks are on different
        // lines and should separate them with a newline character. The final string
        // contains only the newline because the text content of the chunks is empty.
        assertEquals("\n", resultantText);
    }
}
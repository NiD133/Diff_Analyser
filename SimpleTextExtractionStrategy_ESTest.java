/*
 * Copyright (c) 2022 iText Group NV
 *
 * This test class is for the SimpleTextExtractionStrategy class.
 * It has been refactored from an auto-generated EvoSuite test to improve
 * clarity, maintainability, and developer understanding.
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Test;

import javax.swing.text.Segment;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SimpleTextExtractionStrategy}.
 *
 * This class tests the core functionality of the strategy, including its initial state,
 * text appending logic, handling of no-op methods, and responses to various
 * valid and invalid inputs.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * Creates a mock TextRenderInfo that produces an empty string upon rendering.
     * This is useful for testing the positional logic of the extraction strategy
     * (i.e., when it decides to add spaces or newlines) without complicating
     * the test with actual text content.
     *
     * @return A TextRenderInfo object with valid geometry that yields empty text.
     */
    private TextRenderInfo createRenderInfoWithEmptyText() {
        GraphicsState graphicsState = new GraphicsState();
        // This font setup is a trick to create a font that doesn't decode any characters,
        // resulting in an empty string from renderInfo.getText().
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction());
        Matrix matrix = new Matrix(1, 0, 0, 1, 100, 100); // Set a specific position
        return new TextRenderInfo(new PdfDate(), graphicsState, matrix, Collections.emptySet());
    }

    @Test
    public void getResultantText_should_returnEmptyString_onInitialization() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act
        String result = strategy.getResultantText();

        // Assert
        assertEquals("The initial text should be empty.", "", result);
    }

    @Test
    public void getResultantText_should_returnAppendedText_when_chunkIsAdded() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String expectedText = "some appended text";

        // Act
        strategy.appendTextChunk(expectedText);

        // Assert
        assertEquals("The result should match the appended text.", expectedText, strategy.getResultantText());
    }

    @Test
    public void renderText_should_notAddSeparator_whenRenderingAtSamePositionAfterManualAppend() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        TextRenderInfo renderInfo = createRenderInfoWithEmptyText();

        // Act
        // 1. Render at a position. This sets the "last position" in the strategy.
        strategy.renderText(renderInfo);

        // 2. Manually append some text. This should not reset the last position.
        strategy.appendTextChunk("manually_appended");

        // 3. Render at the exact same position again.
        strategy.renderText(renderInfo);

        // Assert
        // The strategy should not add a space or newline because the second render
        // is at the same position as the first. The text from renderText itself is
        // empty, so only the manually appended text should be present.
        assertEquals("manually_appended", strategy.getResultantText());
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void renderText_should_throwUnsupportedCharsetException_forInvalidEncoding() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction()); // A dummy font object
        // A PdfString with an encoding that is not a valid charset name
        PdfString textWithInvalidEncoding = new PdfString("some text", "Times-BoldItalic");
        TextRenderInfo renderInfo = new TextRenderInfo(textWithInvalidEncoding, graphicsState, new Matrix(), Collections.emptySet());

        // Act
        strategy.renderText(renderInfo); // Should throw
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void renderText_should_throwIllegalCharsetNameException_forIllegalEncodingName() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction()); // A dummy font object
        // A PdfString with an illegal charset name
        PdfString textWithIllegalEncoding = new PdfString("some text", ">|");
        TextRenderInfo renderInfo = new TextRenderInfo(textWithIllegalEncoding, graphicsState, new Matrix(), Collections.emptySet());

        // Act
        strategy.renderText(renderInfo); // Should throw
    }

    @Test(expected = NullPointerException.class)
    public void renderText_should_throwNullPointerException_when_renderInfoLacksGeometryAndTextExists() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("some initial text"); // This makes the next render not the "first" one.

        // Create a TextRenderInfo using a constructor that doesn't set up geometry vectors.
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.font = new CMapAwareDocumentFont(new PdfAction());
        TextRenderInfo renderInfoWithoutGeometry = new TextRenderInfo(
            new PdfDate(), // A generic PdfObject
            graphicsState,
            new Matrix(),
            Collections.emptySet()
        );

        // Act
        // This will fail because it tries to access geometry (e.g., getBaseline())
        // which is null for this TextRenderInfo object on a non-initial render.
        strategy.renderText(renderInfoWithoutGeometry);
    }

    @Test(expected = NullPointerException.class)
    public void appendTextChunk_should_throwNullPointerException_when_segmentArrayIsNull() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        Segment segment = new Segment(null, 0, 10);

        // Act
        strategy.appendTextChunk(segment); // Should throw
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void appendTextChunk_should_throwException_when_segmentIndicesAreInvalid() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] chars = new char[10];
        // Create a segment with a negative offset, which is invalid.
        Segment segment = new Segment(chars, -1, 5);

        // Act
        strategy.appendTextChunk(segment); // Should throw
    }

    @Test
    public void beginTextBlock_should_haveNoEffectOnResult() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("existing text");

        // Act
        strategy.beginTextBlock();

        // Assert
        assertEquals("beginTextBlock should be a no-op.", "existing text", strategy.getResultantText());
    }

    @Test
    public void endTextBlock_should_haveNoEffectOnResult() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("existing text");

        // Act
        strategy.endTextBlock();

        // Assert
        assertEquals("endTextBlock should be a no-op.", "existing text", strategy.getResultantText());
    }

    @Test
    public void renderImage_should_haveNoEffectOnResult() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("existing text");
        ImageRenderInfo imageRenderInfo = ImageRenderInfo.createForXObject(new GraphicsState(), null, new PdfDictionary(), Collections.emptySet());

        // Act
        strategy.renderImage(imageRenderInfo);

        // Assert
        assertEquals("renderImage should be a no-op for this strategy.", "existing text", strategy.getResultantText());
    }
}
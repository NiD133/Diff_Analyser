package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import org.junit.Test;

import javax.swing.text.Segment;
import java.nio.CharBuffer;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Readable, behavior-focused tests for SimpleTextExtractionStrategy.
 *
 * These tests intentionally avoid complex PDF constructs and exception-driven scenarios
 * produced by fuzzing tools, and instead verify the core public contract:
 * - result starts empty
 * - text can be appended via CharSequence
 * - begin/end text block are no-ops
 * - image rendering is a no-op
 */
public class SimpleTextExtractionStrategyTest {

    @Test
    public void returnsEmptyStringByDefault() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Assert
        assertEquals("Expected no text by default", "", strategy.getResultantText());
    }

    @Test
    public void appendTextChunk_appendsStringBuffer() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act
        strategy.appendTextChunk(new StringBuffer("Hello"));
        strategy.appendTextChunk(new StringBuffer(", world"));

        // Assert
        assertEquals("Hello, world", strategy.getResultantText());
    }

    @Test
    public void appendTextChunk_withCharBuffer_appendsRemainingChars() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        CharBuffer buffer = CharBuffer.wrap("abcde");
        buffer.position(2); // remaining = "cde"

        // Act
        strategy.appendTextChunk(buffer);

        // Assert
        assertEquals("cde", strategy.getResultantText());
    }

    @Test
    public void appendTextChunk_withSegment_appendsOnlySegmentSlice() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] chars = "abcde".toCharArray();
        Segment segment = new Segment(chars, 1, 3); // "bcd"

        // Act
        strategy.appendTextChunk(segment);

        // Assert
        assertEquals("bcd", strategy.getResultantText());
    }

    @Test
    public void beginAndEndTextBlock_doNotChangeResult() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("before");

        // Act
        strategy.beginTextBlock();
        strategy.endTextBlock();

        // Assert
        assertEquals("begin/end are expected to be no-ops", "before", strategy.getResultantText());
    }

    @Test
    public void renderImage_isNoOp() {
        // Arrange
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.appendTextChunk("text");
        GraphicsState gs = new GraphicsState();

        // Create a minimal ImageRenderInfo instance.
        // The renderer is documented as not being interested in image events,
        // so this call should not affect the accumulated text.
        ImageRenderInfo imageInfo = ImageRenderInfo.createForXObject(
                gs,
                (PdfIndirectReference) null,
                new PdfDictionary(),
                Collections.<MarkedContentInfo>emptyList()
        );

        // Act
        strategy.renderImage(imageInfo);

        // Assert
        assertEquals("text", strategy.getResultantText());
    }
}
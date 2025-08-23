package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link SimpleTextExtractionStrategy}, focusing on the consistency of text extraction.
 */
public class SimpleTextExtractionStrategyTest {

    /**
     * A test-specific strategy that forces text to be processed character-by-character.
     * This is used to validate that the default chunk-based processing of
     * {@link SimpleTextExtractionStrategy} produces a consistent result.
     */
    private static class CharacterByCharacterExtractionStrategy extends SimpleTextExtractionStrategy {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            // The default strategy processes the entire TextRenderInfo object as a single chunk.
            // This implementation breaks it down and processes each character individually
            // by calling the base class's renderText for each character's render info.
            for (TextRenderInfo characterRenderInfo : renderInfo.getCharacterRenderInfos()) {
                super.renderText(characterRenderInfo);
            }
        }
    }

    /**
     * Verifies that extracting text using the standard chunk-based approach yields the
     * same result as extracting it character-by-character. This ensures that the
     * strategy's internal logic for spacing and line breaks is consistent, regardless
     * of how text is chunked in the PDF content stream.
     */
    @Test
    public void extractionByChunkAndByCharacterShouldYieldSameResult() throws IOException {
        // ARRANGE
        // A real-world PDF is used as a comprehensive regression test case.
        String resourceName = "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";

        // Use try-with-resources for automatic and safe resource management.
        try (InputStream resourceStream = TestResourceUtils.getResourceAsStream(this, resourceName);
             PdfReader reader = new PdfReader(resourceStream)) {

            // ACT
            // 1. Extract text using the standard strategy (processes text in chunks).
            String textExtractedByChunk = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy())
                                          + "\n"
                                          + PdfTextExtractor.getTextFromPage(reader, 2, new SimpleTextExtractionStrategy());

            // 2. Extract text using the character-by-character strategy.
            String textExtractedByCharacter = PdfTextExtractor.getTextFromPage(reader, 1, new CharacterByCharacterExtractionStrategy())
                                              + "\n"
                                              + PdfTextExtractor.getTextFromPage(reader, 2, new CharacterByCharacterExtractionStrategy());

            // ASSERT
            Assert.assertEquals(
                "Extraction result should be identical whether processing by chunk or by character.",
                textExtractedByChunk,
                textExtractedByCharacter
            );
        }
    }
}
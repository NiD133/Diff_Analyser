package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains regression tests for text extraction, focusing on specific bugs.
 */
public class TextExtractionBugsTest {

    private static final int FIRST_PAGE = 1;

    /**
     * Verifies that Japanese text is extracted correctly.
     * <p>
     * This test was introduced to fix a bug where processing certain Japanese characters
     * in TextRenderInfo caused an ArrayIndexOutOfBoundsException. It ensures the text extraction
     * does not crash and that the extracted content matches the expected string.
     */
    @Test
    public void extractingJapaneseText_shouldReturnCorrectText() throws Exception {
        // The expected text from the first line of the PDF resource.
        // Using the literal string is more readable than Unicode escapes.
        String expectedText = "直近のＳ＆Ｐ５００配当貴族指数のパフォーマンスはＳ＆Ｐ５００指数を上回る";
        String resourceName = "japanese_text.pdf";

        // Arrange: Load the PDF and prepare the extraction strategy.
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, resourceName);
        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        // Act: Extract text from the first page.
        String extractedText = PdfTextExtractor.getTextFromPage(reader, FIRST_PAGE, strategy);

        // Assert: Verify the extracted text is correct after trimming whitespace.
        // Using trim() is more robust than relying on substring and newline characters.
        Assert.assertEquals(expectedText, extractedText.trim());
    }
}
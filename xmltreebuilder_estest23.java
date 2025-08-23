package org.jsoup.parser;

import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link XmlTreeBuilder#parse(java.io.Reader, String)} method.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the parse method throws an IllegalArgumentException
     * when the provided base URI is null.
     */
    @Test
    public void parseWithNullBaseUriThrowsIllegalArgumentException() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // The content of the reader is irrelevant for this test, as the baseUri check happens first.
        StringReader emptyReader = new StringReader("");
        String expectedErrorMessage = "The parameter 'baseUri' must not be null.";

        // Act & Assert
        try {
            xmlTreeBuilder.parse(emptyReader, null);
            fail("Expected an IllegalArgumentException to be thrown for a null baseUri.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}
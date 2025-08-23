package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the parse method throws an IllegalArgumentException
     * when the provided base URI is null, as this is a required parameter.
     */
    @Test
    public void parseWithNullBaseUriShouldThrowIllegalArgumentException() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String irrelevantXml = "<data>test</data>";
        String expectedErrorMessage = "The parameter 'baseUri' must not be null.";

        // Act & Assert
        try {
            xmlTreeBuilder.parse(irrelevantXml, null);
            fail("An IllegalArgumentException should have been thrown for a null baseUri.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}
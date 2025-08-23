package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static {@link Attribute#getValidKey(String, Document.OutputSettings.Syntax)} method.
 */
public class AttributeTest {

    /**
     * Tests that getValidKey correctly sanitizes an attribute key for XML syntax
     * by replacing invalid characters with underscores.
     */
    @Test
    public void getValidKeyShouldReplaceInvalidXmlCharacters() {
        // Arrange
        String invalidKey = "kQ N^LU"; // Contains space and caret, which are invalid in XML attribute names.
        String expectedKey = "kQ_N_LU";
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;

        // Act
        String actualKey = Attribute.getValidKey(invalidKey, syntax);

        // Assert
        assertEquals(expectedKey, actualKey);
    }
}
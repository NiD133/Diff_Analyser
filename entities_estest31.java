package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities} class, focusing on escaping behavior.
 */
public class EntitiesTest {

    /**
     * Verifies that when using XML syntax, the escape method correctly handles
     * characters that have special meaning in XML. Specifically, it should escape
     * the ampersand ('&') and the greater-than symbol ('>') when it is part of a
     * CDATA section terminator (']]>').
     */
    @Test
    public void escapeWithXmlSyntaxCorrectlyHandlesAmpersandAndCdataTerminator() {
        // Arrange
        String input = "&c\n/*]]>*/";
        String expectedOutput = "&amp;c\n/*]]&gt;*/";

        OutputSettings xmlOutputSettings = new OutputSettings()
                .syntax(OutputSettings.Syntax.xml);

        // Act
        String escapedString = Entities.escape(input, xmlOutputSettings);

        // Assert
        assertEquals(expectedOutput, escapedString);
    }
}
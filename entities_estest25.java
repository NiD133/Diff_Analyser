package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are preserved as they might be part of a larger test suite setup.
public class Entities_ESTestTest25 extends Entities_ESTest_scaffolding {

    /**
     * Tests that the less-than character ('<') is correctly escaped to "&lt;"
     * when the output syntax is set to XML.
     * This test targets the package-private `escape` method that writes to an Appendable.
     */
    @Test(timeout = 4000)
    public void escapeWithXmlSyntaxShouldEscapeLessThanCharacter() {
        // Arrange
        String input = "s{otKya)T<'/ETl/L";
        String expectedOutput = "s{otKya)T&lt;'/ETl/L";

        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);

        Document.OutputSettings outputSettings = new Document.OutputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(Entities.EscapeMode.extended);

        // The original test used a magic number (2694) for the options parameter.
        // This has been replaced with 0, representing no special options, for clarity.
        // The core behavior of escaping '<' in XML mode is unaffected by this parameter.
        int options = 0;

        // Act
        Entities.escape(appendable, input, outputSettings, options);

        // Assert
        assertEquals(expectedOutput, writer.toString());
    }
}
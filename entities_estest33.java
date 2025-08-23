package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class Entities_ESTestTest33 extends Entities_ESTest_scaffolding {

    /**
     * Tests that the internal Entities.escape() method correctly collapses multiple
     * whitespace characters (spaces, tabs, newlines) into a single space when the
     * 'Normalise' option is specified.
     */
    @Test
    public void escapeWithNormaliseOptionCollapsesMultipleWhitespaceCharacters() {
        // Arrange
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String inputWithMultipleWhitespace = "   \n  \t "; // A mix of spaces, newline, and tab
        
        // The 'Normalise' option flag instructs the escape method to collapse whitespace.
        int options = Entities.Normalise;

        // Act
        Entities.escape(appendable, inputWithMultipleWhitespace, outputSettings, options);

        // Assert
        String expectedOutput = " ";
        assertEquals("Multiple whitespace characters should be collapsed to a single space.",
                expectedOutput, writer.toString());
    }
}
package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link Entities}.
 */
public class EntitiesTest {

    @Test
    public void escapeWithHtmlSettingsShouldEscapeSpecialCharacters() {
        // Arrange
        String input = "Handles <, >, &, and \" characters";
        String expectedOutput = "Handles &lt;, &gt;, &amp;, and &quot; characters";

        StringWriter stringWriter = new StringWriter();
        // The method under test requires a QuietAppendable, which wraps our StringWriter.
        QuietAppendable appendable = new QuietAppendable(stringWriter);

        Document.OutputSettings settings = new Document.OutputSettings();
        // Explicitly set syntax to HTML for clarity, even though it's the default.
        settings.syntax(Document.OutputSettings.Syntax.html);

        // Act
        // We call the escape method to write the escaped output into our appendable.
        // The last argument 'options' is a package-private bitmask.
        // 0 represents the default behavior for escaping text content.
        Entities.escape(appendable, input, settings, 0);

        // Assert
        // 1. Verify the primary functionality: the string is correctly escaped.
        assertEquals(expectedOutput, stringWriter.toString());

        // 2. Verify the original test's intent: the settings object is not mutated.
        assertEquals(Document.OutputSettings.Syntax.html, settings.syntax());
    }
}
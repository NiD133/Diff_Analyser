package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the translator correctly handles an ampersand that is not
     * part of a valid numeric entity. It should be treated as literal text
     * and passed through to the output unchanged.
     */
    @Test
    public void translateShouldPassThroughAmpersandThatIsNotANumericEntity() throws IOException {
        // Arrange
        // The unescaper is created with default options (semicolon required for entities).
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final StringWriter writer = new StringWriter();
        final String input = "&test";

        // Act
        unescaper.translate(input, writer);

        // Assert
        // The input string should be written to the writer without any changes,
        // as '&t' is not a valid start to a numeric entity.
        assertEquals("The ampersand should be treated as a literal character", "&test", writer.toString());
    }
}